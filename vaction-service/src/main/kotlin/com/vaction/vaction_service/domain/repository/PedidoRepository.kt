package com.vaction.vaction_service.domain.repository


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

    @Query("""
    SELECT p
    FROM Pedido p
    JOIN FETCH p.usuario u
    JOIN FETCH u.nivelAcesso
    JOIN FETCH u.empresa
    WHERE p.status.nome = :statusNome
    ORDER BY p.dataInicio ASC
""")
    fun buscaPedidosPorStatus(statusNome: StatusNome): List<Pedido>

    // COLABORADOR: só seus próprios pedidos
    @Query("""
    SELECT p
    FROM Pedido p
    JOIN FETCH p.usuario u
    JOIN FETCH u.nivelAcesso
    JOIN FETCH u.empresa
    WHERE p.status.nome = :statusNome
      AND u.id = :usuarioId
    ORDER BY p.dataInicio ASC
""")
    fun buscaPedidosCalendarioColaborador(statusNome: StatusNome, usuarioId: Int): List<Pedido>

    // Busca IDs de usuários da equipe do gestor (subordinados com fk_aprovador = gestorId)
    @Query(value = """
    SELECT u.id_usuario
    FROM usuario u
    WHERE u.fk_aprovador = :gestorId
""", nativeQuery = true)
    fun buscaIdsEquipeGestor(gestorId: Int): List<Int>

    // Busca IDs de todos os gestores
    @Query("""
    SELECT u.id
    FROM Usuario u
    JOIN u.nivelAcesso na
    WHERE na.nome = 'GESTOR'
""")
    fun buscaIdsGestores(): List<Int>

    // GESTOR: pedidos da equipe + outros gestores
    @Query("""
    SELECT DISTINCT p
    FROM Pedido p
    JOIN FETCH p.usuario u
    JOIN FETCH u.nivelAcesso
    JOIN FETCH u.empresa
    WHERE p.status.nome = :statusNome
      AND u.id IN :usuarioIds
    ORDER BY p.dataInicio ASC
""")
    fun buscaPedidosPorIdsUsuarios(statusNome: StatusNome, usuarioIds: List<Int>): List<Pedido>

    // RH: vê todos (já existe buscaPedidosPorStatus)

    // Busca IDs de usuários subordinados ao gestor
    @Query(value = """
    SELECT u.id_usuario
    FROM usuario u
    WHERE u.fk_aprovador = :gestorId
""", nativeQuery = true)
    fun buscaIdsSubordinadosGestor(gestorId: Int): List<Int>

    // Busca pedidos PENDENTE_GESTOR de uma lista de usuários
    @Query("""
    SELECT DISTINCT p
    FROM Pedido p
    JOIN FETCH p.usuario u
    JOIN FETCH u.nivelAcesso
    JOIN FETCH u.empresa
    WHERE p.status.nome = 'PENDENTE_GESTOR'
      AND u.id IN :usuarioIds
    ORDER BY p.dataSolicitacao ASC
""")
    fun buscaPedidosPendentesPorUsuarios(usuarioIds: List<Int>): List<Pedido>

    // RH: busca pedidos PENDENTE_RH (todos)
    @Query("""
    SELECT p
    FROM Pedido p
    JOIN FETCH p.usuario u
    JOIN FETCH u.nivelAcesso
    JOIN FETCH u.empresa
    WHERE p.status.nome = 'PENDENTE_RH'
    ORDER BY p.dataSolicitacao ASC
""")
    fun buscaPedidosPendentesRH(): List<Pedido>
}