package com.example.vidasalud2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.data.model.ValidateResultField
import com.example.vidasalud2.domain.UseCases.FieldValidation.email.ValidateEmailUseCase
import com.example.vidasalud2.domain.UseCases.FieldValidation.username.ValidateUserNameUseCase
import com.example.vidasalud2.usuarios.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val validateUserNameUseCase: ValidateUserNameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase
): ViewModel() {

    private val _isloading = MutableLiveData<Boolean>(false)
    val isloading: LiveData<Boolean> get() = _isloading

//    private val _buttonIsValid = MutableLiveData<Boolean>(false)
//    val buttonIsValid: LiveData<Boolean> get() = _buttonIsValid

    private val _usernameField = MutableLiveData<ValidateResultField>()
    val usernameField: LiveData<ValidateResultField> get() = _usernameField

    private val _emailField = MutableLiveData<ValidateResultField>()
    val emailField: LiveData<ValidateResultField> get() = _emailField

    private val _catchError = MutableLiveData<String?>()
    val catchError: LiveData<String?> get() = _catchError

    fun registrarse(){

        val usernameValue = _usernameField.value?.isSuccess ?: false
        val emailValue = _emailField.value?.isSuccess ?: false
        //_buttonIsValid.value = usernameValue && emailValue
        if (!usernameValue || !emailValue) {
            _catchError.postValue("Error en la peticion")
            return
        }

        _isloading.value = true
        viewModelScope.launch {
        }
    }

    fun validarUserName(username: String) {
        try {
            val usernameResult = validateUserNameUseCase.validar(username)
            _usernameField.postValue(usernameResult)
        } catch (e: Exception) {
            _catchError.postValue("Algo a salido mal")
        }
    }

    fun validarEmail(email: String) {
        try {
            val emailResult = validateEmailUseCase.validar(email)
            _emailField.postValue(emailResult)
        }catch (e: Exception) {
            _catchError.postValue("Algo a salido mal")
        }
    }

    fun catchErrorNull(){
        _catchError.value = null
    }

//    fun buttonHabilitado(){
//        val usernameValue = _usernameField.value?.isSuccess ?: false
//        val emailValue = _emailField.value?.isSuccess ?: false
//        _buttonIsValid.value = usernameValue && emailValue
//    }

}