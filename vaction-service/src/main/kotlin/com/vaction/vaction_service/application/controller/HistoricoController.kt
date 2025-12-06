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

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val notificacoes = historicos.map { h ->

                val pedido = h.pedido
                val statusBruto = (pedido?.status?.nome ?: "").toString()


                val statusLegivel = when (statusBruto.uppercase()) {
                    "PENDENTE_RH", "PENDENTE_GESTOR", "PENDENTE" -> "pendente"
                    "APROVADO_RH", "APROVADO_GESTOR", "APROVADO" -> "aprovada"
                    "REPROVADO_RH", "REPROVADO_GESTOR", "REPROVADO" -> "reprovada"
                    else -> statusBruto.lowercase()
                }

                val dataNotif = h.dataAlteracao ?: LocalDate.now()

                val dataInicio = pedido?.dataInicio?.format(formatter) ?: "-"
                val dataFim = pedido?.dataFim?.format(formatter) ?: "-"

                val mensagem =
                    "Sua solicitação de férias ($dataInicio - $dataFim) está $statusLegivel."

                NotificacaoResponse(
                    message = mensagem,
                    date = dataNotif
                )
            }

            return ResponseEntity.ok(notificacoes)
        }


    }

