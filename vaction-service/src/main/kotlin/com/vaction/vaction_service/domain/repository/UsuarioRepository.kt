package com.vaction.vaction_service.domain.repository

import com.vaction.vaction_service.domain.model.entity.Usuario
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface UsuarioRepository : JpaRepository <Usuario, Int> {

    @Query("SELECT u FROM Usuario u WHERE (u.email = :email OR u.cpf = :cpf) AND u.senha = :senha")
    fun findByEmailOrCpfAndSenha(email: String?, cpf: String?, senha: String?): Usuario

    @Transactional
    @Modifying
    @Query("update Usuario u set u.autenticado = true where u.id = :id")
    fun autenticar(id: Int?): Int
    @Transactional
    @Modifying
    @Query("update Usuario u set u.autenticado = false where u.id = :id")
    fun desautenticar(id: Int?): Int
    
    @Transactional
    @Modifying
    @Query("""
        update Usuario u 
        set u.cargo = :cargo,
            u.area = :area,
            u.dataAdmissao = :dataAdmissao,
            u.senha = :senha
        where u.id = :id
    """)
    fun atualizaCamposMutaveis(
        id: Int,
        cargo: String?,
        area: String?,
        dataAdmissao: LocalDate?,
        senha: String?
    ): Int
}