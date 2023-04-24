package com.example.vidasalud2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.LoginRepository
import com.example.vidasalud2.core.TokenRepository
import com.example.vidasalud2.data.model.DataResult
import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.data.model.ResponseHttp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

//
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

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
                    _resp.postValue(result.body())
                } else {
                    val jObjError = JSONObject(result.errorBody()!!.charStream().readText())
                    _resp.postValue(ResponseHttp(error = "${jObjError.getString("error")}"))
                }
                _isloading.postValue(false)
            }
            catch(e: Exception) {
                Log.d("AQUIIII", "errorServidor:${e.message}")
                _resp.postValue(ResponseHttp(error = e.message))
                _isloading.postValue(false)
            }
        }
    }
}