package com.vaction.vaction_service.domain.repository.projection

interface AnaliseFeriasPorMesEmpresaProjection {
    fun getGestor(): String?
    fun getJan(): Int?
    fun getFev(): Int?
    fun getMar(): Int?
    fun getAbr(): Int?
    fun getMai(): Int?
    fun getJun(): Int?
    fun getJul(): Int?
    fun getAgo(): Int?
    fun getSept(): Int?
    fun getOct(): Int?
    fun getNov(): Int?
    fun getDez(): Int?
}