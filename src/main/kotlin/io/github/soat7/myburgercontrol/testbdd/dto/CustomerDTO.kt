package io.github.soat7.myburgercontrol.testbdd.dto

import java.util.UUID

data class CustomerDTO(
    val id: UUID? = null,
    val cpf: String,
    val name: String,
    val email: String,
)
