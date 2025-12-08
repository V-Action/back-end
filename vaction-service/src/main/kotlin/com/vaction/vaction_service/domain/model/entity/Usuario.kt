package com.vaction.vaction_service.domain.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import jakarta.validation.constraints.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate

@Entity
data class Usuario(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Column(name = "id_usuario")
    var id: Int? = null,

    @field:NotNull
    val nome: String?,

    @field:NotNull
    @field:CPF
    val cpf: String?,

    var dataAdmissao: LocalDate?,

    var autenticado: Boolean? = false,

    var cargo: String?,

    var area: String?,

    @field:Email
    @field:NotNull
    val email: String?,

    var senha: String?,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @field:ManyToOne
    @field:JoinColumn(name = "fk_empresa")
    val empresa: Empresa?,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @field:ManyToOne
    @field:JoinColumn(name = "fk_nivel")
    val nivelAcesso: NivelAcesso?
)
