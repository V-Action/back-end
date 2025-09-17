package com.vaction.vaction_service

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.sql.DataSource

@SpringBootTest
class JdbcConnectionTest {

    @Autowired
    private lateinit var dataSource: DataSource

    @Test
    fun `test database connection`() {
        dataSource.connection.use { connection ->
            val metaData = connection.metaData
            val databaseName = metaData.databaseProductName
            val databaseUrl = metaData.url

            println("Connected to database: $databaseName")
            println("Database URL: $databaseUrl")

            // Verifica se a conexão está ativa
            assertTrue(connection.isValid(2), "Connection to the database is not valid")
        }
    }
}