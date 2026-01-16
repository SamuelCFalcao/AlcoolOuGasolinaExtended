package com.example.alcoolgasolina.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alcoolgasolina.R
import com.example.alcoolgasolina.data.Posto
import com.example.alcoolgasolina.data.PostoRepository
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaPostosScreen(
    repository: PostoRepository,
    onNavigateBack: () -> Unit,
    onNavigateToDetalhes: (String) -> Unit,
    onNavigateToNovo: () -> Unit
) {
    var postos by remember { mutableStateOf(repository.listarPostos()) }
    var postoParaExcluir by remember { mutableStateOf<Posto?>(null) }

    LaunchedEffect(Unit) {
        postos = repository.listarPostos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.lista_postos)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.voltar))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToNovo,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.adicionar_posto))
            }
        }
    ) { padding ->
        if (postos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.nenhum_posto),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(postos, key = { it.id }) { posto ->
                    PostoCard(
                        posto = posto,
                        onClick = { onNavigateToDetalhes(posto.id) },
                        onDelete = { postoParaExcluir = posto }
                    )
                }
            }
        }
    }

    postoParaExcluir?.let { posto ->
        AlertDialog(
            onDismissRequest = { postoParaExcluir = null },
            title = { Text(stringResource(R.string.confirmar_exclusao)) },
            text = { Text(stringResource(R.string.msg_confirmar_exclusao, posto.nome)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        repository.excluirPosto(posto.id)
                        postos = repository.listarPostos()
                        postoParaExcluir = null
                    }
                ) {
                    Text(stringResource(R.string.excluir))
                }
            },
            dismissButton = {
                TextButton(onClick = { postoParaExcluir = null }) {
                    Text(stringResource(R.string.cancelar))
                }
            }
        )
    }
}

@Composable
fun PostoCard(
    posto: Posto,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = posto.nome,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${stringResource(R.string.alcool)}: R$ ${"%.2f".format(posto.precoAlcool)}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Text(
                    text = "${stringResource(R.string.gasolina)}: R$ ${"%.2f".format(posto.precoGasolina)}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatarData(posto.dataCadastro),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.excluir),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun formatarData(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}