package com.vaction.vaction_service.application.controller


import com.vaction.vaction_service.application.dto.request.LoginForm
import com.vaction.vaction_service.application.dto.response.UsuarioResponse
import com.vaction.vaction_service.domain.model.entity.Usuario
import com.vaction.vaction_service.domain.repository.UsuarioRepository
import com.vaction.vaction_service.domain.service.UsuarioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/usuarios")
class UsuarioController(
    val usuarioRepository: UsuarioRepository,
    val usuarioService: UsuarioService
) {

    @Operation(
        summary = "Autentique o usuário",
        description = "Autentique o usuário com base no tipo dele (aluno, professor ou representante legal)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Autenticação com sucesso"),
            ApiResponse(responseCode = "403", description = "Erro no login"),
            ApiResponse(responseCode = "401", description = "Erro no nível de acesso")
        ]
    )

    @GetMapping("/{id}")
    @CrossOrigin
    fun buscaUsuarioPorId(
        @PathVariable id: Int
    ): ResponseEntity<UsuarioResponse> {
        val usuario = usuarioService.buscaPorId(id)
        val response = usuarioService.retornaUsuario(usuario)
        return ResponseEntity.status(200).body(response)
    }

    @PostMapping("/autenticar")
    @CrossOrigin
    fun autenticarUsuario(
        @RequestBody loginForm: LoginForm
    ): ResponseEntity<UsuarioResponse> {
        try {
            val usuario = usuarioRepository.findByEmailOrCpfAndSenha(loginForm.email, loginForm.cpf, loginForm.senha)
            val novoUsuario = usuarioService.autenticar(usuario.id!!)

            return ResponseEntity.status(201).body(novoUsuario.copy(autenticado = true))
        } catch (e: EmptyResultDataAccessException) {
            return ResponseEntity.status(403).build()
        }
    }

    @Operation(
        summary = "Desautentique o usuário",
        description = "Desautentique o usuário com base no tipo dele (aluno, professor ou representante legal)"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Desautenticação feita com sucesso"),
            ApiResponse(responseCode = "404", description = "Não existe"),
            ApiResponse(responseCode = "401", description = "Erro no nível de acesso no parâmetro da requisição")
        ]
    )
    @PostMapping("/desautenticar/{id}")
    fun desautenticarUsuario(
        @PathVariable id: Int
    ): ResponseEntity<Void> {
        if (usuarioRepository.existsById(id)) {
            val usuarioDesautenticado = usuarioRepository.findById(id).get()

            usuarioService.desautenticar(usuarioDesautenticado.id!!)
            return ResponseEntity.status(200).build()
        }
        return ResponseEntity.status(404).build()
    }

    @Operation(summary = "Busque os usuários", description = "Busque todos os professores.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Professores buscados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum professor encontrado")
        ]
    )
    @GetMapping()
    @CrossOrigin
    fun buscaUsuarios(): ResponseEntity<List<UsuarioResponse>> {
        val listaUsuarios = usuarioService.buscaUsuarios()

        if (listaUsuarios.isEmpty()) {
            throw ResponseStatusException(HttpStatus.NO_CONTENT)
        }

        return ResponseEntity.status(200).body(listaUsuarios)
    }

    @Operation(summary = "Salve um aluno", description = "Salve um aluno com as informações dele.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Criado com sucesso"),
            ApiResponse(responseCode = "401", description = "Erro no nível de acesso no corpo da requisição")
        ]
    )
    @PostMapping()
    @CrossOrigin
    fun salvaUsuario(
        @RequestBody @Valid novoUsuario: Usuario
    ): ResponseEntity<UsuarioResponse> {

        val usuarioSalvo = usuarioService.salvar(novoUsuario)
        return ResponseEntity.status(201).body(usuarioSalvo)
    }

    @Operation(
        summary = "Edite um aluno",
        description = "Edite um aluno com as informações dele no corpo e o id no parâmetro."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Aluno editado"),
            ApiResponse(responseCode = "404", description = "Aluno não existe"),
            ApiResponse(
                responseCode = "401",
                description = "Erro no nível de acesso no parâmetro ou corpo da requisição"
            )
        ]
    )
    @PutMapping("/{id}")
    @CrossOrigin
    fun editaUsuario(
        @PathVariable id: Int,
        @RequestBody novoUsuario: Usuario
    ): ResponseEntity<UsuarioResponse> {
        if (usuarioRepository.existsById(id)) {
            val usuarioAntigo = usuarioRepository.findById(id).get()

            if (usuarioAntigo.nivelAcesso!!.id !== novoUsuario.nivelAcesso!!.id) return ResponseEntity.status(401)
                .build()

            novoUsuario.id = id
            val usuarioEditado = usuarioService.edita(novoUsuario)
            return ResponseEntity.status(200).body(usuarioEditado)
        }
        return ResponseEntity.status(404).build()
    }

    @DeleteMapping("/{id}")
    @CrossOrigin
    fun deletaUsuario(
        @PathVariable id: Int
    ): ResponseEntity<Void> {
        if (usuarioRepository.existsById(id)) {
            usuarioService.deleta(id)
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).build()
    }
}