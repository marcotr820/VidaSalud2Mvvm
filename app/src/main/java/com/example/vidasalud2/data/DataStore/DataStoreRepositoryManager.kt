package com.example.vidasalud2.data.DataStore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.vidasalud2.core.dataStore
import com.example.vidasalud2.data.model.Usuario
import com.google.gson.Gson
import kotlinx.coroutines.flow.first

class DataStoreRepositoryManager(private val context: Context) : DataStoreRepository {

    override suspend fun setIsLoggedIn(loggedIn: Boolean) {
        val preferencesKey = booleanPreferencesKey(DataStorePreferencesKeys.LOGGEDIN)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = loggedIn
        }
    }

    override suspend fun getIsLoggedIn(): Boolean {
        val preferencesKey = booleanPreferencesKey(DataStorePreferencesKeys.LOGGEDIN)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey] ?: false
    }

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

    override suspend fun clearDataStore() {
        context.dataStore.edit {preferences ->
            preferences.clear()
        }
    }


}