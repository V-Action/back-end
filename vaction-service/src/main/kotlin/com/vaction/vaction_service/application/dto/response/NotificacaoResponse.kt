package com.vaction.vaction_service.application.dto.response
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
data class NotificacaoResponse (
    val message: String,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val date: LocalDate
    )