package com.example.vidasalud2

import com.example.vidasalud2.data.model.DataResult
import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.data.model.ResponseHttp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiRoutes {

    @POST("cuentas/login")
    suspend fun loginApiRoute(@Body loginModel: LoginModel): Response<ResponseHttp<DataResult>>

}