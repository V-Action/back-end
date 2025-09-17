package com.vaction.vaction_service.domain.service.impl

import com.vaction.vaction_service.domain.model.entity.Historico
import com.vaction.vaction_service.domain.repository.HistoricoRepository
import com.vaction.vaction_service.domain.service.HistoricoService
import com.vaction.vaction_service.domain.service.UsuarioService
import org.springframework.stereotype.Service

@Service
class HistoricoServiceImpl(
    private val historicoRepository: HistoricoRepository,
    private val usuarioService: UsuarioService
): HistoricoService {
    override fun salvaHistorico(historico: Historico): Historico {
        val historicoSalvo = historicoRepository.save(historico)
        val usuario = usuarioService.buscaPorId(historicoSalvo.usuario?.id!!)
        return historicoSalvo.copy(usuario = usuario)

    }

    override fun buscaHistorico(): List<Historico> {
        return historicoRepository.findAll()
    }
}