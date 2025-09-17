package com.vaction.vaction_service.domain.service.impl

import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Status
import com.vaction.vaction_service.domain.repository.PedidoRepository
import com.vaction.vaction_service.domain.service.PedidoService
import org.springframework.stereotype.Service

@Service
class PedidoServiceImpl(
    private val pedidoRepository: PedidoRepository
): PedidoService {
    override fun solicitar(pedido: Pedido): Pedido {
        return pedidoRepository.save(pedido)
    }

    override fun buscaPedido(): List<Pedido> {
        return pedidoRepository.findAll()
    }

    override fun atualizaStatusPedido(id: Int, status: Status): Int {
        val pedido = pedidoRepository.atualizaStatusPeloId(id, status)
        return pedido
    }
}