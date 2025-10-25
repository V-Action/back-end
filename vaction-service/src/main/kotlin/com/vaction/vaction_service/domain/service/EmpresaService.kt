package com.vaction.vaction_service.domain.service

import com.vaction.vaction_service.domain.model.entity.Empresa

interface EmpresaService {
    fun buscaEmpresas(): List<Empresa>
}