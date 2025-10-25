package com.vaction.vaction_service.domain.repository

import com.vaction.vaction_service.domain.model.entity.Empresa
import org.springframework.data.jpa.repository.JpaRepository

interface EmpresaRepository : JpaRepository <Empresa, Int> {

}