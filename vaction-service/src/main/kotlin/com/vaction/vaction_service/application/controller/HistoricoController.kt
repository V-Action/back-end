package com.vaction.vaction_service.application.controller


import com.vaction.vaction_service.application.dto.response.NotificacaoResponse
import com.vaction.vaction_service.domain.service.HistoricoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RestController
@RequestMapping("/dashboard")
class HistoricoController (
    private val historicoService: HistoricoService
    )
    {
        @GetMapping("/notificacoes/{idUsuario}")
        @CrossOrigin
        fun notificacoesRecentes(
            @PathVariable idUsuario: Int
        ): ResponseEntity<List<NotificacaoResponse>> {

            val historicos = historicoService.buscarUltimosPorUsuario(idUsuario)

            if (historicos.isEmpty()) {
                return ResponseEntity.status(204).body(emptyList())
            }

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val notificacoes = historicos.map { h ->

                val pedido = h.pedido
                val decisao = h.decisao?.nome?.toString() ?: ""
                val usuarioAprovador = h.usuario
                val nivelAprovador = usuarioAprovador?.nivelAcesso?.nome?.toString() ?: ""
                val statusBruto = (pedido?.status?.nome ?: "").toString()

                val dataNotif = h.dataAlteracao ?: LocalDate.now()
                val dataInicio = pedido?.dataInicio?.format(formatter) ?: "-"
                val dataFim = pedido?.dataFim?.format(formatter) ?: "-"

                // Monta mensagem baseada na decisão e fase
                val mensagem = when {
                    decisao.uppercase() == "APROVADO" && nivelAprovador.uppercase() == "GESTOR" -> 
                        "Sua solicitação de férias ($dataInicio - $dataFim) foi aprovada pelo gestor."
                    decisao.uppercase() == "APROVADO" && nivelAprovador.uppercase() == "RH" -> 
                        "Sua solicitação de férias ($dataInicio - $dataFim) foi aprovada pelo RH."
                    decisao.uppercase() == "REPROVADO" && nivelAprovador.uppercase() == "GESTOR" -> 
                        "Sua solicitação de férias ($dataInicio - $dataFim) foi reprovada pelo gestor."
                    decisao.uppercase() == "REPROVADO" && nivelAprovador.uppercase() == "RH" -> 
                        "Sua solicitação de férias ($dataInicio - $dataFim) foi reprovada pelo RH."
                    statusBruto.uppercase() == "PENDENTE_GESTOR" -> 
                        "Sua solicitação de férias ($dataInicio - $dataFim) está pendente de aprovação do gestor."
                    statusBruto.uppercase() == "PENDENTE_RH" -> 
                        "Sua solicitação de férias ($dataInicio - $dataFim) está pendente de aprovação do RH."
                    else -> 
                        "Sua solicitação de férias ($dataInicio - $dataFim) foi atualizada."
                }

                NotificacaoResponse(
                    message = mensagem,
                    date = dataNotif
                )
            }

            return ResponseEntity.ok(notificacoes)
        }


    }

