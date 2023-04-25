package com.example.vidasalud2

import com.example.vidasalud2.data.model.DataResult
import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.data.model.ResponseHttp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

//para poder usar directamente el apiLoginClient ya con RETROFIT debemos crear un proveedor
//que es provideLoginApiClient
class AuthService @Inject constructor(
    @Named("retrofitConHeader") private val retrofitConHeader: Retrofit,
    @Named("retrofitSinHeader") private val retrofitSinHeader: Retrofit
) {

    suspend fun loginService(loginModel: LoginModel): Response<ResponseHttp<DataResult>>
    {
        return withContext(Dispatchers.IO) {
            val apiroutes = retrofitSinHeader.create(AuthApiRoutes::class.java)
            apiroutes.loginApiRoute(loginModel)
        }
    }

    suspend fun renovarToken(): Response<ResponseHttp<DataResult>>
    {
        return withContext(Dispatchers.IO) {
            retrofitConHeader.create(AuthApiRoutes::class.java).renovarToken()
        }
    }

}