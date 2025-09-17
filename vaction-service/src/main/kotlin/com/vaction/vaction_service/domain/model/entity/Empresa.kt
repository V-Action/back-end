package com.vaction.vaction_service.domain.model.entity

import com.vaction.vaction_service.domain.model.enums.NivelAcessoNome
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
data class Empresa(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Column(name = "id_empresa")
    val id: Int,

    @field:NotNull
    val cnpj: String?
)
