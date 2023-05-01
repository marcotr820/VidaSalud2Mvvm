package com.example.vidasalud2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.data.model.ValidateResultField
import com.example.vidasalud2.usuarios.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
): ViewModel() {

    private val _isloading = MutableLiveData<Boolean>(false)
    val isloading: LiveData<Boolean> get() = _isloading

    private val _usuarioField = MutableLiveData<ValidateResultField>()
    val usuarioField: LiveData<ValidateResultField> get() = _usuarioField

    private val _emailField = MutableLiveData<ValidateResultField>()
    val emailField: LiveData<ValidateResultField> get() = _emailField

    fun registrarse(username: String, email: String){
        _isloading.postValue(true)
        viewModelScope.launch {
            delay(700)
            val usuarioExiste = validarAsyncUserName(username)
            if (usuarioExiste) {
                _usuarioField.postValue(ValidateResultField(
                    isSuccess = false, errorMessage = "El username no esta disponible"))
                _isloading.postValue(false)
            }

            val emailExiste = validarAsyncEmail(email)
            if (emailExiste) {
                _emailField.postValue(ValidateResultField(
                    isSuccess = false, errorMessage = "El email no esta disponible"))
                _isloading.postValue(false)
            }
            _isloading.postValue(false)
        }
    }

    private suspend fun validarAsyncUserName(username: String): Boolean {
        return try {
            usuarioRepository.userNameExiste(username).body() ?: false
        } catch (e: Exception){
            _isloading.postValue(false)
            false
        }
    }

    private suspend fun validarAsyncEmail(email: String): Boolean {
        return try {
            usuarioRepository.emailExiste(email).body() ?: false
        } catch (e: Exception){
            _isloading.postValue(false)
            false
        }
    }
}