package com.vaction.vaction_service.domain.service.impl

import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Status
import com.vaction.vaction_service.domain.model.enums.NivelAcessoNome
import com.vaction.vaction_service.domain.model.enums.StatusNome
import com.vaction.vaction_service.domain.repository.PedidoRepository
import com.vaction.vaction_service.domain.service.PedidoService
import com.vaction.vaction_service.domain.service.UsuarioService
import org.springframework.stereotype.Service

@Service
class PedidoServiceImpl(
    private val pedidoRepository: PedidoRepository,
    private val usuarioService: UsuarioService
): PedidoService {
    override fun solicitar(pedido: Pedido): Pedido {
        return pedidoRepository.save(pedido)
    }

    override fun buscaPedido(): List<Pedido> {
        return pedidoRepository.findAll()
    }

    override fun buscaPedidoPorId(id: Int): Pedido {
        return pedidoRepository.findById(id)
            .orElseThrow { Exception("Pedido não encontrado com id: $id") }
    }

    override fun atualizaStatusPedido(id: Int, status: Status): Int {
        val pedido = pedidoRepository.atualizaStatusPeloId(id, status)
        return pedido
    }

    override fun buscaPedidosAprovadosPorUsuario(id: Int): List<Pedido> {
        return pedidoRepository.buscaPedidosAprovadosPorUsuario(id, 4)
    }

    override fun buscaPedidoPorUsuarioOuStatus(id: Int, status: StatusNome?): List<Pedido> {
        return pedidoRepository.buscaPedidoPorUsuarioOuStatus(id, status)
    }

    override fun buscaPedidosAprovados(): List<Pedido> {
        return pedidoRepository.buscaPedidosPorStatus(StatusNome.APROVADO)
    }

    override fun buscaPedidosCalendarioPorUsuario(usuarioId: Int): List<Pedido> {
        // Busca o usuário para verificar o nível de acesso
        val usuario = usuarioService.buscaPorId(usuarioId)
        val nivelAcesso = usuario.nivelAcesso?.nome

        return when (nivelAcesso) {
            NivelAcessoNome.COLABORADOR -> {
                // COLABORADOR: só seus próprios pedidos
                pedidoRepository.buscaPedidosCalendarioColaborador(StatusNome.APROVADO, usuarioId)
            }
            NivelAcessoNome.GESTOR -> {
                // GESTOR: pedidos da equipe + outros gestores
                val idsEquipe = pedidoRepository.buscaIdsEquipeGestor(usuarioId)
                val idsGestores = pedidoRepository.buscaIdsGestores()
                val todosIds = (idsEquipe + idsGestores).distinct()
                pedidoRepository.buscaPedidosPorIdsUsuarios(StatusNome.APROVADO, todosIds)
            }
            NivelAcessoNome.RH -> {
                // RH: vê todos
                pedidoRepository.buscaPedidosPorStatus(StatusNome.APROVADO)
            }
            else -> {
                // Padrão: só seus próprios pedidos
                pedidoRepository.buscaPedidosCalendarioColaborador(StatusNome.APROVADO, usuarioId)
            }
        }
    }

    override fun buscaPedidosPendentesPorUsuario(usuarioId: Int): List<Pedido> {
        val usuario = usuarioService.buscaPorId(usuarioId)
        val nivelAcesso = usuario.nivelAcesso?.nome

        return when (nivelAcesso) {
            NivelAcessoNome.GESTOR -> {
                // GESTOR: pedidos PENDENTE_GESTOR da equipe (subordinados com fk_aprovador = gestorId)
                val idsSubordinados = pedidoRepository.buscaIdsSubordinadosGestor(usuarioId)
                if (idsSubordinados.isEmpty()) {
                    emptyList()
                } else {
                    pedidoRepository.buscaPedidosPendentesPorUsuarios(idsSubordinados)
                }
            }
            NivelAcessoNome.RH -> {
                // RH: pedidos PENDENTE_RH (todos)
                pedidoRepository.buscaPedidosPendentesRH()
            }
            else -> {
                // COLABORADOR não vê pedidos pendentes para aprovar
                emptyList()
            }
        }
    }
}