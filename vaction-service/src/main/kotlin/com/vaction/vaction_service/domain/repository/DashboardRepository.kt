package com.vaction.vaction_service.domain.repository

import com.vaction.vaction_service.domain.model.entity.Empresa
import com.vaction.vaction_service.domain.repository.projection.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DashboardRepository : JpaRepository <Empresa, Int> {
    @Query(value = "call sp_tempo_proximas_ferias(:id);", nativeQuery = true)
    fun buscaProximasFerias(id: Int): List<ProximasFeriasProjection>?

    @Query(value = "CALL sp_analise_ferias_por_mes_empresa(:idEmpresa);", nativeQuery = true)
    fun buscarAnaliseFeriasPorMesEmpresa(idEmpresa: Int): List<AnaliseFeriasPorMesEmpresaProjection>?

    @Query(value = "CALL sp_analise_ferias_por_mes_gestor(:idGestor);", nativeQuery = true)
    fun buscarAnaliseFeriasPorMesGestor(idGestor: Int): List<AnaliseFeriasPorMesGestorProjection>?

    @Query(value = "CALL sp_saldo_ferias_usuario(:idUsuario);", nativeQuery = true)
    fun buscarSaldoFeriasUsuario(idUsuario: Int): List<SaldoFeriasProjection>?

    @Query(value = "CALL sp_chamados_pendentes_gestor_rh(:idUsuario);", nativeQuery = true)
    fun buscarChamadosPendentesGestorRh(idUsuario: Int): List<ChamadosPendentesGestorRhProjection>?

    @Query(value = "CALL sp_disponibilidade_equipe_por_gestor(:gestorId);", nativeQuery = true)
    fun buscarDisponibilidadeEquipePorGestor(gestorId: Int): List<DisponibilidadeEquipePorGestorProjection>?

    @Query(value = "CALL sp_sla_medio_por_empresa(:idEmpresa);", nativeQuery = true)
    fun buscarSlaMedioPorEmpresa(idEmpresa: Int): List<SlaMedioPorEmpresaProjection>?

}