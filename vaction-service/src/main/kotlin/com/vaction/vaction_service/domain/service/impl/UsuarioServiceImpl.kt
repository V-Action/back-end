package com.vaction.vaction_service.domain.service.impl

import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Usuario
import com.vaction.vaction_service.domain.repository.UsuarioRepository
import com.vaction.vaction_service.domain.service.UsuarioService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class UsuarioServiceImpl(
    private val usuarioRepository: UsuarioRepository
): UsuarioService {
    override fun buscaPorId(id: Int): Usuario {
        return usuarioRepository.findById(id).orElseThrow { Exception("Pedido não encontrado") }
    }

    override fun calculaSaldoFerias(usuario: Usuario, pedidosAprovados: List<Pedido>): Int {
        val mesesTrabalhados = ChronoUnit.MONTHS.between(usuario.dataAdmissao, LocalDate.now())
        val diasDireito = (mesesTrabalhados / 12.0) * 30
        val diasUsufruidos = pedidosAprovados.sumOf {
            if(it.diasUsufruidos != null){
                it.diasUsufruidos!!.toInt()
            } else {
                0
            }
        }
        return (diasDireito - diasUsufruidos).toInt().coerceAtLeast(0)
    }
}