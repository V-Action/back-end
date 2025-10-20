package com.vaction.vaction_service.domain.repository

import com.vaction.vaction_service.application.dto.response.PedidoResponse
import com.vaction.vaction_service.domain.model.entity.Pedido
import com.vaction.vaction_service.domain.model.entity.Status
import com.vaction.vaction_service.domain.model.enums.StatusNome
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PedidoRepository : JpaRepository <Pedido, Int> {

    @Transactional
    @Modifying
    @Query("update Pedido p set p.status = :status where p.id = :id")
    fun atualizaStatusPeloId(id: Int, status: Status): Int


    @Query("SELECT p FROM Pedido p WHERE p.usuario.id = :id AND p.status.id = :statusId")
    fun buscaPedidosAprovadosPorUsuario(id: Int, statusId: Int): List<Pedido>

    @Query("""
    SELECT 
        p
    FROM Pedido p
    JOIN p.status s
    JOIN p.usuario u
    WHERE (:idUsuario IS NULL OR u.id = :idUsuario)
      AND (:nomeStatus IS NULL OR s.nome = :nomeStatus)
""")
    fun buscaPedidoPorUsuarioOuStatus(idUsuario: Int, nomeStatus: StatusNome?): List<Pedido>
}