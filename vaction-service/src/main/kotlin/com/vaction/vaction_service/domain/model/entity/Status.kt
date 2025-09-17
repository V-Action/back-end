package com.vaction.vaction_service.domain.model.entity

import com.vaction.vaction_service.domain.model.enums.NivelAcessoNome
import com.vaction.vaction_service.domain.model.enums.StatusNome
import jakarta.persistence.*

@Entity
data class Status(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Column(name = "id_status")
    val id: Int,

    @Enumerated(EnumType.STRING)
    val nome: StatusNome?
)