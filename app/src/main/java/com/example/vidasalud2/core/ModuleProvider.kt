package com.example.vidasalud2.core

import android.content.Context
import com.example.vidasalud2.data.DataStore.DataStoreRepositoryImplement
import com.example.vidasalud2.data.DataStore.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleProvider {

    @Singleton  //maneja una unica instancia para varias llamadas
    @Provides
    //nombre identificativo para usar la instancia que se requiera
    @Named("retrofitWithHeaders")
    fun provideRetrofitConHeader( okHttpClient: OkHttpClient ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.4:7000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client( okHttpClient )    //agregamos configuracion de los headers
            .build()
    }

    @Singleton  //maneja una unica instancia para varias llamadas
    @Provides
    //nombre identificativo para usar la instancia que se requiera
    @Named("retrofitWithoutHeaders")
    fun provideRetrofitSinHeader(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.4:7000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideInterceptor(dataStoreRepositoryImplement: DataStoreRepositoryImplement): HeaderInterceptor{
        return HeaderInterceptor(dataStoreRepositoryImplement)
    }

    @Singleton
    @Provides
    //implementacion para poder user el interceptor sin problemas de dependencias
    fun provideDataImplement(@ApplicationContext context: Context) : DataStoreRepositoryImplement {
        return DataStoreRepositoryImplement(context)
    }

//    @Singleton
//    @Provides
//    //nombre identificativo para usar la instancia que se requiera
//    @Named("loginApiRoutesWithHeaders")
//    fun provideLoginApiRoutesWithHeaders(@Named("retrofitWithHeaders") retrofit: Retrofit) : LoginApiRoutes {
//        return retrofit.create(LoginApiRoutes::class.java)
//    }
//
//    @Singleton
//    @Provides
//    //nombre identificativo para usar la instancia que se requiera
//    @Named("loginApiRoutesWithoutHeaders")
//    fun provideLoginApiRoutesWithoutHeaders(@Named("retrofitWithoutHeaders") retrofit: Retrofit) : LoginApiRoutes {
//        return retrofit.create(LoginApiRoutes::class.java)
//    }

    //dataStore
    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): DataStoreRepository = DataStoreRepositoryImplement(app)
}