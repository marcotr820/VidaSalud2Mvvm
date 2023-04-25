package com.example.vidasalud2.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.data.DataStore.DataStoreRepositoryManager
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

// TODO: creamos una extensión para el almacén de datos de tipo DataStore<Preferences>
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStorePreferencesKeys.DATASTORE_PREFERENCES_NAME)

@Module
@InstallIn(SingletonComponent::class)
object ModuleProvider {

    @Singleton  //maneja una unica instancia para varias llamadas
    @Provides
    //nombre identificativo para usar la instancia que se requiera
    @Named("retrofitConHeader")
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
    @Named("retrofitSinHeader")
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
    fun provideInterceptor(dataStoreRepositoryManager: DataStoreRepositoryManager): HeaderInterceptor{
        return HeaderInterceptor(dataStoreRepositoryManager)
    }

    @Singleton
    @Provides
    //implementacion para poder usar en el HeaderInterceptor sin problemas de dependencias
    fun provideDataStoreManager(@ApplicationContext context: Context) : DataStoreRepositoryManager {
        return DataStoreRepositoryManager(context)
    }

//    @Singleton
//    @Provides
//    //funcion que retorna una instancia injectable de DataStoreRepositoryManager
//    //para el DataViewModel
//    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository {
//        return DataStoreRepositoryManager(context)
//    }
}