package com.example.vidasalud2.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.data.DataStore.DataStoreRepositoryManager
import com.example.vidasalud2.data.model.ActualizarUserName
import com.example.vidasalud2.data.model.ValidateResultField
import com.example.vidasalud2.domain.UseCases.FieldValidation.username.ValidateUserNameUseCase
import com.example.vidasalud2.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ActualizarUserNameViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val dataStoreRepositoryManager: DataStoreRepositoryManager
) : ViewModel() {

    private val _isloadingLiveData = MutableLiveData<Boolean>(false)
    val isloadingLiveData: LiveData<Boolean> get() = _isloadingLiveData

    private val _userNameLiveData = MutableLiveData<ValidateResultField>()
    val userNameLiveData: LiveData<ValidateResultField> get() = _userNameLiveData

    private val _msgToastLiveData = MutableLiveData<String?>()
    val msgToastLiveData: LiveData<String?> get() = _msgToastLiveData

    private val _actualizarUserNameIsSuccessLiveData = MutableLiveData<Boolean>(false)
    val actualizarUserNameIsSuccessLiveData: LiveData<Boolean> get() = _actualizarUserNameIsSuccessLiveData

    fun actualizarUserName(userName: String) {
        if ( !validarCampo(userName) ){ return }
        _isloadingLiveData.value = true
        viewModelScope.launch {
            try {
                val campoExisteAsync = withContext(Dispatchers.IO) { validarCampoAsync(userName) }
                if (!campoExisteAsync){
                    val idUser = dataStoreRepositoryManager.getUser(DataStorePreferencesKeys.USER)?.id.orEmpty()
                    val userNameData = ActualizarUserName(idUser, userName)
                    val actualizarUserName = usuarioRepository.actualizarUserName(userNameData)
                    if (actualizarUserName.isSuccessful){
                        dataStoreRepositoryManager.clearDataStore()
                        _actualizarUserNameIsSuccessLiveData.postValue(true)
                        _msgToastLiveData.postValue("Tu username se actualizo con exito, por favor vuelve a iniciar sesión")
                    }else{
                        val jsonObjError = JSONObject(actualizarUserName.errorBody()!!.charStream().readText())
                        _msgToastLiveData.postValue( jsonObjError.getString("error") )
                    }
                }
            }catch (e: Exception){
                _msgToastLiveData.postValue("Error request.")
            }finally {
                _isloadingLiveData.value = false
            }
        }
    }

    private fun validarCampo(userName: String): Boolean {
        val userNameResult = ValidateUserNameUseCase.validar(userName)
        _userNameLiveData.value = userNameResult

        return userNameResult.isSuccess
    }

    private suspend fun validarCampoAsync(userName: String): Boolean {
        val userNameExiste = usuarioRepository.userNameExiste(userName)
        if (userNameExiste){
            _userNameLiveData.postValue(ValidateResultField(errorMessage = "El username no esta disponible"))
        } else {
            _userNameLiveData.postValue(ValidateResultField(isSuccess = true))
        }
        return userNameExiste
    }
}