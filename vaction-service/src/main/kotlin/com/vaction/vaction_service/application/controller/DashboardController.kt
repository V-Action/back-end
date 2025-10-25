package com.vaction.vaction_service.application.controller

import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.service.PedidoService
import com.vaction.vaction_service.domain.service.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dashboard")
class DashboardController (
    val usuarioService: UsuarioService,
    val pedidoService: PedidoService
){
    @CrossOrigin
    @GetMapping("/saldo-ferias")
    fun buscaUsuariosNovoPelaDataCorte(
        @PathVariable id: Int
    ): ResponseEntity<Int> {
        val usuario = usuarioService.buscaPorId(id)
        val pedidosAprovados = pedidoService.buscaPedidosAprovadosPorUsuario(usuario.id!!)
        val saldoFerias = usuarioService.calculaSaldoFerias(usuario, pedidosAprovados)

        return ResponseEntity.status(200).body(saldoFerias)
    }

    @CrossOrigin
    @GetMapping("/ultima-solicitacao")
    fun buscaUltimaSolicitacao(
        @PathVariable id: Int
    ): ResponseEntity<Pedido> {
        val usuario = usuarioService.buscaPorId(id)
        val pedido = pedidoService.buscaPedidoPorUsuarioOuStatus(usuario.id!!, null).maxByOrNull { it.dataSolicitacao!! }

        return ResponseEntity.status(200).body(pedido)
    }

    /*
    @CrossOrigin
    @GetMapping("/proximas-ferias")
    fun buscaUltimaSolicitacao(
        @PathVariable id: Int
    ): Int

     */
}