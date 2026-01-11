package com.example.alcoolgasolina.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PostoDao {

    @Insert
    suspend fun inserir(posto: Posto)

    @Delete
    suspend fun deletar(posto: Posto)

    @Query("SELECT * FROM postos ORDER BY nome ASC")
    fun listarTodos(): Flow<List<Posto>>
}