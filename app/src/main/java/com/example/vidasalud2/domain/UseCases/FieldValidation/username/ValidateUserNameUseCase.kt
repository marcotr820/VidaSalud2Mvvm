package com.example.vidasalud2.domain.UseCases.FieldValidation.username

import com.example.vidasalud2.data.model.ValidateResultField
import com.example.vidasalud2.usuarios.UsuarioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ValidateUserNameUseCase @Inject constructor(private val usuarioRepository: UsuarioRepository) {


    private val campo: String = "Usuario"

    fun validar(userName: String): ValidateResultField {

        if (userName.isBlank()) {
            return ValidateResultField(
                isSuccess = false,
                errorMessage = "El $campo es requerido."
            )
        }

        if (userName.length < 5) {
            return ValidateResultField(
                isSuccess = false,
                errorMessage = "El $campo debe tener al menos 5 caracteres"
            )
        }

        val usernameExiste = runBlocking {
            usuarioRepository.userNameExiste(userName).body() ?: false
        }
        if (usernameExiste){
            return ValidateResultField(
                isSuccess = false,
                errorMessage = "El username no esta disponible"
            )
        }

        return ValidateResultField(
            isSuccess = true
        )
    }
}