package com.example.vidasalud2.data.repository

import com.example.vidasalud2.data.model.Rol
import com.example.vidasalud2.data.model.providers.RolProvider
import com.example.vidasalud2.data.network.rol.RolService
import retrofit2.Response
import javax.inject.Inject

class RolesRepository @Inject constructor(
    private val rolService: RolService
) {

    suspend fun getRoles(): Response<List<Rol>> {
        val response = rolService.getRoles()
        RolProvider.roles = response.body() ?: emptyList()
        return response
    }
}