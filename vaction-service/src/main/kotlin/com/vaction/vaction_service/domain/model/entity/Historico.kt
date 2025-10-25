package com.vaction.vaction_service.domain.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import jakarta.validation.constraints.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDate

@Entity
data class Historico(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Column(name = "id_alteracao")
    var id: Int? = null,

    @field:JsonFormat(pattern = "yyyy-MM-dd")
    @field:NotNull
    val dataAlteracao: LocalDate? = LocalDate.now(),

    val observacao: String?,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @field:ManyToOne
    @field:JoinColumn(name = "fk_pedido")
    val pedido: Pedido?,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.EAGER)
    @field:JoinColumn(name = "fk_usuario")
    val usuario: Usuario?,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @field:ManyToOne
    @field:JoinColumn(name = "fk_decisao")
    val decisao: Decisao?,
)
