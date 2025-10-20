package com.vaction.vaction_service.domain.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import jakarta.validation.constraints.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDate

@Entity
data class Pedido(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Column(name = "id_pedido")
    var id: Int? = null,

    @field:JsonFormat(pattern = "yyyy-MM-dd")
    @field:NotNull
    val dataInicio: LocalDate?,

    @field:JsonFormat(pattern = "yyyy-MM-dd")
    @field:NotNull
    val dataFim: LocalDate?,

    @field:JsonFormat(pattern = "yyyy-MM-dd")
    @field:NotNull
    val dataSolicitacao: LocalDate? = LocalDate.now(),

    var diasUsufruidos: Int?,

    @field:JsonFormat(pattern = "yyyy-MM-dd")
    var ultimaAtualizacao: LocalDate? = LocalDate.now(),

    @OnDelete(action = OnDeleteAction.CASCADE)
    @field:ManyToOne
    @field:JoinColumn(name = "fk_usuario")
    val usuario: Usuario?,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @field:ManyToOne
    @field:JoinColumn(name = "fk_status")
    var status: Status?
)
