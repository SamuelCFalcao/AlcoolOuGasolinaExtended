package com.example.alcoolgasolina.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alcoolgasolina.R
import com.example.alcoolgasolina.data.Posto
import com.example.alcoolgasolina.data.PostoRepository
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesPostoScreen(
    repository: PostoRepository,
    postoId: String?,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val posto = postoId?.let { repository.obterPosto(it) }

    var nome by remember { mutableStateOf(posto?.nome ?: "") }
    var precoAlcool by remember { mutableStateOf(posto?.precoAlcool?.toString() ?: "") }
    var precoGasolina by remember { mutableStateOf(posto?.precoGasolina?.toString() ?: "") }
    var latitude by remember { mutableStateOf(posto?.latitude ?: 0.0) }
    var longitude by remember { mutableStateOf(posto?.longitude ?: 0.0) }
    var permissaoNegada by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        latitude = it.latitude
                        longitude = it.longitude
                    }
                }
            } catch (e: SecurityException) {
                permissaoNegada = true
            }
        } else {
            permissaoNegada = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (postoId == null) stringResource(R.string.novo_posto)
                        else stringResource(R.string.editar_posto)
                    )
                },
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text(stringResource(R.string.nome_posto)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

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

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.localizacao),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    if (latitude != 0.0 && longitude != 0.0) {
                        Text(
                            text = stringResource(R.string.lat_lng, latitude, longitude),
                            fontSize = 14.sp
                        )

                        Button(
                            onClick = {
                                val uri = "geo:$latitude,$longitude?q=$latitude,$longitude"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.ver_no_mapa))
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.localizacao_nao_definida),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
                        )
                    }

                    Button(
                        onClick = {
                            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.obter_localizacao))
                    }
                }
            }

            if (permissaoNegada) {
                Text(
                    text = stringResource(R.string.permissao_negada),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val precoAlcoolDouble = precoAlcool.toDoubleOrNull()
                    val precoGasolinaDouble = precoGasolina.toDoubleOrNull()

                    if (nome.isNotBlank() && precoAlcoolDouble != null &&
                        precoGasolinaDouble != null && latitude != 0.0 && longitude != 0.0) {

                        val novoPosto = Posto(
                            id = postoId ?: repository.gerarNovoId(),
                            nome = nome,
                            precoAlcool = precoAlcoolDouble,
                            precoGasolina = precoGasolinaDouble,
                            latitude = latitude,
                            longitude = longitude,
                            dataCadastro = posto?.dataCadastro ?: System.currentTimeMillis()
                        )

                        repository.salvarPosto(novoPosto)
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = nome.isNotBlank() &&
                        precoAlcool.toDoubleOrNull() != null &&
                        precoGasolina.toDoubleOrNull() != null &&
                        latitude != 0.0 && longitude != 0.0
            ) {
                Text(
                    stringResource(R.string.salvar),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}