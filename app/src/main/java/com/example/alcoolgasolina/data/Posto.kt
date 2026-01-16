package com.example.alcoolgasolina.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "postos")
data class Posto(
    @PrimaryKey
    val id: String,
    val nome: String,
    val precoAlcool: Double,
    val precoGasolina: Double,
    val latitude: Double,
    val longitude: Double,
    val dataCadastro: Long = System.currentTimeMillis()
)