package com.example.vidasalud2.roles

import com.example.vidasalud2.data.model.Rol
import retrofit2.Response
import javax.inject.Inject

class RolesRepository @Inject constructor(
    private val rolService: RolService
) {

    suspend fun getRolesDropdown(): Response<List<Rol>> {
        return rolService.getRolesDropdown()
    }
}