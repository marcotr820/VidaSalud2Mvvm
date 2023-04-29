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
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _isloading = MutableLiveData<Boolean>(false)
    val isloading: LiveData<Boolean> get() = _isloading

    private val _usuarios = MutableLiveData<ResponseHttp<List<Usuario>>>()
    val usuarios: LiveData<ResponseHttp<List<Usuario>>> get() = _usuarios

    fun getUsuarios() {
        _isloading.postValue(true)
        viewModelScope.launch {
            //delay(1000)
            try {
                val result = repository.getUsuarios()
                if (result.isSuccessful) {
                    Log.d("DATA", "${result.body()}")
                    _usuarios.postValue(result.body())
                } else {
                    val respuestaError = ResponseHttp<List<Usuario>>(error = "Error al cargar los datos")
                    _usuarios.postValue(respuestaError)
                }
            } catch (e: Exception) {
                val respuestaError = ResponseHttp<List<Usuario>>(error = "Error en el Servidor")
                _usuarios.postValue(respuestaError)
            }finally {
                _isloading.postValue(false)
            }
        }
    }
}