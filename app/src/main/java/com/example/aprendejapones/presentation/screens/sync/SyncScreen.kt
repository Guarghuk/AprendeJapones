package com.example.aprendejapones.presentation.screens.sync

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun SyncScreen(
    onBack: () -> Unit,
    viewModel: SyncViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Sincronizar Datos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Información
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📱 Datos locales: ${state.localDataCount} registros")
                Text("☁️ Datos en la nube: ${state.cloudDataCount} registros")

                if (state.lastSyncTime != null) {
                    Text("Última sincronización: ${state.lastSyncTime}")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de sincronización
        Button(
            onClick = { viewModel.onEvent(SyncEvent.StartSync) },
            enabled = !state.isSyncing,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isSyncing) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Sincronizar Ahora")
            }
        }

        // Progreso
        if (state.isSyncing) {
            LinearProgressIndicator(
                progress = { state.syncProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            Text(
                text = "${(state.syncProgress * 100).toInt()}% completado",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}