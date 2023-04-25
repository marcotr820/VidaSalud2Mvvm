package com.example.vidasalud2.ui.viewmodel

import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.CuentasRepository
import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.data.DataStore.DataStoreRepositoryImplement
import com.example.vidasalud2.data.model.DataResult
import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.data.model.ResponseHttp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

//
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: CuentasRepository, private val dataStoreRepositoryImplement: DataStoreRepositoryImplement) : ViewModel() {

    private val _isloading = MutableLiveData<Boolean>(false)
    val isloading: LiveData<Boolean> get() = _isloading

    private val _resp = MutableLiveData<ResponseHttp<DataResult>>()
    val resp: LiveData<ResponseHttp<DataResult>> get() = _resp

    fun login(loginModel: LoginModel) {
        _isloading.postValue(true)
        viewModelScope.launch {
            try {
                val result = repository.loginRepository(loginModel)
                if (result.isSuccessful) {
                    Log.d("AQUIIII", "success:${result.body()}")
                    dataStoreRepositoryImplement.saveToken(DataStorePreferencesKeys.TOKEN, result.body()?.dataResult?.token!!)
                    val datos = repository.renovarToken()
                    Log.d("AQUIIII", "renovartoken:${datos.body()}")
                    _resp.postValue(datos.body())
                } else {
                    val jObjError = JSONObject(result.errorBody()!!.charStream().readText())
                    _resp.postValue(ResponseHttp(error = "${jObjError.getString("error")}"))
                }
                _isloading.postValue(false)
            }
            catch(e: Exception) {
                //Log.d("AQUIIII", "errorServidor:${e.message}")
                _resp.postValue(ResponseHttp(error = e.message))
                _isloading.postValue(false)
            }
        }
    }

//    private fun renovarToken(): ResponseHttp<DataResult> {
//        viewModelScope.launch {
//            return try {
//                val datos = repository.renovarToken()
//                datos
//            } catch (e: Exception) {
//
//            }
//        }
//    }
}