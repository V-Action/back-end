package com.vaction.vaction_service.domain.service.impl

import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Status
import com.vaction.vaction_service.domain.model.entity.Usuario
import com.vaction.vaction_service.domain.repository.PedidoRepository
import com.vaction.vaction_service.domain.repository.UsuarioRepository
import com.vaction.vaction_service.domain.service.PedidoService
import com.vaction.vaction_service.domain.service.UsuarioService
import org.springframework.stereotype.Service

@Service
class UsuarioServiceImpl(
    private val usuarioRepository: UsuarioRepository
): UsuarioService {
    override fun buscaPorId(id: Int): Usuario {
        return usuarioRepository.findById(id).orElseThrow { Exception("Pedido não encontrado") }
    }
}