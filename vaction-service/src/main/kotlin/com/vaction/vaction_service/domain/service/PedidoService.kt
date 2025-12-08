package com.vaction.vaction_service.domain.service

import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Status
import com.vaction.vaction_service.domain.model.enums.StatusNome

interface PedidoService {
    fun solicitar(pedido: Pedido): Pedido

    fun buscaPedido(): List<Pedido>
    fun buscaPedidoPorId(id: Int): Pedido
    fun atualizaStatusPedido(id: Int, status: Status): Int
    fun buscaPedidosAprovadosPorUsuario(id: Int): List<Pedido>
    fun buscaPedidoPorUsuarioOuStatus(id: Int, status: StatusNome?): List<Pedido>
    fun buscaPedidosAprovados(): List<Pedido>
    fun buscaPedidosCalendarioPorUsuario(usuarioId: Int): List<Pedido>
    fun buscaPedidosPendentesPorUsuario(usuarioId: Int): List<Pedido>
}