package com.example.vidasalud2.data.DataStore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.vidasalud2.data.model.Usuario
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import javax.inject.Inject

// TODO: buscar la manera de extender la clase dataStore
private val Context.dataStore by preferencesDataStore(name = DataStorePreferencesKeys.DATASTORE_PREFERENCES_NAME)

class DataStoreRepositoryImplement @Inject constructor(private val context: Context) : DataStoreRepository {

    override suspend fun saveToken(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getToken(key: String): String? {
        return try {
            val preferencesKey = stringPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            preferences[preferencesKey]
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    override suspend fun saveUser(key: String, usuario: Usuario) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = Gson().toJson(usuario)
        }
    }

    override suspend fun getUser(key: String): Usuario? {
        return try {
            val preferencesKey = stringPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            val userJson = preferences[preferencesKey]
            Gson().fromJson(userJson, Usuario::class.java)
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
}