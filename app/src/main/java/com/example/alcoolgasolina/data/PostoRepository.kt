package com.example.alcoolgasolina.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.util.UUID

class PostoRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("alcool_gasolina_prefs", Context.MODE_PRIVATE)

    private val json = Json { ignoreUnknownKeys = true }

    fun salvarPercentualSwitch(usar75: Boolean) {
        prefs.edit().putBoolean("usar_75_porcento", usar75).apply()
    }

    fun carregarPercentualSwitch(): Boolean {
        return prefs.getBoolean("usar_75_porcento", false)
    }

    fun salvarPosto(posto: Posto) {
        val postos = listarPostos().toMutableList()
        val index = postos.indexOfFirst { it.id == posto.id }

        if (index >= 0) {
            postos[index] = posto
        } else {
            postos.add(posto)
        }

        val postosJson = json.encodeToString(postos)
        prefs.edit().putString("postos", postosJson).apply()
    }

    fun listarPostos(): List<Posto> {
        val postosJson = prefs.getString("postos", null) ?: return emptyList()
        return try {
            json.decodeFromString<List<Posto>>(postosJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun obterPosto(id: String): Posto? {
        return listarPostos().find { it.id == id }
    }

    fun excluirPosto(id: String) {
        val postos = listarPostos().filter { it.id != id }
        val postosJson = json.encodeToString(postos)
        prefs.edit().putString("postos", postosJson).apply()
    }

    fun gerarNovoId(): String = UUID.randomUUID().toString()
}