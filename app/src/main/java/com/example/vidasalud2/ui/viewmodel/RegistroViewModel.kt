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

    private val _isloadingLiveData = MutableLiveData<Boolean>(false)
    val isloadingLiveData: LiveData<Boolean> get() = _isloadingLiveData

    private val _userNameLiveData = MutableLiveData<ValidateResultField>()
    val userNameLiveData: LiveData<ValidateResultField> get() = _userNameLiveData

    private val _emailLiveData = MutableLiveData<ValidateResultField>()
    val emailLiveData: LiveData<ValidateResultField> get() = _emailLiveData

    private val _msgToastLiveData = MutableLiveData<String?>()
    val msgToastLiveData: LiveData<String?> get() = _msgToastLiveData

    private val _passwordLiveData = MutableLiveData<ValidateResultField>()
    val passwordLiveData: LiveData<ValidateResultField> get() = _passwordLiveData

    private val _repeatPasswordLiveData = MutableLiveData<ValidateResultField>()
    val repeatPasswordLiveData: LiveData<ValidateResultField> get() = _repeatPasswordLiveData

    private val _registroSuccessLiveData = MutableLiveData<Boolean>(false)
    val registroSuccessLiveData: LiveData<Boolean> get() = _registroSuccessLiveData

    fun registrarse(userName: String, email: String, password: String, repeatPassword: String){

        if ( !validarCampos(userName, email, password, repeatPassword) ){ return }

        _isloadingLiveData.value = true
        viewModelScope.launch {
            try {
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
                        dataStoreRepositoryManager.setIsLoggedIn(true)
                        _registroSuccessLiveData.postValue(true)
                    } else {
                        //mensajde de error de badrequest
                        val jsonObjError = JSONObject(registroResult.errorBody()!!.charStream().readText())
                        _msgToastLiveData.postValue( jsonObjError.getString("error") )
                    }
                }
            } catch (e: Exception){
                _msgToastLiveData.postValue("Por favor, verifique su conexión a internet y vuelva a intentarlo")
            } finally {
                if (isloadingLiveData.value == true) {
                    _isloadingLiveData.postValue(false)
                }
            }
        }
    }

    private fun validarCampos(userName: String, email: String, password: String, repeatPassword: String): Boolean {
       val usernameResult = ValidateUserNameUseCase.validar(userName)
       _userNameLiveData.value = usernameResult

       val emailResult = ValidateEmailUseCase.validar(email)
       _emailLiveData.value = emailResult

       val passwordResult = ValidatePasswordUseCase.validar(password)
       _passwordLiveData.value = passwordResult

       var repeatPasswordResult = ValidateResultField()
       if (passwordResult.isSuccess){
           repeatPasswordResult = ValidateRepeatPasswordUseCase.validar(password, repeatPassword)
           _repeatPasswordLiveData.value = repeatPasswordResult
       }

       return (usernameResult.isSuccess && emailResult.isSuccess && passwordResult.isSuccess &&
               repeatPasswordResult.isSuccess)
   }

    private suspend fun validarCamposAsync(userName: String, email: String): Boolean = coroutineScope {
        val usernameResult  = async { usuarioRepository.userNameExiste(userName) }
        val emailResult = async { usuarioRepository.emailExiste(email) }

        val usernameExiste = usernameResult.await()
        if (usernameExiste){
            _userNameLiveData.postValue(ValidateResultField(errorMessage = "username no disponible"))
        } else {
            _userNameLiveData.postValue(ValidateResultField(isSuccess = true))
        }

        val emailExiste = emailResult.await()
        if (emailExiste){
            _emailLiveData.postValue(ValidateResultField(errorMessage = "Verifica que tu email sea correcto"))
        } else {
            _emailLiveData.postValue(ValidateResultField(isSuccess = true))
        }
        //ejecutar las validaciones en paralelo usando corutinas y la función async para
        // mejorar la eficiencia y el tiempo de respuesta.
        (!usernameExiste && !emailExiste)
    }

    fun msgToastNull(){
        _msgToastLiveData.value = null
    }

}