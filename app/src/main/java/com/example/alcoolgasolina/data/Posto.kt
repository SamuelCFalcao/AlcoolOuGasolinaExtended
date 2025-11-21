package com.example.alcoolgasolina.data

import kotlinx.serialization.Serializable

@Serializable
data class Posto(
    val id: String,
    val nome: String,
    val precoAlcool: Double,
    val precoGasolina: Double,
    val latitude: Double,
    val longitude: Double,
    val dataCadastro: Long = System.currentTimeMillis()
)