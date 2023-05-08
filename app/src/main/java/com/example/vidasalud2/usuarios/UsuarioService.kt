package com.example.vidasalud2.usuarios

import com.example.vidasalud2.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
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
        return withContext(Dispatchers.IO) {
            val apiroutes = retrofitSinHeader.create(UsuarioApiRoutes::class.java)
            apiroutes.registrarUsuario(registroModel)
        }
    }

    suspend fun actualizarPassword(actualizarPassword: ActualizarPassword): Response<ResponseHttp<Boolean>>{
        return withContext(Dispatchers.IO){
            val apiroute = retrofitConHeader.create(UsuarioApiRoutes::class.java)
            apiroute.actualizarPassword(actualizarPassword)
        }
    }

    suspend fun actualizarUserName(actualizarUserName: ActualizarUserName): Response<ResponseHttp<Boolean>>{
        return withContext(Dispatchers.IO){
            val apiroute = retrofitConHeader.create(UsuarioApiRoutes::class.java)
            apiroute.actualizarUserName(actualizarUserName)
        }
    }

    suspend fun actualizarRolUsuario(usuario: Usuario): Response<ResponseHttp<Boolean>>{
        return withContext(Dispatchers.IO){
            val apiroute = retrofitConHeader.create(UsuarioApiRoutes::class.java)
            apiroute.actualizarRolUsuario(usuario)
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

    suspend fun bloquearDesbloquearUsuario(id: String): Response<ResponseHttp<Boolean>>{
        return withContext(Dispatchers.IO) {
            val apiRoutes = retrofitConHeader.create(UsuarioApiRoutes::class.java)
            apiRoutes.bloquearDesbloquearUsuario(id)
        }
    }
}