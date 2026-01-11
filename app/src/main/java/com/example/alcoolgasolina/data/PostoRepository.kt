package com.example.alcoolgasolina.data

import kotlinx.coroutines.flow.Flow

class PostoRepository(private val dao: PostoDao) {

    fun listarPostos(): Flow<List<Posto>> {
        return dao.listarTodos()
    }

    suspend fun adicionarPosto(posto: Posto) {
        dao.inserir(posto)
    }

    suspend fun removerPosto(posto: Posto) {
        dao.deletar(posto)
    }
}
