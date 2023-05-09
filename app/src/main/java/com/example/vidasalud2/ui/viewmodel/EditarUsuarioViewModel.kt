package com.example.vidasalud2.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.data.model.Rol
import com.example.vidasalud2.data.model.Usuario
import com.example.vidasalud2.data.model.ValidateResultField
import com.example.vidasalud2.roles.RolesRepository
import com.example.vidasalud2.usuarios.UsuarioRepository
import com.example.vidasalud2.utils.CheckInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarUsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val rolesRepository: RolesRepository,
    private val checkInternetConnection: CheckInternetConnection
): ViewModel() {

    private var _selectedRolId: String? = null

    private val _isloadingLiveData = MutableLiveData<Boolean>(false)
    val isloadingLiveData: LiveData<Boolean> get() = _isloadingLiveData

    private val _rolesDropdownLiveData = MutableLiveData<List<Rol>>()
    val rolesDropdownLiveData: LiveData<List<Rol>> get() = _rolesDropdownLiveData

    private val _msgToastLiveData = MutableLiveData<String?>()
    val msgToastLiveData: LiveData<String?> get() = _msgToastLiveData

    private val _rolLiveData = MutableLiveData<ValidateResultField>()
    val rolLiveData: LiveData<ValidateResultField> get() = _rolLiveData

    private val _eliminarUsuarioLiveData = MutableLiveData<Boolean>(false)
    val eliminarUsuarioLiveData: LiveData<Boolean> get() = _eliminarUsuarioLiveData

    fun getRolesDropdown(){
        viewModelScope.launch {
            delay(500)
            try {
                val getRoles = rolesRepository.getRolesDropdown()
                if (getRoles.isSuccessful){
                    _rolesDropdownLiveData.postValue(getRoles.body() ?: emptyList())
                } else {
                    _msgToastLiveData.postValue("Error al obtener los roles")
                }
            } catch (e: Exception){
                _msgToastLiveData.postValue("Error servidor")
            }
        }
    }

    fun actualizarRolUsuario(usuario: Usuario){
        val usuarioData = Usuario(
            usuario.id,
            usuario.userName,
            usuario.email,
            usuario.isBlocked,
            mutableListOf(_selectedRolId.orEmpty())
        )
        _isloadingLiveData.value = true
        viewModelScope.launch {
            delay(500)
            try {
                val actualizarRol = usuarioRepository.actualizarRolUsuario(usuarioData)
                if (actualizarRol.isSuccessful){
                    _msgToastLiveData.postValue("Actualizado con exito")
                }else {
                    _msgToastLiveData.postValue("fallo la peticion")
                }
            }catch (e: Exception){
                _msgToastLiveData.postValue("Error conexion")
            } finally {
                _isloadingLiveData.value = false
            }
        }
    }

    fun setSelectedRolId(rolId: String){
        _selectedRolId = rolId
    }

    fun cambiarEstadoBloqueo(usuarioId: String) {
        _isloadingLiveData.value = true
        viewModelScope.launch {
            delay(500)
            try {
                val resultado = usuarioRepository.bloquearDesbloquearUsuario(usuarioId)
                if (resultado.isSuccessful){
                    _msgToastLiveData.postValue("Actualizado con exito")
                } else {
                    _msgToastLiveData.postValue("fallo la peticion")
                }
            } catch (e: Exception){
                _msgToastLiveData.postValue("Error servidor")
            }finally {
                _isloadingLiveData.value = false
            }
        }
    }

    fun eliminarUsuario(usuarioId: String) {
        if (!checkInternetConnection()){
            _msgToastLiveData.value = "Sin internet"
            return
        }
        _isloadingLiveData.value = true
        viewModelScope.launch {
            delay(500)
            try {
                val resultado = usuarioRepository.eliminarUsuario(usuarioId)
                if (resultado.isSuccessful){
                    _eliminarUsuarioLiveData.postValue(true)
                    _msgToastLiveData.postValue("Usuario eliminado")
                } else {
                    _msgToastLiveData.postValue("fallo la petición")
                }
            } catch (e: Exception){
                _msgToastLiveData.postValue("Error servidor")
            }finally {
                _isloadingLiveData.value = false
            }
        }
    }

}