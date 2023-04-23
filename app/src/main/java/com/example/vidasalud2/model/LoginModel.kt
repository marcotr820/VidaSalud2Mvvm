package com.example.vidasalud2.model

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("userName") val userName: String,
    @SerializedName("password") val password: String
)