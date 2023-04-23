package com.example.vidasalud2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vidasalud2.LoginRepository
import com.example.vidasalud2.model.LoginModel
import com.example.vidasalud2.model.ResponseHttp
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    private val _isloading = MutableLiveData<Boolean>(false)
    val isloading: LiveData<Boolean> get() = _isloading

    private val _resp = MutableLiveData<ResponseHttp>()
    val resp: LiveData<ResponseHttp> get() = _resp

    private val _username = MutableLiveData<String?>(null)
    val username: LiveData<String?> get() = _username

    fun login(loginModel: LoginModel) {

        if (!validarForm(loginModel)) {
            return
        }

        _isloading.postValue(true)
        repository.loginRepository(loginModel).enqueue(
            object : Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    if (response.isSuccessful){
                        _resp.postValue(response.body())
                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.charStream().readText())
                        _resp.postValue(ResponseHttp(error = "${jObjError.getString("error")}"))
                    }
                    _isloading.postValue(false)
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    _resp.postValue(ResponseHttp(error = "ERROR EN EL SERVIDOR"))
                    _isloading.postValue(false)
                }

            }
        )
    }

    private fun validarForm(loginModel: LoginModel): Boolean {
        if (loginModel.userName.isBlank()) {
            _username.postValue("el campo no puede ser blanco")
            return false
        }
        _username.postValue(null)
        return true
    }
}