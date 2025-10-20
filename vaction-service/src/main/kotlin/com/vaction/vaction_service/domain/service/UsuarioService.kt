package com.vaction.vaction_service.domain.service

import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Usuario

interface UsuarioService {

    fun buscaPorId(id: Int): Usuario
    fun calculaSaldoFerias(usuario: Usuario, pedidosAprovados: List<Pedido>): Int
}