package com.example.vidasalud2.core

import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.data.DataStore.DataStoreRepositoryImplement
import com.example.vidasalud2.ui.viewmodel.DataViewModel
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(private val dataStoreRepositoryImplement: DataStoreRepositoryImplement) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = runBlocking {
            dataStoreRepositoryImplement.getToken(DataStorePreferencesKeys.TOKEN)
        }

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }

}