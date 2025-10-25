package com.vaction.vaction_service.domain.service

import com.vaction.vaction_service.application.dto.response.UsuarioResponse
import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Usuario

interface UsuarioService {

    fun buscaPorId(id: Int): Usuario
    fun calculaSaldoFerias(usuario: Usuario, pedidosAprovados: List<Pedido>): Int
    fun autenticar(id: Int): UsuarioResponse
    fun retornaUsuario(usuario: Usuario): UsuarioResponse
    fun desautenticar(id: Int): UsuarioResponse

    fun salvar(usuario: Usuario): UsuarioResponse

    fun deleta(id: Int)

    fun edita(novoUsuario: Usuario): UsuarioResponse
    fun buscaUsuarios(): List<UsuarioResponse>
}