package com.example.login.models

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id") var id: Int?,
    @SerializedName("nombre") var nombre: String?,
    @SerializedName("correo") var correo: String?,
    @SerializedName("clave") var clave: String?
)