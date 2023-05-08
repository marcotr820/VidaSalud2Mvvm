package com.example.vidasalud2.roles

import com.example.vidasalud2.data.model.Rol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject
import javax.inject.Named

class RolService @Inject constructor(
    @Named("retrofitConHeader") private val retrofitConHeader: Retrofit
) {

        suspend fun getRolesDropdown(): Response<List<Rol>>{
            return withContext(Dispatchers.IO){
                val apiroutes = retrofitConHeader.create(RolesApiRoutes::class.java)
                apiroutes.getRolesDropdown()
            }
        }

}