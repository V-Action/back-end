package com.vaction.vaction_service.application.dto.response

import com.vaction.vaction_service.domain.model.entity.Empresa
import com.vaction.vaction_service.domain.model.entity.NivelAcesso
import com.vaction.vaction_service.domain.model.entity.Usuario
import java.time.LocalDate

data class UsuarioResponse (
    var id:Int? = null,
    val nome: String,
    val cpf: String,
    val dataAdmissao: LocalDate?,
    val autenticado: Boolean?,
    val cargo: String?,
    val area: String?,
    val email: String,
    val empresa: Empresa,
    val nivelAcesso: NivelAcesso
){
    companion object {
        fun from(usuario: Usuario): UsuarioResponse {
            return UsuarioResponse(
                id = usuario.id,
                nome = usuario.nome!!,
                cpf = usuario.cpf!!,
                dataAdmissao = usuario.dataAdmissao,
                autenticado = usuario.autenticado,
                cargo = usuario.cargo,
                area = usuario.area,
                email = usuario.email!!,
                empresa = usuario.empresa!!,
                nivelAcesso = usuario.nivelAcesso!!
            )
        }
    }
}