package com.example.vidasalud2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.data.DataStore.DataStoreRepository
import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

//creamos un proveedor del TokenRepository y devolvemos el TokenRepositoryImplement
//para poder inyectarlo
@HiltViewModel
class DataViewModel @Inject constructor(private val repository: DataStoreRepository) : ViewModel() {

    fun saveToken(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveToken(DataStorePreferencesKeys.TOKEN, value)
        }
    }

    fun getToken(): String? = runBlocking {
        repository.getToken(DataStorePreferencesKeys.TOKEN)
    }

    fun saveUser(usuario: Usuario) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUser(DataStorePreferencesKeys.USER, usuario)
        }
    }

    fun getUser(): Usuario? = runBlocking {
        repository.getUser(DataStorePreferencesKeys.USER)
    }

}