package com.vaction.vaction_service.application.controller

import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.repository.DashboardRepository
import com.vaction.vaction_service.domain.repository.projection.*
import com.vaction.vaction_service.domain.service.PedidoService
import com.vaction.vaction_service.domain.service.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dashboard")
class DashboardController (
    val usuarioService: UsuarioService,
    val pedidoService: PedidoService,
    val dashboardRepository: DashboardRepository
){
    @CrossOrigin
    @GetMapping("/saldo-ferias/{id}")
    fun buscaSaldoFerias(
        @PathVariable id: Int
    ): ResponseEntity<List<SaldoFeriasProjection>> {
        val saldoFerias = dashboardRepository.buscarSaldoFeriasUsuario(id)

        return ResponseEntity.status(200).body(saldoFerias)
    }

    @CrossOrigin
    @GetMapping("/ultima-solicitacao/{id}")
    fun buscaUltimaSolicitacao(
        @PathVariable id: Int
    ): ResponseEntity<Pedido> {
        val usuario = usuarioService.buscaPorId(id)
        val pedido = pedidoService.buscaPedidoPorUsuarioOuStatus(usuario.id!!, null).maxByOrNull { it.dataSolicitacao!! }

        return ResponseEntity.status(200).body(pedido)
    }


    @CrossOrigin
    @GetMapping("/proximas-ferias/{id}")
    fun buscaProximasFerias(
        @PathVariable id: Int
    ): ResponseEntity<List<ProximasFeriasProjection>> {
        val diasProximasFerias = dashboardRepository.buscaProximasFerias(id)

        return ResponseEntity.status(200).body(diasProximasFerias)
    }

    @CrossOrigin
    @GetMapping("/analise-ferias-mes/{id}")
    fun buscarAnaliseFeriasPorMesEmpresa(
        @PathVariable id: Int
    ): ResponseEntity<List<AnaliseFeriasPorMesEmpresaProjection>> {
        val analiseMensal = dashboardRepository.buscarAnaliseFeriasPorMesEmpresa(id)

        return ResponseEntity.status(200).body(analiseMensal)
    }

    @CrossOrigin
    @GetMapping("/chamados-pendentes/{id}")
    fun buscarChamadosPendentesGestorRh(
        @PathVariable id: Int
    ): ResponseEntity<List<ChamadosPendentesGestorRhProjection>> {
        val chamadosPendentesGestorRh = dashboardRepository.buscarChamadosPendentesGestorRh(id)

        return ResponseEntity.status(200).body(chamadosPendentesGestorRh)
    }

    @CrossOrigin
    @GetMapping("/disponibilidade-equipe/{id}")
    fun buscarDisponibilidadeEquipePorGestor(
        @PathVariable id: Int
    ): ResponseEntity<List<DisponibilidadeEquipePorGestorProjection>> {
        val disponibilidade = dashboardRepository.buscarDisponibilidadeEquipePorGestor(id)

        return ResponseEntity.status(200).body(disponibilidade)
    }


    @CrossOrigin
    @GetMapping("/sla-medio/{id}")
    fun buscarSlaMedioPorEmpresa(
        @PathVariable id: Int
    ): ResponseEntity<List<SlaMedioPorEmpresaProjection>> {
        val sla = dashboardRepository.buscarSlaMedioPorEmpresa(id)

        return ResponseEntity.status(200).body(sla)
    }

}