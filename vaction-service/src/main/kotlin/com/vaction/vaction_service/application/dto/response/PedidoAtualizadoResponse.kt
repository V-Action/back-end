package com.vaction.vaction_service.application.dto.response

import com.vaction.vaction_service.domain.model.entity.Decisao
import com.vaction.vaction_service.domain.model.entity.Usuario
import com.vaction.vaction_service.domain.model.enums.DecisaoNome
import com.vaction.vaction_service.domain.model.enums.StatusNome
import java.time.LocalDate

data class PedidoAtualizadoResponse(
    val dataAlteracao: LocalDate?,
    val observacao: String?,
    val usuario: Usuario?,
    val decisao: Decisao?,
    val statusPedido: StatusNome?
)