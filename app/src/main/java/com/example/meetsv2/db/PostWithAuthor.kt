package com.example.meetsv2.db

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Classe que representa a junção de um Post com seu autor (User).
 * Room usa a anotação @Relation para buscar esses dados relacionados automaticamente.
 */
data class PostWithAuthor(
    // A anotação @Embedded diz ao Room para tratar os campos do Post como se fossem da própria classe PostWithAuthor
    @Embedded
    val post: Post,

    // A anotação @Relation define a relação entre as tabelas
    @Relation(
        parentColumn = "userId", // Coluna na tabela de posts (a classe pai)
        entityColumn = "id"     // Coluna correspondente na tabela de usuários (a entidade relacionada)
    )
    val user: User
)
