package com.example.vidasalud2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.data.DataStore.DataStoreRepositoryManager
import com.example.vidasalud2.data.model.ActualizarPassword
import com.example.vidasalud2.data.model.ValidateResultField
import com.example.vidasalud2.domain.UseCases.FieldValidation.password.ValidatePasswordUseCase
import com.example.vidasalud2.domain.UseCases.FieldValidation.password.ValidateRepeatPasswordUseCase
import com.example.vidasalud2.usuarios.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ActualizarPasswordViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val dataStoreRepositoryManager: DataStoreRepositoryManager
): ViewModel() {

    private val _isloadingLiveData = MutableLiveData<Boolean>(false)
    val isloadingLiveData: LiveData<Boolean> get() = _isloadingLiveData

    private val _actualPasswordLiveData = MutableLiveData<ValidateResultField>()
    val actualPasswordLiveData: LiveData<ValidateResultField> get() = _actualPasswordLiveData

    private val _nuevoPasswordLiveData = MutableLiveData<ValidateResultField>()
    val nuevoPasswordLiveData: LiveData<ValidateResultField> get() = _nuevoPasswordLiveData

    private val _confirmarPasswordLiveData = MutableLiveData<ValidateResultField>()
    val confirmarPasswordLiveData: LiveData<ValidateResultField> get() = _confirmarPasswordLiveData

    private val _actualizarPasswordIsSuccessLiveData = MutableLiveData<Boolean>(false)
    val actualizarPasswordIsSuccessLiveData: LiveData<Boolean> get() = _actualizarPasswordIsSuccessLiveData

    private val _msgToastLiveData = MutableLiveData<String?>()
    val msgToastLiveData: LiveData<String?> get() = _msgToastLiveData

    fun actualizarPassword(actualPassword: String, nuevoPassword: String, confirmarPassword: String) {
        if (!validarCampos(actualPassword, nuevoPassword, confirmarPassword)){ return }
        _isloadingLiveData.value = true
        viewModelScope.launch {
            delay(300)
            try {
                val idUser = dataStoreRepositoryManager.getUser(DataStorePreferencesKeys.USER)?.id.orEmpty()
                val actualizarPasswordModel = ActualizarPassword(idUser, actualPassword, nuevoPassword, confirmarPassword)
                val actualizarPasswordResult = usuarioRepository.actualizarPassword(actualizarPasswordModel)
                if (actualizarPasswordResult.isSuccessful){
                    _msgToastLiveData.postValue("Tu password se actualizo con exito")
                    _actualizarPasswordIsSuccessLiveData.postValue(true)
                } else {
                    val jsonObjError = JSONObject(actualizarPasswordResult.errorBody()!!.charStream().readText())
                    _msgToastLiveData.postValue( jsonObjError.getString("error") )
                }
            }catch (e: Exception){
                _msgToastLiveData.postValue("Error al realizar la petición")
            }finally {
                _isloadingLiveData.postValue(false)
            }
        }
    }

    private fun validarCampos(antiguoPassword: String, nuevoPassword: String, confirmarPassword: String): Boolean {
        val antiguoPasswordResult = ValidatePasswordUseCase.validar(antiguoPassword)
        _actualPasswordLiveData.value = antiguoPasswordResult

        val nuevoPasswordResult = ValidatePasswordUseCase.validar(nuevoPassword)
        _nuevoPasswordLiveData.value = nuevoPasswordResult

        var repetirPasswordResult = ValidateResultField()
        if (nuevoPasswordResult.isSuccess){
            repetirPasswordResult = ValidateRepeatPasswordUseCase.validar(nuevoPassword, confirmarPassword)
            _confirmarPasswordLiveData.value = repetirPasswordResult
        }

        return (antiguoPasswordResult.isSuccess && nuevoPasswordResult.isSuccess &&
                repetirPasswordResult.isSuccess)
    }

    fun msgToastNull(){
        _msgToastLiveData.value = null
    }
}