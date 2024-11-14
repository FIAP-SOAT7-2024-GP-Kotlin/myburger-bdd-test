package io.github.soat7.myburgercontrol.testbdd.dto

data class UserCreationDTO(
    val cpf: String,
    val password: String,
    val role: UserRole,
)
