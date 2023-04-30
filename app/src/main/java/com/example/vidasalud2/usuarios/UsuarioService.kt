package com.example.vidasalud2.usuarios

import com.example.vidasalud2.data.model.ResponseHttp
import com.example.vidasalud2.data.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class UsuarioService @Inject constructor(
    @Named("retrofitConHeader") private val retrofitConHeader: Retrofit
)
{
    suspend fun getUsuarios(): Response<ResponseHttp<List<Usuario>>> {
        return withContext(Dispatchers.IO) {
            val apiroutes = retrofitConHeader.create(UsuarioApiRoutes::class.java)
            apiroutes.getUsuarios()
        }
    }
}