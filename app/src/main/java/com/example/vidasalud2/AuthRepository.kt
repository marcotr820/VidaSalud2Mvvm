package com.example.vidasalud2

import com.example.vidasalud2.data.model.DataResult
import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.data.model.ResponseHttp
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiAuthService: AuthService) {

    suspend fun loginRepository(loginModel: LoginModel): Response<ResponseHttp<DataResult>> {
        return apiAuthService.loginService(loginModel)
    }

    suspend fun renovarToken(): Response<ResponseHttp<DataResult>> {
        return apiAuthService.renovarToken()
    }
}