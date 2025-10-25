package com.vaction.vaction_service.domain.service.impl

import com.vaction.vaction_service.application.dto.response.UsuarioResponse
import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Usuario
import com.vaction.vaction_service.domain.repository.UsuarioRepository
import com.vaction.vaction_service.domain.service.UsuarioService
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
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

    override fun desautenticar(id: Int): UsuarioResponse {
        usuarioRepository.desautenticar(id)
        return retornaUsuario(usuarioRepository.findById(id).get())
    }

    override fun autenticar(id: Int): UsuarioResponse {
        usuarioRepository.autenticar(id)
        return retornaUsuario(usuarioRepository.findById(id).get())
    }

    override fun buscaUsuarios(): List<UsuarioResponse> {
        val usuarios = usuarioRepository.findAll()
        return usuarios.map { retornaUsuario(it) }
    }

    override fun salvar(usuario: Usuario): UsuarioResponse {
        val usuarioSalvo = usuarioRepository.save(usuario)
        return retornaUsuario(usuarioSalvo)
    }

    override fun edita(novoUsuario: Usuario): UsuarioResponse {
        val usuarioExistente = usuarioRepository.findById(novoUsuario.id!!)
        if (usuarioExistente.isEmpty) {
            throw ResponseStatusException(HttpStatusCode.valueOf(404)) // Status 404 Not Found
        }
        if (novoUsuario.senha == null) {
            novoUsuario.senha = usuarioExistente.get().senha
        } else if (novoUsuario.senha != usuarioExistente.get().senha) {
            throw ResponseStatusException(HttpStatusCode.valueOf(401))
        }

        novoUsuario.autenticado = usuarioExistente.get().autenticado;

        val usuario = usuarioRepository.save(novoUsuario)
        val usuarioResponse = retornaUsuario(usuario)
        return usuarioResponse
    }

    override fun deleta(id: Int) {
        usuarioRepository.deleteById(id)
    }

    override fun retornaUsuario(usuario: Usuario): UsuarioResponse {
        val dto = UsuarioResponse.from(usuario)

        return dto
    }
}