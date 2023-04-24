package com.example.vidasalud2

import com.example.vidasalud2.data.model.DataResult
import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.data.model.ResponseHttp
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiLoginService: LoginService) {

    suspend fun loginRepository(loginModel: LoginModel): Response<ResponseHttp<DataResult>> {
        return apiLoginService.loginService(loginModel)
    }
}