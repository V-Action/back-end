package com.vaction.vaction_service.domain.service
import com.vaction.vaction_service.application.dto.response.NotificacaoResponse
import com.vaction.vaction_service.domain.model.entity.Historico

interface HistoricoService {
    fun salvaHistorico(historico: Historico): Historico

    fun buscaHistorico(): List<Historico>

    fun buscarUltimosPorUsuario(idUsuario: Int): List<Historico>

}