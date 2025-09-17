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
    val historicoService: HistoricoService
) {

    @Operation(
        summary = "Autentique o usuário",
        description = "Autentique o usuário com base no tipo dele (aluno, professor ou representante legal)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Pedido feito com sucesso"),
            ApiResponse(responseCode = "403", description = "Erro no pedido"),
            ApiResponse(responseCode = "401", description = "Erro no nível de acesso")
        ]
    )

    @PostMapping("/solicitar")
    @CrossOrigin
    fun solicitarPedido(
        @Valid @RequestBody pedido: Pedido
    ): ResponseEntity<Pedido> {
        try {
            pedido.status = Status(id=1, nome = StatusNome.PENDENTE_GESTOR)
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
/*
    @Operation(summary = "Busque os pedidos pelo professor", description = "Busque todos os pedidos.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Pedidos buscados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
        ]
    )
    @GetMapping("/{id}")
    @CrossOrigin
    fun buscaPedidoPeloProfessor(@RequestBody status: StatusNome): ResponseEntity<List<Pedido>> {

        val listaUsuarios = usuarioService.buscaPorAprovador()

        if (listaUsuarios.isEmpty()) {
            throw ResponseStatusException(HttpStatus.NO_CONTENT)
        }

        return ResponseEntity.status(200).body(listaUsuarios)
    }
 */

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

        val historico = historicoService.salvaHistorico(pedidoAtualizado)

        val updated: Int;
        val statusFinal: StatusNome;
        if(historico.usuario?.nivelAcesso?.nome == NivelAcessoNome.GESTOR){
            updated = pedidoService.atualizaStatusPedido(historico.pedido?.id!!, Status(id=2, nome = StatusNome.PENDENTE_RH))
            statusFinal = if(historico.decisao?.nome == DecisaoNome.APROVADO) StatusNome.PENDENTE_RH else StatusNome.PENDENTE_GESTOR

        } else if(historico.usuario?.nivelAcesso?.nome == NivelAcessoNome.RH){
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
}