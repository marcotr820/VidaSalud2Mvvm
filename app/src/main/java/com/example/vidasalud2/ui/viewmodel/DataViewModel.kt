package com.example.vidasalud2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.core.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

//creamos un proveedor del TokenRepository y devolvemos el TokenRepositoryImplement
//para poder inyectarlo
@HiltViewModel
class DataViewModel @Inject constructor(private val repository: TokenRepository) : ViewModel() {

    fun saveToken(value: String) {
        viewModelScope.launch {
            repository.saveToken("TOKEN", value)
        }
    }

    fun getToken(): String? = runBlocking {
        repository.getToken("TOKEN")
    }

}