package com.example.vidasalud2

import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.data.model.ResponseHttp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

//para poder usar directamente el apiLoginClient ya con RETROFIT debemos crear un proveedor
//que es provideLoginApiClient
class LoginService @Inject constructor(private val apiLoginRoutes: LoginApiRoutes) {

    suspend fun loginService(loginModel: LoginModel): Response<ResponseHttp>
    {
        return withContext(Dispatchers.IO) {
            val response = apiLoginRoutes.loginApiRoute(loginModel)
            response
        }

        //return apiLoginRoutes.loginApiRoute(loginModel)
    }
}