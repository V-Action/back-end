package com.vaction.vaction_service.domain.repository

import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Status
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PedidoRepository : JpaRepository <Pedido, Int> {

    @Transactional
    @Modifying
    @Query("update Pedido p set p.status = :status where p.id = :id")
    fun atualizaStatusPeloId(id: Int, status: Status): Int
}