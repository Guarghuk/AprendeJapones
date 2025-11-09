package com.example.aprendejapones.presentation.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aprendejapones.presentation.components.cards.PostCard
import com.example.aprendejapones.presentation.theme.*

/**
 * Pantalla de Comunidad
 */
@Composable
fun CommunityScreen(
    onNavigateToNewPost: () -> Unit = {},
    viewModel: CommunityViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    // Manejar efectos
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CommunityEffect.NavigateToNewPost -> onNavigateToNewPost()
                is CommunityEffect.NavigateToPostDetail -> {
                    // TODO: Navegar al detalle
                }
                is CommunityEffect.ShowToast -> {
                    // TODO: Mostrar toast
                }
            }
        }
    }

    // Mostrar error
    state.error?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(CommunityEvent.DismissError) },
            title = { Text("Error") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(CommunityEvent.DismissError) }) {
                    Text("OK")
                }
            }
        )
    }

    CommunityContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun CommunityContent(
    state: CommunityState,
    onEvent: (CommunityEvent) -> Unit
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceWhite)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "💬 Comunidad",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Comparte y aprende juntos",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Content
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Botón nueva publicación
            Button(
                onClick = { onEvent(CommunityEvent.CreateNewPost) },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, PrimaryGreen, RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(14.dp)
            ) {
                Text(
                    text = "+ Nueva Publicación",
                    color = SurfaceWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Posts
            state.posts.forEach { post ->
                PostCard(
                    post = post,
                    onLike = { postId -> onEvent(CommunityEvent.LikePost(postId)) },
                    onSave = { postId -> onEvent(CommunityEvent.SavePost(postId)) }
                )
            }
        }
    }
}