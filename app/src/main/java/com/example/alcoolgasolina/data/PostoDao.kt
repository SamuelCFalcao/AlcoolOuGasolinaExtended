package com.example.alcoolgasolina.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PostoDao {
    
    @Query("SELECT * FROM postos ORDER BY dataCadastro DESC")
    fun listarTodos(): Flow<List<Posto>>
    
    @Query("SELECT * FROM postos WHERE id = :id")
    suspend fun obterPorId(id: String): Posto?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(posto: Posto)
    
    @Update
    suspend fun atualizar(posto: Posto)
    
    @Delete
    suspend fun excluir(posto: Posto)
    
    @Query("DELETE FROM postos WHERE id = :id")
    suspend fun excluirPorId(id: String)
}
