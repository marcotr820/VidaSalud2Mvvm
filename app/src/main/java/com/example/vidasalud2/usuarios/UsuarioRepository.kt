package com.example.vidasalud2.usuarios

import com.example.vidasalud2.data.model.DataResult
import com.example.vidasalud2.data.model.RegistroModel
import com.example.vidasalud2.data.model.ResponseHttp
import com.example.vidasalud2.data.model.Usuario
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

    suspend fun userNameExiste(username: String): Boolean {
        return usuarioService.userNameExiste(username)
    }

    suspend fun emailExiste(email: String): Boolean {
        return usuarioService.emailExiste(email)
    }
}