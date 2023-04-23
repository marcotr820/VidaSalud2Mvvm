package com.example.vidasalud2

import com.example.vidasalud2.model.LoginModel
import com.example.vidasalud2.model.ResponseHttp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiRoutes {

    @POST("cuentas/login")
    fun loginApiRoute(@Body loginModel: LoginModel): Call<ResponseHttp>

}