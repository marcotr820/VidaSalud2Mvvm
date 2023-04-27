package com.example.vidasalud2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.AuthRepository
import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.data.DataStore.DataStoreRepositoryManager
import com.example.vidasalud2.data.model.DataResult
import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.data.model.ResponseHttp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

//
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dataStoreRepositoryManager: DataStoreRepositoryManager
) : ViewModel() {

    private val _isloading = MutableLiveData<Boolean>(false)
    val isloading: LiveData<Boolean> get() = _isloading

    private val _resp = MutableLiveData<ResponseHttp<DataResult>>()
    val resp: LiveData<ResponseHttp<DataResult>> get() = _resp

    fun login(loginModel: LoginModel) {
        _isloading.postValue(true)
        viewModelScope.launch {
            delay(1000)
            try {
                val result = repository.loginRepository(loginModel)
                if (result.isSuccessful) {
                    dataStoreRepositoryManager.saveToken(DataStorePreferencesKeys.TOKEN, result.body()?.dataResult?.token!!)

                    renovarToken()

                } else {
                    val jsonObjError = JSONObject(result.errorBody()!!.charStream().readText())
                    _resp.postValue(ResponseHttp(error = "${jsonObjError.getString("error")}"))
                }
            }
            catch(e: Exception) {
                _resp.postValue(ResponseHttp(error = e.message))
            } finally {
                delay(230)
                _isloading.postValue(false)
            }
        }
    }

    private fun renovarToken() {
        viewModelScope.launch {
            try {
                val resultado = repository.renovarToken()
                if (resultado.isSuccessful) {
                    _resp.postValue(resultado.body())
                } else {
                    _resp.postValue(ResponseHttp(error = "Error Petición."))
                }
            } catch (e: Exception) {
                _resp.postValue(ResponseHttp(error = e.message))
            }
        }
    }
}