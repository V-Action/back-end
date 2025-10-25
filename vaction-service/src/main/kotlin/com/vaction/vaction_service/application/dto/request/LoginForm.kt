package com.vaction.vaction_service.application.dto.request


data class LoginForm(
    val email: String?,
    val cpf: String?,
    val senha: String
)