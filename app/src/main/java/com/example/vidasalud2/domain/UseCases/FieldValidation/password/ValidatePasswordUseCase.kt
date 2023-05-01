package com.example.vidasalud2.domain.UseCases.FieldValidation.password

import com.example.vidasalud2.data.model.ValidateResultField

object ValidatePasswordUseCase {

    private val campo: String = "Password"

    fun validar(password: String): ValidateResultField {
        if (password.isBlank()) {
            return ValidateResultField(
                isSuccess = false,
                errorMessage = "El $campo es requerido."
            )
        }

        if (password.length < 5) {
            return ValidateResultField(
                isSuccess = false,
                errorMessage = "El $campo debe tener al menos 5 caracteres."
            )
        }

        return ValidateResultField(
            isSuccess = true
        )
    }
}