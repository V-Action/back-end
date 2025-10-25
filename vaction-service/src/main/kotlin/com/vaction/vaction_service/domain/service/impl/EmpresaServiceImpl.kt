package com.vaction.vaction_service.domain.service.impl

import com.vaction.vaction_service.domain.model.entity.Empresa
import com.vaction.vaction_service.domain.repository.EmpresaRepository
import com.vaction.vaction_service.domain.service.EmpresaService
import org.springframework.stereotype.Service

@Service
class EmpresaServiceImpl(
    private val empresaRepository: EmpresaRepository
): EmpresaService {

    override fun buscaEmpresas(): List<Empresa> {
        val empresas = empresaRepository.findAll()
        return empresas
    }
}