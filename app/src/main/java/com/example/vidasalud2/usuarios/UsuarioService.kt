package com.example.vidasalud2.usuarios

import android.util.Log
import com.example.vidasalud2.data.model.DataResult
import com.example.vidasalud2.data.model.RegistroModel
import com.example.vidasalud2.data.model.ResponseHttp
import com.example.vidasalud2.data.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class UsuarioService @Inject constructor(
    @Named("retrofitConHeader") private val retrofitConHeader: Retrofit,
    @Named("retrofitSinHeader") private val retrofitSinHeader: Retrofit
)
{
    suspend fun getUsuarios(): Response<ResponseHttp<List<Usuario>>> {
        return withContext(Dispatchers.IO) {
            val apiroutes = retrofitConHeader.create(UsuarioApiRoutes::class.java)
            apiroutes.getUsuarios()
        }
    }

    suspend fun registrarUsuario(registroModel: RegistroModel): Response<ResponseHttp<DataResult>> {
        Log.d("gg", "llego")
        return withContext(Dispatchers.IO) {
            val apiroutes = retrofitSinHeader.create(UsuarioApiRoutes::class.java)
            apiroutes.registrarUsuario(registroModel)
        }
    }

    suspend fun userNameExiste(username: String): Boolean {
        return withContext(Dispatchers.IO) {
            val apiroutes = retrofitSinHeader.create(UsuarioApiRoutes::class.java)
            apiroutes.userNameExiste(username)
        }
    }

    suspend fun emailExiste(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            val apiroutes = retrofitSinHeader.create(UsuarioApiRoutes::class.java)
            apiroutes.emailExiste(email)
        }
    }
}