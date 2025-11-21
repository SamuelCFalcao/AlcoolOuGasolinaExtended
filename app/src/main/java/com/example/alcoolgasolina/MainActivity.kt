package com.example.alcoolgasolina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.remember
import androidx.navigation.compose.rememberNavController
import com.example.alcoolgasolina.data.PostoRepository
import com.example.alcoolgasolina.ui.screens.CalculadoraScreen
import com.example.alcoolgasolina.ui.screens.ListaPostosScreen
import com.example.alcoolgasolina.ui.screens.DetalhesPostoScreen
import com.example.alcoolgasolina.ui.theme.AlcoolGasolinaTheme

class MainActivity : ComponentActivity() {
    private lateinit var repository: PostoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = PostoRepository(applicationContext)

        setContent {
            AlcoolGasolinaTheme {
                val navController = rememberNavController()

                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "calculadora"
                    ) {
                        composable("calculadora") {
                            CalculadoraScreen(
                                repository = repository,
                                onNavigateToLista = { navController.navigate("lista") }
                            )
                        }

                        composable("lista") {
                            ListaPostosScreen(
                                repository = repository,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToDetalhes = { postoId ->
                                    navController.navigate("detalhes/$postoId")
                                },
                                onNavigateToNovo = { navController.navigate("detalhes/novo") }
                            )
                        }

                        composable("detalhes/{postoId}") { backStackEntry ->
                            val postoId = backStackEntry.arguments?.getString("postoId")
                            DetalhesPostoScreen(
                                repository = repository,
                                postoId = postoId?.takeIf { it != "novo" },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
