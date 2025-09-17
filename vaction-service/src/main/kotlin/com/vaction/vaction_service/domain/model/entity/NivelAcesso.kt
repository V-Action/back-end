package com.vaction.vaction_service.domain.model.entity

import com.vaction.vaction_service.domain.model.enums.NivelAcessoNome
import jakarta.persistence.*

@Entity
data class NivelAcesso(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Column(name = "id_nivel")
    val id: Int,

    @Enumerated(EnumType.STRING)
    @field:Column(name = "descricao")
    val nome: NivelAcessoNome?
)