// ui/screens/CalculadoraScreen.kt
package com.example.alcoolgasolina.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alcoolgasolina.R
import com.example.alcoolgasolina.data.PostoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculadoraScreen(
    repository: PostoRepository,
    onNavigateToLista: () -> Unit
) {
    var precoAlcool by remember { mutableStateOf("") }
    var precoGasolina by remember { mutableStateOf("") }
    var usar75Porcento by remember { mutableStateOf(repository.carregarPercentualSwitch()) }

    // Salva automaticamente quando o switch muda
    LaunchedEffect(usar75Porcento) {
        repository.salvarPercentualSwitch(usar75Porcento)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onNavigateToLista) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = stringResource(R.string.lista_postos))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "⛽ ${stringResource(R.string.titulo_calculadora)}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Card para inputs
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = precoAlcool,
                        onValueChange = { precoAlcool = it },
                        label = { Text(stringResource(R.string.preco_alcool)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = precoGasolina,
                        onValueChange = { precoGasolina = it },
                        label = { Text(stringResource(R.string.preco_gasolina)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            // Switch para escolher percentual
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.percentual_calculo),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = if (usar75Porcento)
                                stringResource(R.string.percentual_75)
                            else
                                stringResource(R.string.percentual_70),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }

                    Switch(
                        checked = usar75Porcento,
                        onCheckedChange = { usar75Porcento = it }
                    )
                }
            }

            Button(
                onClick = { /* Cálculo é automático */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = precoAlcool.isNotEmpty() && precoGasolina.isNotEmpty()
            ) {
                Text(
                    stringResource(R.string.calcular),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Resultado
            val resultado = calcularMelhorOpcao(
                precoAlcool.toDoubleOrNull(),
                precoGasolina.toDoubleOrNull(),
                usar75Porcento
            )

            if (resultado.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            resultado.contains(stringResource(R.string.use_alcool)) ->
                                MaterialTheme.colorScheme.primaryContainer
                            resultado.contains(stringResource(R.string.use_gasolina)) ->
                                MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.errorContainer
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.recomendacao),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = resultado,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun calcularMelhorOpcao(
    precoAlcool: Double?,
    precoGasolina: Double?,
    usar75: Boolean
): String {
    if (precoAlcool == null || precoGasolina == null) return ""
    if (precoAlcool <= 0 || precoGasolina <= 0) return stringResource(R.string.valores_invalidos)

    val percentual = if (usar75) 0.75 else 0.70
    val limiteAlcool = precoGasolina * percentual

    return if (precoAlcool <= limiteAlcool) {
        stringResource(R.string.use_alcool)
    } else {
        stringResource(R.string.use_gasolina)
    }
}