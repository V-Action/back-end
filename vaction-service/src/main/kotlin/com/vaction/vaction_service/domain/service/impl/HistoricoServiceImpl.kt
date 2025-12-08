package com.vaction.vaction_service.domain.service.impl

import com.vaction.vaction_service.application.dto.response.NotificacaoResponse
import com.vaction.vaction_service.domain.model.entity.Historico
import com.vaction.vaction_service.domain.repository.HistoricoRepository
import com.vaction.vaction_service.domain.service.HistoricoService
import com.vaction.vaction_service.domain.service.UsuarioService
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter
@Service
class HistoricoServiceImpl(
    private val historicoRepository: HistoricoRepository,
    private val usuarioService: UsuarioService
): HistoricoService {
    override fun salvaHistorico(historico: Historico): Historico {
        return historicoRepository.save(historico)
    }

    override fun buscaHistorico(): List<Historico> {
        return historicoRepository.findAll()
    }

    override fun buscarUltimosPorUsuario(idUsuario: Int): List<Historico> {
        val historicos = historicoRepository.findTop5ByPedidoUsuarioIdOrderByDataAlteracaoDesc(idUsuario)
        return historicos.take(2)
    }


}