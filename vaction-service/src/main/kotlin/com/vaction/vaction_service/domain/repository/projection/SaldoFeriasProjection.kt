package com.vaction.vaction_service.domain.repository.projection
interface SaldoFeriasProjection {
    fun getId_Usuario(): Long?
    fun getNome_Usuario(): String?
    fun getData_Admissao(): String?
    fun getMeses_Trabalhados(): Int?
    fun getDias_Direito(): Double?
    fun getDias_Usufruidos(): Double?
    fun getSaldo_Ferias(): Double?
}