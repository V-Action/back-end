package com.vaction.vaction_service.domain.service

import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Status

interface PedidoService {
    fun solicitar(pedido: Pedido): Pedido

    fun buscaPedido(): List<Pedido>
    fun atualizaStatusPedido(id: Int, status: Status): Int
}