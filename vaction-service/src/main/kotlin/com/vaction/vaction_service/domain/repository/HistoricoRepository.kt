package com.vaction.vaction_service.domain.repository

import com.vaction.vaction_service.domain.model.entity.Historico
import com.vaction.vaction_service.domain.model.entity.Pedido
import org.springframework.data.jpa.repository.JpaRepository

interface HistoricoRepository : JpaRepository <Historico, Int> {
}