package com.vaction.vaction_service.application.dto.response

data class PedidoResponse(
    val id: Int,
    val dataInicio: String,
    val dataFim: String,
    val dataSolicitacao: String,
    val diasUsufruidos: Int?,
    val ultimaAtualizacao: String?,
    val statusNome: String,
    val usuarioNome: String
)
