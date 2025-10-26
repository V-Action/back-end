package com.vaction.vaction_service.domain.repository.projection

interface DisponibilidadeEquipePorGestorProjection {
    fun getGestor(): String?
    fun getTotal_Colaboradores(): Int?
    fun getEm_Ferias(): Int?
    fun getDisponiveis(): Int?
}