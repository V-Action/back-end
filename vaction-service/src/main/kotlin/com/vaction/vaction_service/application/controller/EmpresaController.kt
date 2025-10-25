package com.vaction.vaction_service.application.controller

import com.vaction.vaction_service.domain.model.entity.Empresa
import com.vaction.vaction_service.domain.service.EmpresaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/empresas")
class EmpresaController (
    val empresaService: EmpresaService
){
    @CrossOrigin
    @GetMapping()
    fun buscaEmpresas(
    ): ResponseEntity<List<Empresa>> {
        val listaEmpresas = empresaService.buscaEmpresas()

        if (listaEmpresas.isEmpty()) {
            throw ResponseStatusException(HttpStatus.NO_CONTENT)
        }

        return ResponseEntity.status(200).body(listaEmpresas)
    }
}