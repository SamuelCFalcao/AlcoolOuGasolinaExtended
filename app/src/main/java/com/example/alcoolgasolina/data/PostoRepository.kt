package com.example.alcoolgasolina.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.UUID

class PostoRepository(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val postoDao = database.postoDao()
    
    // Mantém SharedPreferences apenas para o switch de percentual
    private val prefs: SharedPreferences =
        context.getSharedPreferences("alcool_gasolina_prefs", Context.MODE_PRIVATE)

    fun salvarPercentualSwitch(usar75: Boolean) {
        prefs.edit().putBoolean("usar_75_porcento", usar75).apply()
    }

    fun carregarPercentualSwitch(): Boolean {
        return prefs.getBoolean("usar_75_porcento", false)
    }

    suspend fun salvarPosto(posto: Posto) {
        postoDao.inserir(posto)
    }

    fun listarPostos(): Flow<List<Posto>> {
        return postoDao.listarTodos()
    }
    
    suspend fun listarPostosSync(): List<Posto> {
        return postoDao.listarTodos().first()
    }

    suspend fun obterPosto(id: String): Posto? {
        return postoDao.obterPorId(id)
    }

    suspend fun excluirPosto(id: String) {
        postoDao.excluirPorId(id)
    }

    fun gerarNovoId(): String = UUID.randomUUID().toString()
}