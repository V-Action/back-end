package com.vaction.vaction_service.domain.model.entity

import com.vaction.vaction_service.domain.model.enums.DecisaoNome
import jakarta.persistence.*

@Entity
data class Decisao(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Column(name = "id_decisao")
    val id: Int,

    @Enumerated(EnumType.STRING)
    val nome: DecisaoNome?
)