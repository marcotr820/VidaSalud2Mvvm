package com.example.vidasalud2

import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.data.DataStore.DataStoreRepositoryManager
import com.example.vidasalud2.data.model.DataResult
import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.data.model.ResponseHttp
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiAuthService: AuthService,
    private val dataStoreRepositoryManager: DataStoreRepositoryManager
) {

    suspend fun loginRepository(loginModel: LoginModel): Response<ResponseHttp<DataResult>> {
        return apiAuthService.loginService(loginModel)
    }

    suspend fun renovarToken(): Response<ResponseHttp<DataResult>> {
        val resultado = apiAuthService.renovarToken()
        if (resultado.isSuccessful){
            //guardamos los datos devueltos en la petición
            dataStoreRepositoryManager.saveToken(
                DataStorePreferencesKeys.TOKEN, resultado.body()?.dataResult?.token.orEmpty())

            dataStoreRepositoryManager.saveUser(
                DataStorePreferencesKeys.USER, resultado.body()?.dataResult?.usuario!!)
        }
        return resultado
    }
}