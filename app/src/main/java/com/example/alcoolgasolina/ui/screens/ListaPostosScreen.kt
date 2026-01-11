package com.example.alcoolgasolina.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.alcoolgasolina.data.Posto
import com.example.alcoolgasolina.data.PostoRepository
import kotlinx.coroutines.launch

@Composable
fun ListaPostosScreen(repository: PostoRepository) {

    val postos by repository
        .listarPostos()
        .collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()

    Text("Postos Salvos", style = MaterialTheme.typography.titleMedium)

    LazyColumn {
        items(postos) { posto ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(posto.nome, style = MaterialTheme.typography.bodyLarge)
                        Text("Álcool: R$ ${posto.precoAlcool}")
                        Text("Gasolina: R$ ${posto.precoGasolina}")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                repository.removerPosto(posto)
                            }
                        }
                    ) {
                        Text("Excluir")
                    }
                }
            }
        }
    }
}
