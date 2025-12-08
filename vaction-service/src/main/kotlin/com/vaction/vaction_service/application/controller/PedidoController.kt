package com.vaction.vaction_service.application.controller


import com.vaction.vaction_service.application.dto.response.PedidoAtualizadoResponse
import com.vaction.vaction_service.domain.model.entity.Historico
import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Status
import com.vaction.vaction_service.domain.model.enums.DecisaoNome
import com.vaction.vaction_service.domain.model.enums.NivelAcessoNome
import com.vaction.vaction_service.domain.model.enums.StatusNome
import com.vaction.vaction_service.domain.service.HistoricoService
import com.vaction.vaction_service.domain.service.PedidoService
import com.vaction.vaction_service.domain.service.UsuarioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/pedido")
class PedidoController(
    val pedidoService: PedidoService,
    val historicoService: HistoricoService,
    val usuarioService: UsuarioService
) {

    @Operation(
        summary = "Solicitar o pedido",
        description = "Autentique o usuário com base no tipo dele (aluno, professor ou representante legal)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Pedido feito com sucesso"),
            ApiResponse(responseCode = "403", description = "Erro no pedido, saldo de férias indísponivel"),
            ApiResponse(responseCode = "401", description = "Erro no nível de acesso")
        ]
    )

    @PostMapping("/solicitar")
    @CrossOrigin
    fun solicitarPedido(
        @Valid @RequestBody pedido: Pedido
    ): ResponseEntity<Pedido> {
        try {
            if(pedido.usuario == null){
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Faltando usuario no pedido")
            }
            pedido.status = Status(id=1, nome = StatusNome.PENDENTE_GESTOR)
            val usuario = usuarioService.buscaPorId(pedido.usuario!!.id!!)
            val pedidosAprovados = pedidoService.buscaPedidosAprovadosPorUsuario(usuario.id!!)
            val saldoFerias = usuarioService.calculaSaldoFerias(usuario, pedidosAprovados)
            val diasUsufruidos = pedido.dataFim!!.toEpochDay() - pedido.dataInicio!!.toEpochDay() + 1

            if(diasUsufruidos > saldoFerias){
                throw ResponseStatusException(HttpStatus.FORBIDDEN, "Saldo de férias insuficiente")
            }
            pedido.diasUsufruidos = diasUsufruidos.toInt()
            val novoPedido = pedidoService.solicitar(pedido)
            return ResponseEntity.status(201).body(novoPedido)

        } catch (e: EmptyResultDataAccessException) {
            return ResponseEntity.status(403).build()
        }
    }

    @Operation(summary = "Busque os pedidos", description = "Busque todos os pedidos.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Pedidos buscados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
        ]
    )
    @GetMapping
    @CrossOrigin
    fun buscaPedidos(): ResponseEntity<List<Pedido>> {

        val listaPedidos = pedidoService.buscaPedido()

        if (listaPedidos.isEmpty()) {
            throw ResponseStatusException(HttpStatus.NO_CONTENT)
        }

        return ResponseEntity.status(200).body(listaPedidos)
    }

    @Operation(summary = "Busque os pedidos pelo professor", description = "Busque todos os pedidos.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Pedidos buscados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
        ]
    )
    @GetMapping("/usuario/{id}")
    @CrossOrigin
    fun buscaPedidoPeloUsuario(
        @PathVariable id: Int,
        @RequestParam(required = false) status: String?
    ): ResponseEntity<List<Pedido>> {

        val statusEnum = try {
            status?.let { StatusNome.valueOf(it) }
        } catch (e: IllegalArgumentException) {
            null
        }
        val listaPedidos = pedidoService.buscaPedidoPorUsuarioOuStatus(id, statusEnum)

        if (listaPedidos.isEmpty()) {
            return ResponseEntity.status(204).body(emptyList())
        }

        return ResponseEntity.status(200).body(listaPedidos)
    }

    @Operation(summary = "Busque os pedidos", description = "Busque todos os pedidos.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Pedidos buscados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
        ]
    )
    @PutMapping
    @CrossOrigin
    fun atualizaStatusPedido( @Valid @RequestBody pedidoAtualizado: Historico): ResponseEntity<PedidoAtualizadoResponse> {

        // Busca o usuário completo do banco para ter acesso ao nivelAcesso
        val usuarioId = pedidoAtualizado.usuario?.id
        if (usuarioId == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não informado")
        }
        val usuarioCompleto = usuarioService.buscaPorId(usuarioId)
        
        // Busca o pedido completo
        val pedidoId = pedidoAtualizado.pedido?.id
        if (pedidoId == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido não informado")
        }
        val pedidoCompleto = pedidoService.buscaPedidoPorId(pedidoId)
        
        // Cria o histórico com os objetos completos
        val historicoCompleto = pedidoAtualizado.copy(
            usuario = usuarioCompleto,
            pedido = pedidoCompleto
        )
        
        val historico = historicoService.salvaHistorico(historicoCompleto)

        val updated: Int;
        val statusFinal: StatusNome;
        if(historico.usuario?.nivelAcesso?.nome == NivelAcessoNome.GESTOR){
            // GESTOR: se aprovar vai para PENDENTE_RH, se rejeitar vai para REPROVADO
            if(historico.decisao?.nome == DecisaoNome.APROVADO) {
                updated = pedidoService.atualizaStatusPedido(historico.pedido?.id!!, Status(id=2, nome = StatusNome.PENDENTE_RH))
                statusFinal = StatusNome.PENDENTE_RH
            } else {
                updated = pedidoService.atualizaStatusPedido(historico.pedido?.id!!, Status(id=4, nome = StatusNome.REPROVADO))
                statusFinal = StatusNome.REPROVADO
            }

        } else if(historico.usuario?.nivelAcesso?.nome == NivelAcessoNome.RH){
            // RH: se aprovar vai para APROVADO, se rejeitar vai para REPROVADO
            val updated = pedidoService.atualizaStatusPedido(historico.pedido?.id!!, historico.decisao?.nome!!.toStatus())
            statusFinal = if(historico.decisao?.nome == DecisaoNome.APROVADO) StatusNome.APROVADO else StatusNome.REPROVADO

        } else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Nível de acesso inválido para atualizar o status do pedido")
        }

        return ResponseEntity.status(200).body(PedidoAtualizadoResponse(
            dataAlteracao = historico.dataAlteracao,
            observacao = historico.observacao,
            usuario = historico.usuario,
            decisao = historico.decisao,
            statusPedido = statusFinal
        ))
    }

    @Operation(summary = "Busque pedidos aprovados para o calendário", description = "Retorna pedidos aprovados filtrados por nível de acesso do usuário.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Pedidos buscados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
        ]
    )
    @GetMapping("/calendario/{usuarioId}")
    @CrossOrigin
    fun buscaPedidosCalendario(@PathVariable usuarioId: Int): ResponseEntity<List<Pedido>> {
        val listaPedidos = pedidoService.buscaPedidosCalendarioPorUsuario(usuarioId)

        if (listaPedidos.isEmpty()) {
            return ResponseEntity.status(204).body(emptyList())
        }

        return ResponseEntity.status(200).body(listaPedidos)
    }

    @Operation(summary = "Busque pedidos pendentes para aprovação", description = "Retorna pedidos pendentes conforme nível de acesso: GESTOR vê PENDENTE_GESTOR da equipe, RH vê PENDENTE_RH.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Pedidos buscados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
        ]
    )
    @GetMapping("/pendentes/{usuarioId}")
    @CrossOrigin
    fun buscaPedidosPendentes(@PathVariable usuarioId: Int): ResponseEntity<List<Pedido>> {
        val listaPedidos = pedidoService.buscaPedidosPendentesPorUsuario(usuarioId)

        if (listaPedidos.isEmpty()) {
            return ResponseEntity.status(204).body(emptyList())
        }

        return ResponseEntity.status(200).body(listaPedidos)
    }
}