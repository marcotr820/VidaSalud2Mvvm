package com.example.vidasalud2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.data.DataStore.DataStoreRepositoryManager
import com.example.vidasalud2.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

//creamos un proveedor del DataStoreRepositoryManager y devolvemos el DataStoreRepositoryManager
//para poder inyectarlo
@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val dataStoreRepositoryManager: DataStoreRepositoryManager
) : ViewModel() {

    fun setIsLoggedIn(loggedIn: Boolean) {
        viewModelScope.launch {
            dataStoreRepositoryManager.setIsLoggedIn(loggedIn)
        }
    }

    fun getIsLoggedIn(): Boolean = runBlocking {
        dataStoreRepositoryManager.getIsLoggedIn()
    }

    fun saveToken(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepositoryManager.saveToken(DataStorePreferencesKeys.TOKEN, value)
        }
    }

    fun getToken(): String? = runBlocking {
        dataStoreRepositoryManager.getToken(DataStorePreferencesKeys.TOKEN)
    }

    fun saveUser(usuario: Usuario) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepositoryManager.saveUser(DataStorePreferencesKeys.USER, usuario)
        }
    }

    fun getUser(): Usuario? = runBlocking {
        dataStoreRepositoryManager.getUser(DataStorePreferencesKeys.USER)
    }

    fun clearDataStore() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepositoryManager.clearDataStore()
        }
    }

}