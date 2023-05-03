package com.example.vidasalud2.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.AuthRepository
import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.data.DataStore.DataStoreRepositoryManager
import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.data.model.ValidateResultField
import com.example.vidasalud2.domain.UseCases.FieldValidation.password.ValidatePasswordUseCase
import com.example.vidasalud2.domain.UseCases.FieldValidation.username.ValidateUserNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepositoryManager: DataStoreRepositoryManager
) : ViewModel() {

    private val _isloadingLiveData = MutableLiveData<Boolean>(false)
    val isloadingLiveData: LiveData<Boolean> get() = _isloadingLiveData

    private val _loginSuccessLiveData = MutableLiveData<Boolean>(false)
    val loginSuccessLiveData: LiveData<Boolean> get() = _loginSuccessLiveData

    private val _msgToastLiveData = MutableLiveData<String?>()
    val msgToastLiveData: LiveData<String?> get() = _msgToastLiveData

    private val _userNameLiveData = MutableLiveData<ValidateResultField>()
    val userNameLiveData: LiveData<ValidateResultField> get() = _userNameLiveData

    private val _passwordLiveData = MutableLiveData<ValidateResultField>()
    val passwordLiveData: LiveData<ValidateResultField> get() = _passwordLiveData

    fun login(loginModel: LoginModel) {

        if ( !validarCampos(loginModel) ) { return }

        _isloadingLiveData.postValue(true)
        viewModelScope.launch {
            try {
                val result = authRepository.loginRepository(loginModel)
                if (result.isSuccessful) {
                    //guardamos token devuelto despues del login
                    dataStoreRepositoryManager.saveToken(DataStorePreferencesKeys.TOKEN, result.body()?.dataResult?.token!!)
                    //renovarToken()
                    authRepository.renovarToken()
                    //login exitoso
                    dataStoreRepositoryManager.setIsLoggedIn(true)
                    _loginSuccessLiveData.postValue(true)
                } else {
                    val jsonObjError = JSONObject(result.errorBody()!!.charStream().readText())
                    _msgToastLiveData.postValue( jsonObjError.getString("error") )
                }
            }
            catch(e: Exception) {
                _msgToastLiveData.postValue(e.message)
            } finally {
                _isloadingLiveData.postValue(false)
            }
        }
    }

    private fun validarCampos(loginModel: LoginModel): Boolean {
        val usernameResult = ValidateUserNameUseCase.validar(loginModel.userName)
        _userNameLiveData.value = usernameResult

        val passwordResult = ValidatePasswordUseCase.validar(loginModel.password)
        _passwordLiveData.value = passwordResult

        return (usernameResult.isSuccess && passwordResult.isSuccess)
    }

    fun msgToastNull(){
        _msgToastLiveData.value = null
    }
}