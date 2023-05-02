package com.example.vidasalud2.domain.UseCases.FieldValidation.email

import com.example.vidasalud2.data.model.ValidateResultField
import com.example.vidasalud2.usuarios.UsuarioRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    private val campo: String = "email"
    private val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

    fun validar(email: String): ValidateResultField {

        if (email.isBlank()) {
            return ValidateResultField(
                isSuccess = false,
                errorMessage = "El $campo es requerido."
            )
        }

        if (!emailPattern.matches(email)) {
            return ValidateResultField(
                isSuccess = false,
                errorMessage = "El $campo no tiene un formato valido."
            )
        }

        val usernameExiste = runBlocking {
            usuarioRepository.emailExiste(email).body() ?: false
        }
        if (usernameExiste){
            return ValidateResultField(
                isSuccess = false,
                errorMessage = "El $campo no esta disponible"
            )
        }

        return ValidateResultField(
            isSuccess = true
        )
    }
}