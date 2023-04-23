package com.example.vidasalud2

import com.example.vidasalud2.model.LoginModel
import com.example.vidasalud2.model.ResponseHttp
import retrofit2.Call
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiLoginService: LoginService) {

    fun loginRepository(loginModel: LoginModel): Call<ResponseHttp> {
        return apiLoginService.loginService(loginModel)
    }
}