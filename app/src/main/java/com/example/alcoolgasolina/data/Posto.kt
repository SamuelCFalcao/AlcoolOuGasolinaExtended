package com.example.alcoolgasolina.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "postos")
data class Posto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val precoAlcool: Double,
    val precoGasolina: Double
)
