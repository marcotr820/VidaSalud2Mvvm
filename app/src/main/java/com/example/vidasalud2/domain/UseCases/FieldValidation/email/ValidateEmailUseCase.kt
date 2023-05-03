package com.example.vidasalud2.domain.UseCases.FieldValidation.email

import com.example.vidasalud2.data.model.ValidateResultField

object ValidateEmailUseCase {
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

        return ValidateResultField(
            isSuccess = true
        )
    }
}