package com.example.alcoolgasolina.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.alcoolgasolina.data.Posto
import com.example.alcoolgasolina.data.PostoRepository
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(repository: PostoRepository) {

    var nome by remember { mutableStateOf("") }
    var alcool by remember { mutableStateOf("") }
    var gasolina by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Adicionar Posto", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do Posto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = alcool,
            onValueChange = { alcool = it },
            label = { Text("Preço do Álcool") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = gasolina,
            onValueChange = { gasolina = it },
            label = { Text("Preço da Gasolina") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    repository.adicionarPosto(
                        Posto(
                            nome = nome,
                            precoAlcool = alcool.toDouble(),
                            precoGasolina = gasolina.toDouble()
                        )
                    )
                    nome = ""
                    alcool = ""
                    gasolina = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Posto")
        }

        Spacer(modifier = Modifier.height(24.dp))

        ListaPostosScreen(repository)
    }
}
