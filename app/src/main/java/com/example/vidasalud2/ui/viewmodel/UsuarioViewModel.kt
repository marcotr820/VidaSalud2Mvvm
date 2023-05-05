package com.example.vidasalud2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud2.data.model.ResponseHttp
import com.example.vidasalud2.data.model.Usuario
import com.example.vidasalud2.usuarios.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _isloadingLiveData = MutableLiveData<Boolean>(false)
    val isloadingLiveData: LiveData<Boolean> get() = _isloadingLiveData

    private val _usuarios = MutableLiveData<ResponseHttp<List<Usuario>>>()
    val usuarios: LiveData<ResponseHttp<List<Usuario>>> get() = _usuarios

    private val _msgToast = MutableLiveData<String?>()
    val msgToast: LiveData<String?> get() = _msgToast

    fun getUsuarios() {
        _isloadingLiveData.value = true
        viewModelScope.launch {
            //delay(1000)
            try {
                val result = usuarioRepository.getUsuarios()
                if (result.isSuccessful) {
                    _usuarios.postValue(result.body())
                } else {
                    _msgToast.postValue("Error al realizar la petición")
                }
            } catch (e: Exception) {
                _msgToast.postValue("Error en el servidor")
            }finally {
                _isloadingLiveData.value = false
            }
        }
    }
}