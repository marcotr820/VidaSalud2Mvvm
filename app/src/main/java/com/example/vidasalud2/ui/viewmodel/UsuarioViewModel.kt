package com.example.vidasalud2.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.data.model.ResponseHttp
import com.example.vidasalud2.data.model.Usuario
import com.example.vidasalud2.data.repository.UsuarioRepository
import com.example.vidasalud2.utils.CheckInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val checkInternetConnection: CheckInternetConnection
) : ViewModel() {

    private val _isloadingLiveData = MutableLiveData<Boolean>(false)
    val isloadingLiveData: LiveData<Boolean> get() = _isloadingLiveData

    private val _usuarios = MutableLiveData<ResponseHttp<List<Usuario>>>()
    val usuarios: LiveData<ResponseHttp<List<Usuario>>> get() = _usuarios

    private val _msgToastLiveData = MutableLiveData<String?>()
    val msgToastLiveData: LiveData<String?> get() = _msgToastLiveData

    fun getUsuarios() {
        _isloadingLiveData.value = true
        viewModelScope.launch {
            //delay(500)
            if (!checkInternetConnection()){
                _isloadingLiveData.value = false
                _msgToastLiveData.value = "Error al cargar la lista"
                viewModelScope.cancel()
            }
            try {
                val result = usuarioRepository.getUsuarios()
                if (result.isSuccessful) {
                    _usuarios.postValue(result.body())
                } else {
                    _msgToastLiveData.postValue("Error al realizar la petición")
                }
            } catch (e: Exception) {
                _msgToastLiveData.postValue("No se pudo cargar la lista")
            }finally {
                _isloadingLiveData.value = false
            }
        }
    }
}