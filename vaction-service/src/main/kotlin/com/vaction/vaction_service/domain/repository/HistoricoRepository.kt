package com.vaction.vaction_service.domain.repository

import com.vaction.vaction_service.domain.model.entity.Historico
import com.vaction.vaction_service.domain.model.entity.Pedido
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface HistoricoRepository : JpaRepository <Historico, Int> {
    fun findTop3ByUsuario_IdOrderByDataAlteracaoDesc(idUsuario: Int): List<Historico>

    @Query("""
        SELECT h
        FROM Historico h
        JOIN FETCH h.pedido p
        JOIN FETCH p.usuario u
        JOIN FETCH h.usuario aprovedor
        JOIN FETCH aprovedor.nivelAcesso
        JOIN FETCH h.decisao
        WHERE u.id = :idUsuario
        ORDER BY h.dataAlteracao DESC
    """)
    fun findTop5ByPedidoUsuarioIdOrderByDataAlteracaoDesc(idUsuario: Int): List<Historico>

}