package com.example.vidasalud2

import com.example.vidasalud2.data.model.DataResult
import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.data.model.ResponseHttp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject
import javax.inject.Named

//para poder usar directamente el apiLoginClient ya con RETROFIT debemos crear un proveedor
//que es provideLoginApiClient
class LoginService @Inject constructor(
//    @Named("loginApiRoutesWithHeaders") private val apiLoginRoutesWithHeaders: LoginApiRoutes,
//    @Named("loginApiRoutesWithoutHeaders") private val apiLoginRoutesWithoutHeaders: LoginApiRoutes,
    @Named("retrofitWithHeaders") private val retrofitCon: Retrofit,
    @Named("retrofitWithoutHeaders") private val retrofit: Retrofit
) {

    suspend fun loginService(loginModel: LoginModel): Response<ResponseHttp<DataResult>>
    {
        return withContext(Dispatchers.IO) {
            val apiroutes = retrofit.create(LoginApiRoutes::class.java)
            apiroutes.loginApiRoute(loginModel)
//            val response = apiLoginRoutesWithoutHeaders.loginApiRoute(loginModel)
//            response
        }
    }

    suspend fun renovarToken(): Response<ResponseHttp<DataResult>>
    {
        return withContext(Dispatchers.IO) {
//            apiLoginRoutesWithHeaders.renovarToken()
            retrofitCon.create(LoginApiRoutes::class.java).renovarToken()
        }
    }
}