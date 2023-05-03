package com.example.vidasalud2.domain.UseCases.FieldValidation.username

import com.example.vidasalud2.data.model.ValidateResultField

object ValidateUserNameUseCase {

    private val campo: String = "user name"

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

        return ValidateResultField(
            isSuccess = true
        )
    }
}