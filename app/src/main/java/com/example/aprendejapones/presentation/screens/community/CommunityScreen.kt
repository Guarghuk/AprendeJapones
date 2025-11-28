package com.example.aprendejapones.presentation.screens.community

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aprendejapones.domain.model.FirestoreComment
import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.presentation.components.cards.PostCard
import com.example.aprendejapones.presentation.theme.*
import com.example.aprendejapones.utils.TimeUtils

/**
 * Pantalla de Comunidad - Mejorada
 */
@Composable
fun CommunityScreen(
    onNavigateToNewPost: () -> Unit = {},
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Manejar efectos
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CommunityEffect.NavigateToNewPost -> onNavigateToNewPost()
                is CommunityEffect.NavigateToPostDetail -> {
                    // Handled internally now
                }
                is CommunityEffect.ShowToast -> {
                    // TODO: Show snackbar
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

    Box(modifier = Modifier.fillMaxSize()) {
        CommunityContent(
            state = state,
            onEvent = viewModel::onEvent,
            onNavigateToNewPost = onNavigateToNewPost
        )

        // Post detail overlay
        AnimatedVisibility(
            visible = state.showPostDetail,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            state.selectedPost?.let { post ->
                PostDetailOverlay(
                    post = post,
                    comments = state.selectedPostComments,
                    isLoadingComments = state.isLoadingComments,
                    newCommentText = state.newCommentText,
                    isSaved = state.savedPostIds.contains(post.id),
                    onClose = { viewModel.onEvent(CommunityEvent.ClosePostDetail) },
                    onCommentTextChange = { viewModel.onEvent(CommunityEvent.UpdateNewCommentText(it)) },
                    onSendComment = { viewModel.onEvent(CommunityEvent.AddComment(post.id)) },
                    onLike = { viewModel.onEvent(CommunityEvent.LikePost(post.id)) },
                    onSave = { viewModel.onEvent(CommunityEvent.ToggleSavePost(post.id)) }
                )
            }
        }
    }
}

@Composable
private fun CommunityContent(
    state: CommunityState,
    onEvent: (CommunityEvent) -> Unit,
    onNavigateToNewPost: () -> Unit
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
    ) {
        // Header mejorado
        CommunityHeader(
            filterMode = state.filterMode,
            onFilterChange = { onEvent(CommunityEvent.SetFilterMode(it)) },
            onNewPost = onNavigateToNewPost
        )

        // Posts list
        val displayPosts = when (state.filterMode) {
            PostFilterMode.ALL_POSTS -> state.posts
            PostFilterMode.SAVED_POSTS -> state.savedPosts
        }

        if (displayPosts.isEmpty()) {
            EmptyPostsMessage(filterMode = state.filterMode)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(displayPosts, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        isSaved = state.savedPostIds.contains(post.id),
                        onLike = { postId -> onEvent(CommunityEvent.LikePost(postId)) },
                        onSave = { postId -> onEvent(CommunityEvent.ToggleSavePost(postId)) },
                        onCommentClick = { onEvent(CommunityEvent.SelectPost(it)) }
                    )
                }
                
                // Bottom spacing for navigation bar
                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}

@Composable
private fun CommunityHeader(
    filterMode: PostFilterMode,
    onFilterChange: (PostFilterMode) -> Unit,
    onNewPost: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceWhite,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "💬 Comunidad",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Comparte y aprende juntos",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // New post button
                Button(
                    onClick = onNewPost,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "+ Publicar",
                        color = SurfaceWhite,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterTab(
                    text = "📋 Todos",
                    isSelected = filterMode == PostFilterMode.ALL_POSTS,
                    onClick = { onFilterChange(PostFilterMode.ALL_POSTS) }
                )
                FilterTab(
                    text = "📌 Guardados",
                    isSelected = filterMode == PostFilterMode.SAVED_POSTS,
                    onClick = { onFilterChange(PostFilterMode.SAVED_POSTS) }
                )
            }
        }
    }
}

@Composable
private fun FilterTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) PrimaryGreen else SurfaceGray)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) SurfaceWhite else TextSecondary
        )
    }
}

