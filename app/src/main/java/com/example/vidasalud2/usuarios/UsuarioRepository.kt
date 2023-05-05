package com.example.vidasalud2.usuarios

import com.example.vidasalud2.data.model.*
import retrofit2.Response
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val usuarioService: UsuarioService
)
{
    suspend fun getUsuarios(): Response<ResponseHttp<List<Usuario>>> {
        return usuarioService.getUsuarios()
    }

    suspend fun registrarUsuario(registroModel: RegistroModel): Response<ResponseHttp<DataResult>> {
        return usuarioService.registrarUsuario(registroModel)
    }

    suspend fun actualizarPassword(actualizarPassword: ActualizarPassword): Response<ResponseHttp<Boolean>> {
        return usuarioService.actualizarPassword(actualizarPassword)
    }

    suspend fun actualizarUserName(actualizarUserName: ActualizarUserName): Response<ResponseHttp<Boolean>>{
        return usuarioService.actualizarUserName(actualizarUserName)
    }

    suspend fun userNameExiste(username: String): Boolean {
        return usuarioService.userNameExiste(username)
    }

    suspend fun emailExiste(email: String): Boolean {
        return usuarioService.emailExiste(email)
    }
}