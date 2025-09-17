package com.vaction.vaction_service.domain.model.enums

enum class StatusNome(val descricao: String) {
    PENDENTE_GESTOR("Solicitação está pendente com gestor."),
    PENDENTE_RH("Solicitação está pendente com RH"),
    APROVADO("Pedido aprovado"),
    REPROVADO("Pedido Reprovado");

    override fun toString(): String {
        return descricao
    }
}