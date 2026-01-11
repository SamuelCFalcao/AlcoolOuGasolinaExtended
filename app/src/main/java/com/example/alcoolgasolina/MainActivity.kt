package com.example.alcoolgasolina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.alcoolgasolina.data.AppDatabase
import com.example.alcoolgasolina.data.PostoRepository
import com.example.alcoolgasolina.ui.HomeScreen
import com.example.alcoolgasolina.ui.theme.AlcoolGasolinaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(this)
        val repository = PostoRepository(database.postoDao())

        setContent {
            AlcoolGasolinaTheme {
                HomeScreen(repository)
            }
        }
    }
}
