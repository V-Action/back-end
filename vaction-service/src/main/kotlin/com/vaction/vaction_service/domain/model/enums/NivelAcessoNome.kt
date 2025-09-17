package com.vaction.vaction_service.domain.model.enums

enum class NivelAcessoNome(val descricao: String) {
    COLABORADOR("Funcionário"),
    GESTOR("Coordenador"),
    RH("Recursos Humanos");

    override fun toString(): String {
        return descricao
    }
}