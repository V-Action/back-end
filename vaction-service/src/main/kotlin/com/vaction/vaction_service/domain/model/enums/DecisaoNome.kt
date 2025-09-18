package com.vaction.vaction_service.domain.model.enums

import com.vaction.vaction_service.domain.model.entity.Status

enum class DecisaoNome(val descricao: String) {
    APROVADO("Pedido aprovado"),
    REPROVADO("Pedido Reprovado");

    override fun toString(): String {
        return descricao
    }

    fun toStatus(): Status {
        return when(this) {
            APROVADO -> Status(id = 3, StatusNome.APROVADO)
            REPROVADO -> Status(id = 4, StatusNome.REPROVADO)
        }
    }
}