package com.example.vidasalud2

import com.example.vidasalud2.model.LoginModel
import com.example.vidasalud2.model.ResponseHttp
import retrofit2.Call
import javax.inject.Inject

//para poder usar directamente el apiLoginClient ya con retrofit debemos crear un proveedor
//que es provideLoginApiClient
class LoginService @Inject constructor(private val apiLoginRoutes: LoginApiRoutes) {

    fun loginService(loginModel: LoginModel): Call<ResponseHttp> {
        return apiLoginRoutes.loginApiRoute(loginModel)
    }
}