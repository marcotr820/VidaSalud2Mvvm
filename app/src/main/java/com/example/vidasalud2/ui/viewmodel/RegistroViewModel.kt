package com.example.vidasalud2.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.AuthRepository
import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.data.DataStore.DataStoreRepositoryManager
import com.example.vidasalud2.data.model.RegistroModel
import com.example.vidasalud2.data.model.ValidateResultField
import com.example.vidasalud2.domain.UseCases.FieldValidation.email.ValidateEmailUseCase
import com.example.vidasalud2.domain.UseCases.FieldValidation.password.ValidatePasswordUseCase
import com.example.vidasalud2.domain.UseCases.FieldValidation.password.ValidateRepeatPasswordUseCase
import com.example.vidasalud2.domain.UseCases.FieldValidation.username.ValidateUserNameUseCase
import com.example.vidasalud2.usuarios.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val authRepository: AuthRepository,
    private val dataStoreRepositoryManager: DataStoreRepositoryManager
): ViewModel() {

    private val _isloading = MutableLiveData<Boolean>(false)
    val isloading: LiveData<Boolean> get() = _isloading

    private val _usernameField = MutableLiveData<ValidateResultField>()
    val usernameField: LiveData<ValidateResultField> get() = _usernameField

    private val _emailField = MutableLiveData<ValidateResultField>()
    val emailField: LiveData<ValidateResultField> get() = _emailField

    private val _msgToast = MutableLiveData<String?>()
    val msgToast: LiveData<String?> get() = _msgToast

    private val _passwordField = MutableLiveData<ValidateResultField>()
    val passwordField: LiveData<ValidateResultField> get() = _passwordField

    private val _repeatPasswordField = MutableLiveData<ValidateResultField>()
    val repeatPasswordField: LiveData<ValidateResultField> get() = _repeatPasswordField

    private val _registroSuccess = MutableLiveData<Boolean>(false)
    val registroSuccess: LiveData<Boolean> get() = _registroSuccess

    fun registrarse(userName: String, email: String, password: String, repeatPassword: String){
        viewModelScope.launch {
            try {
                if ( validarCampos(userName, email, password, repeatPassword) ) {
                    _isloading.value = true
                    val isValidAsync = withContext(Dispatchers.IO) { validarCamposAsync(userName, email) }
                    if (isValidAsync){
                        val modelRegistro = RegistroModel(userName, email, password)
                        val registroResult = usuarioRepository.registrarUsuario(modelRegistro)
                        if (registroResult.isSuccessful){
                            //guardamos token devuelto despues del registro
                            dataStoreRepositoryManager.saveToken(DataStorePreferencesKeys.TOKEN,
                                registroResult.body()?.dataResult?.token!!)
                            //renovarToken()
                            authRepository.renovarToken()
                            //registro Exitoso
                            _registroSuccess.postValue(true)
                        } else {
                            //mensajde de error de badrequest
                            val jsonObjError = JSONObject(registroResult.errorBody()!!.charStream().readText())
                            _msgToast.postValue("${jsonObjError.getString("error")}")
                        }
                    }
                }
            } catch (e: Exception){
                _msgToast.postValue("Por favor, verifique su conexión a internet y vuelva a intentarlo")
            } finally {
                if (isloading.value == true) {
                    _isloading.postValue(false)
                }
            }
        }
    }

    private fun renovarToken() {
        viewModelScope.launch {
            try {
                val resultado = authRepository.renovarToken()
                if (resultado.isSuccessful) {
                    dataStoreRepositoryManager.setIsLoggedIn(true)
                    _registroSuccess.postValue(true)
                } else {
                    _msgToast.postValue("no se pudo renovar el token")
                }
            } catch (e: Exception) {
                _msgToast.postValue("error en la conexión")
            }
        }
    }

    private fun validarCampos(userName: String, email: String, password: String, repeatPassword: String): Boolean {
       val usernameResult = ValidateUserNameUseCase.validar(userName)
       _usernameField.value = usernameResult

       val emailResult = ValidateEmailUseCase.validar(email)
       _emailField.value = emailResult

       val passwordResult = ValidatePasswordUseCase.validar(password)
       _passwordField.value = passwordResult

       var repeatPasswordResult = ValidateResultField()
       if (passwordResult.isSuccess){
           repeatPasswordResult = ValidateRepeatPasswordUseCase.validar(password, repeatPassword)
           _repeatPasswordField.value = repeatPasswordResult
       }

       return (usernameResult.isSuccess && emailResult.isSuccess && passwordResult.isSuccess &&
               repeatPasswordResult.isSuccess)
   }

    private suspend fun validarCamposAsync(userName: String, email: String): Boolean = coroutineScope {
        val usernameResult  = async { usuarioRepository.userNameExiste(userName) }
        val emailResult = async { usuarioRepository.emailExiste(email) }

        val usernameExiste = usernameResult.await()
        if (usernameExiste){
            _usernameField.postValue(ValidateResultField(errorMessage = "username no disponible"))
        } else {
            _usernameField.postValue(ValidateResultField(isSuccess = true))
        }

        val emailExiste = emailResult.await()
        if (emailExiste){
            _emailField.postValue(ValidateResultField(errorMessage = "Verifica que tu email sea correcto"))
        } else {
            _emailField.postValue(ValidateResultField(isSuccess = true))
        }
        //ejecutar las validaciones en paralelo usando corutinas y la función async para
        // mejorar la eficiencia y el tiempo de respuesta.
        (!usernameExiste && !emailExiste)
    }

    fun msgToastNull(){
        _msgToast.value = null
    }

}