@Composable
private fun EmptyPostsMessage(filterMode: PostFilterMode) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = when (filterMode) {
                    PostFilterMode.ALL_POSTS -> "📝"
                    PostFilterMode.SAVED_POSTS -> "📌"
                },
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = when (filterMode) {
                    PostFilterMode.ALL_POSTS -> "No hay publicaciones aún"
                    PostFilterMode.SAVED_POSTS -> "No tienes posts guardados"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                text = when (filterMode) {
                    PostFilterMode.ALL_POSTS -> "¡Sé el primero en publicar!"
                    PostFilterMode.SAVED_POSTS -> "Guarda posts para verlos después"
                },
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun PostDetailOverlay(
    post: FirestorePost,
    comments: List<FirestoreComment>,
    isLoadingComments: Boolean,
    newCommentText: String,
    isSaved: Boolean,
    onClose: () -> Unit,
    onCommentTextChange: (String) -> Unit,
    onSendComment: () -> Unit,
    onLike: () -> Unit,
    onSave: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SurfaceWhite
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Publicación",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = TextPrimary
                    )
                }
            }

            Divider(color = BorderLight)

            // Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Post content
                item {
                    PostDetailContent(
                        post = post,
                        isSaved = isSaved,
                        onLike = onLike,
                        onSave = onSave
                    )
                }

                // Comments header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BackgroundGray)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💬 Comentarios",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = " (${comments.size})",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Comments
                if (isLoadingComments) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = PrimaryGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                } else if (comments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "💭",
                                    fontSize = 32.sp
                                )
                                Text(
                                    text = "No hay comentarios aún",
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    text = "¡Sé el primero en comentar!",
                                    fontSize = 12.sp,
                                    color = TextTertiary
                                )
                            }
                        }
                    }
                } else {
                    items(comments) { comment ->
                        CommentItem(comment = comment)
                    }
                }

                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            // Comment input
            CommentInputBar(
                text = newCommentText,
                onTextChange = onCommentTextChange,
                onSend = onSendComment
            )
        }
    }
}

@Composable
private fun PostDetailContent(
    post: FirestorePost,
    isSaved: Boolean,
    onLike: () -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
            .padding(16.dp)
    ) {
        // Author info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .border(2.dp, PrimaryGreen, CircleShape)
                    .background(PrimaryGreenLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = post.authorName.firstOrNull()?.uppercase() ?: "?",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    fontSize = 20.sp
                )
            }

            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = post.authorName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = TimeUtils.formatRelativeTime(post.createdAt),
                        fontSize = 12.sp,
                        color = TextTertiary
                    )
                    if (post.category.isNotEmpty() && post.category != "General") {
                        Text(text = "•", fontSize = 12.sp, color = TextTertiary)
                        Text(
                            text = post.category,
                            fontSize = 12.sp,
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Content
        Text(
            text = post.content,
            fontSize = 15.sp,
            color = TextPrimary,
            lineHeight = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatItem(emoji = "👍", count = post.likesCount, label = "Me gusta", onClick = onLike)
            StatItem(emoji = "💬", count = post.commentsCount, label = "Comentarios")
            StatItem(emoji = "🔖", count = post.savesCount, label = "Guardados")
            StatItem(
                emoji = if (isSaved) "📌" else "📍",
                count = null,
                label = if (isSaved) "Guardado" else "Guardar",
                onClick = onSave,
                isHighlighted = isSaved
            )
        }
    }
}

@Composable
private fun StatItem(
    emoji: String,
    count: Int?,
    label: String,
    onClick: (() -> Unit)? = null,
    isHighlighted: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .background(if (isHighlighted) PrimaryGreenLight else SurfaceGray)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = emoji, fontSize = 16.sp)
            if (count != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$count",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isHighlighted) PrimaryGreen else TextPrimary
                )
            }
        }
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isHighlighted) PrimaryGreen else TextSecondary,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun CommentItem(comment: FirestoreComment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(PrimaryGreenLight, CircleShape)
                .border(1.dp, PrimaryGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = comment.authorName.firstOrNull()?.uppercase() ?: "?",
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen,
                fontSize = 14.sp
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = comment.authorName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = TimeUtils.formatRelativeTime(comment.createdAt),
                    fontSize = 11.sp,
                    color = TextTertiary
                )
            }
            Text(
                text = comment.content,
                fontSize = 13.sp,
                color = TextPrimary,
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun CommentInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceWhite,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text(
                        text = "Escribe un comentario...",
                        fontSize = 14.sp,
                        color = TextTertiary
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    unfocusedBorderColor = BorderGray
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onSend,
                enabled = text.isNotBlank(),
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (text.isNotBlank()) PrimaryGreen else SurfaceGray,
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviar",
                    tint = if (text.isNotBlank()) SurfaceWhite else TextTertiary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}