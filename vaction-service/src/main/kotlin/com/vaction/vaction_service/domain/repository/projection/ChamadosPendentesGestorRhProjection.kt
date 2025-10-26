package com.vaction.vaction_service.domain.repository.projection

interface ChamadosPendentesGestorRhProjection {
    fun getId_Funcionario(): Long?
    fun getNome_Funcionario(): String?
    fun getId_Pedido(): Long?
    fun getData_Inicio(): String?
    fun getData_Fim(): String?
    fun getData_Solicitacao(): String?
    fun getStatus_Pedido(): String?
}
