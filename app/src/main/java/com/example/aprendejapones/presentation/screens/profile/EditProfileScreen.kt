package com.example.aprendejapones.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aprendejapones.presentation.theme.PrimaryGreen
import com.example.aprendejapones.presentation.theme.SurfaceWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is EditProfileEffect.SaveSuccess -> {
                    snackbarHostState.showSnackbar("Perfil actualizado")
                    onSaveSuccess()
                }
                is EditProfileEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is EditProfileEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar with edit option
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(PrimaryGreen)
                    .clickable { viewModel.onEvent(EditProfileEvent.SelectPhoto) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.username.firstOrNull()?.uppercase() ?: "U",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = SurfaceWhite
                )
                // Edit icon overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SurfaceWhite),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Cambiar foto",
                        modifier = Modifier.size(16.dp),
                        tint = PrimaryGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Username field
            OutlinedTextField(
                value = state.username,
                onValueChange = { viewModel.onEvent(EditProfileEvent.UsernameChanged(it)) },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bio field
            OutlinedTextField(
                value = state.bio,
                onValueChange = { viewModel.onEvent(EditProfileEvent.BioChanged(it)) },
                label = { Text("Biografía") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save button
            Button(
                onClick = { viewModel.onEvent(EditProfileEvent.SaveProfile) },
                enabled = !state.isSaving && state.hasChanges,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = SurfaceWhite
                    )
                } else {
                    Text("Guardar Cambios")
                }
            }

            // Error message
            state.error?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
    }
}
