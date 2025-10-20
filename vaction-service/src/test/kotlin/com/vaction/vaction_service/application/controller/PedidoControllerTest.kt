/*
package com.vaction.vaction_service.application.controller

import com.vaction.vaction_service.domain.model.entity.Historico
import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Status
import com.vaction.vaction_service.domain.model.enums.DecisaoNome
import com.vaction.vaction_service.domain.model.enums.NivelAcessoNome
import com.vaction.vaction_service.domain.model.enums.StatusNome
import com.vaction.vaction_service.domain.service.HistoricoService
import com.vaction.vaction_service.domain.service.PedidoService
import com.vaction.vaction_service.domain.service.UsuarioService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class PedidoControllerTest {

    private val pedidoService = mock(PedidoService::class.java)
    private val historicoService = mock(HistoricoService::class.java)
    private val usuarioService = mock(UsuarioService::class.java)
    private val pedidoController = PedidoController(pedidoService, historicoService, usuarioService)

    @Test
    fun solicitarPedido_createsPedidoSuccessfully() {
        val pedido = Pedido().apply {
            usuario = mock(com.vaction.vaction_service.domain.model.entity.Usuario::class.java)
            dataInicio = java.time.LocalDate.of(2025, 1, 1)
            dataFim = java.time.LocalDate.of(2025, 1, 10)
        }
        val usuario = mock(com.vaction.vaction_service.domain.model.entity.Usuario::class.java)
        `when`(usuarioService.buscaPorId(anyInt())).thenReturn(usuario)
        `when`(pedidoService.buscaPedidosAprovadosPorUsuario(anyInt())).thenReturn(emptyList())
        `when`(pedidoService.solicitar(any(Pedido::class.java))).thenReturn(pedido)

        val response = pedidoController.solicitarPedido(pedido)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(pedido, response.body)
    }

    @Test
    fun solicitarPedido_throwsUnauthorizedWhenUsuarioIsNull() {
        val pedido = Pedido().apply {
            usuario = null
        }

        val exception = assertThrows(ResponseStatusException::class.java) {
            pedidoController.solicitarPedido(pedido)
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)
        assertEquals("Faltando usuario no pedido", exception.reason)
    }

    @Test
    fun solicitarPedido_throwsForbiddenWhenSaldoFeriasIsInsufficient() {
        val pedido = Pedido().apply {
            usuario = mock(com.vaction.vaction_service.domain.model.entity.Usuario::class.java)
            dataInicio = java.time.LocalDate.of(2025, 1, 1)
            dataFim = java.time.LocalDate.of(2025, 1, 20)
        }
        val usuario = mock(com.vaction.vaction_service.domain.model.entity.Usuario::class.java)
        `when`(usuarioService.buscaPorId(anyInt())).thenReturn(usuario)
        `when`(pedidoService.buscaPedidosAprovadosPorUsuario(anyInt())).thenReturn(emptyList())
        `when`(usuarioService.calculaSaldoFerias(any(), anyList())).thenReturn(10)

        val exception = assertThrows(ResponseStatusException::class.java) {
            pedidoController.solicitarPedido(pedido)
        }

        assertEquals(HttpStatus.FORBIDDEN, exception.status)
        assertEquals("Saldo de férias insuficiente", exception.reason)
    }

    @Test
    fun atualizaStatusPedido_updatesStatusSuccessfullyForGestor() {
        val historico = Historico().apply {
            usuario = mock(com.vaction.vaction_service.domain.model.entity.Usuario::class.java).apply {
                nivelAcesso = mock(com.vaction.vaction_service.domain.model.entity.NivelAcesso::class.java).apply {
                    nome = NivelAcessoNome.GESTOR
                }
            }
            pedido = Pedido().apply { id = 1 }
            decisao = mock(com.vaction.vaction_service.domain.model.entity.Decisao::class.java).apply {
                nome = DecisaoNome.APROVADO
            }
        }
        `when`(historicoService.salvaHistorico(any(Historico::class.java))).thenReturn(historico)
        `when`(pedidoService.atualizaStatusPedido(anyInt(), any(Status::class.java))).thenReturn(1)

        val response = pedidoController.atualizaStatusPedido(historico)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(StatusNome.PENDENTE_RH, response.body?.statusPedido)
    }

    @Test
    fun atualizaStatusPedido_throwsBadRequestForInvalidNivelAcesso() {
        val historico = Historico().apply {
            usuario = mock(com.vaction.vaction_service.domain.model.entity.Usuario::class.java).apply {
                nivelAcesso = mock(com.vaction.vaction_service.domain.model.entity.NivelAcesso::class.java).apply {
                    nome = null
                }
            }
        }

        val exception = assertThrows(ResponseStatusException::class.java) {
            pedidoController.atualizaStatusPedido(historico)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
        assertEquals("Nível de acesso inválido para atualizar o status do pedido", exception.reason)
    }
}
 */