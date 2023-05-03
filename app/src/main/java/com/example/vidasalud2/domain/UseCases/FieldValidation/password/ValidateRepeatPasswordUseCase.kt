package com.example.vidasalud2.domain.UseCases.FieldValidation.password

import com.example.vidasalud2.data.model.ValidateResultField

object ValidateRepeatPasswordUseCase {

    fun validar(password: String, repeatPassword: String): ValidateResultField {

        if (password != repeatPassword){
            return ValidateResultField(
                isSuccess = false,
                errorMessage = "Los password no coinciden"
            )
        }

        return ValidateResultField(
            isSuccess = true
        )

    }
}