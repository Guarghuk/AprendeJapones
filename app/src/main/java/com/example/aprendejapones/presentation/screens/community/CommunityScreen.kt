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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aprendejapones.R
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
    onNavigateToPostDetail: (String) -> Unit = {},
    onNavigateToProfile: (String) -> Unit = {},
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Manejar efectos
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CommunityEffect.NavigateToNewPost -> onNavigateToNewPost()
                is CommunityEffect.NavigateToPostDetail -> {
                    // Handled internally now
                }
                is CommunityEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration. Short
                    )
                }

                is CommunityEffect.NavigateToUserProfile -> TODO()
            }
        }
    }

    // Mostrar error
    val errorTitle = stringResource(R.string.error_title)
    val okText = stringResource(R.string.ok)
    state.error?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(CommunityEvent.DismissError) },
            title = { Text(errorTitle) },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(CommunityEvent.DismissError) }) {
                    Text(okText)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                . fillMaxSize()
                .padding(paddingValues)
        ) {
            CommunityContent(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigateToNewPost = onNavigateToNewPost,
                onNavigateToProfile = onNavigateToProfile
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
                        isLiked = state.likedPostIds.contains(post.id),
                        onClose = { viewModel.onEvent(CommunityEvent.ClosePostDetail) },
                        onCommentTextChange = { viewModel.onEvent(CommunityEvent.UpdateNewCommentText(it)) },
                        onSendComment = { viewModel.onEvent(CommunityEvent.AddComment(post.id)) },
                        onLike = { viewModel.onEvent(CommunityEvent.ToggleLikePost(post.id)) },
                        onSave = { viewModel.onEvent(CommunityEvent. ToggleSavePost(post. id)) },
                        onNavigateToProfile = onNavigateToProfile
                    )
                }
            }

            // Category selector dialog
            if (state.showCategoryDialog) {
                CategorySelectorDialog(
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { category ->
                        viewModel.onEvent(CommunityEvent.SelectCategory(category))
                    },
                    onDismiss = { viewModel.onEvent(CommunityEvent.HideCategoryDialog) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommunityContent(
    state: CommunityState,
    onEvent: (CommunityEvent) -> Unit,
    onNavigateToNewPost: () -> Unit,
    onNavigateToProfile: (String) -> Unit
) {
    if (state.isLoading && !state.isRefreshing) {
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
            selectedCategory = state.selectedCategory,
            onFilterChange = { onEvent(CommunityEvent.SetFilterMode(it)) },
            onNewPost = onNavigateToNewPost,
            onShowCategoryDialog = { onEvent(CommunityEvent.ShowCategoryDialog) }
        )

        // Posts list
        val displayPosts = when (state.filterMode) {
            PostFilterMode.ALL_POSTS -> state.posts
            PostFilterMode.SAVED_POSTS -> state.savedPosts
            PostFilterMode.BY_CATEGORY -> state.posts.filter { it.category == state.selectedCategory }
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
                        isLiked = state.likedPostIds.contains(post.id),
                        onLike = { postId -> onEvent(CommunityEvent.ToggleLikePost(postId)) },
                        onSave = { postId -> onEvent(CommunityEvent.ToggleSavePost(postId)) },
                        onCommentClick = { onEvent(CommunityEvent.SelectPost(it)) },
                        onProfileClick = onNavigateToProfile
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
    selectedCategory: String,
    onFilterChange: (PostFilterMode) -> Unit,
    onNewPost: () -> Unit,
    onShowCategoryDialog: () -> Unit
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
                        text = stringResource(R.string.community_title),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = stringResource(R.string.community_subtitle),
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
                        text = stringResource(R.string.new_post_button),
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
                    text = if (selectedCategory == "Todos") stringResource(R.string.filter_all) else selectedCategory,
                    isSelected = filterMode == PostFilterMode.ALL_POSTS || filterMode == PostFilterMode.BY_CATEGORY,
                    onClick = onShowCategoryDialog
                )
                FilterTab(
                    text = stringResource(R.string.filter_saved),
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
                    PostFilterMode.BY_CATEGORY -> "🔍"
                },
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = when (filterMode) {
                    PostFilterMode.ALL_POSTS -> stringResource(R.string.no_posts_yet)
                    PostFilterMode.SAVED_POSTS -> stringResource(R.string.no_saved_posts)
                    PostFilterMode.BY_CATEGORY -> stringResource(R.string.no_posts_yet)
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                text = when (filterMode) {
                    PostFilterMode.ALL_POSTS -> stringResource(R.string.be_first_to_post)
                    PostFilterMode.SAVED_POSTS -> stringResource(R.string.save_posts_for_later)
                    PostFilterMode.BY_CATEGORY -> stringResource(R.string.be_first_to_post)
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
    isLiked: Boolean,
    onClose: () -> Unit,
    onCommentTextChange: (String) -> Unit,
    onSendComment: () -> Unit,
    onLike: () -> Unit,
    onSave: () -> Unit,
    onNavigateToProfile: (String) -> Unit = {}
) {
    val closeContentDescription = stringResource(R.string.close)

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
                    text = stringResource(R.string.publication_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = closeContentDescription,
                        tint = TextPrimary
                    )
                }
            }

            HorizontalDivider(color = BorderLight)

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
                        isLiked = isLiked,
                        onLike = onLike,
                        onSave = onSave,
                        onNavigateToProfile = onNavigateToProfile
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
                            text = stringResource(R.string.comments_title),
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
                                    text = stringResource(R.string.no_comments_yet),
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    text = stringResource(R.string.be_first_to_comment),
                                    fontSize = 12.sp,
                                    color = TextTertiary
                                )
                            }
                        }
                    }
                } else {
                    items(comments) { comment ->
                        CommentItem(
                            comment = comment,
                            onNavigateToProfile = onNavigateToProfile
                        )
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
    isLiked: Boolean,
    onLike: () -> Unit,
    onSave: () -> Unit,
    onNavigateToProfile: (String) -> Unit = {}
) {
    val likesLabel = stringResource(R.string.likes_label)
    val commentsLabel = stringResource(R.string.comments_label)
    val savesLabel = stringResource(R.string.saves_label)
    val savedLabel = stringResource(R.string.saved_label)
    val saveLabel = stringResource(R.string.save_label)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
            .padding(16.dp)
    ) {
        // Author info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .clickable { onNavigateToProfile(post.authorId) }
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
            StatItem(
                emoji = if (isLiked) "❤️" else "👍",
                count = post.likesCount,
                label = likesLabel,
                onClick = onLike,
                isHighlighted = isLiked
            )
            StatItem(emoji = "💬", count = post.commentsCount, label = commentsLabel)
            StatItem(emoji = "🔖", count = post.savesCount, label = savesLabel)
            StatItem(
                emoji = if (isSaved) "📌" else "📍",
                count = null,
                label = if (isSaved) savedLabel else saveLabel,
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
private fun CommentItem(
    comment: FirestoreComment,
    onNavigateToProfile: (String) -> Unit = {}
) {
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
                .border(1.dp, PrimaryGreen, CircleShape)
                .clickable { onNavigateToProfile(comment.authorId) },
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
                    color = TextPrimary,
                    modifier = Modifier.clickable { onNavigateToProfile(comment.authorId) }
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
    val sendContentDescription = stringResource(R.string.send)
    val placeholderText = stringResource(R.string.comment_placeholder)

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
                        text = placeholderText,
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
                    contentDescription = sendContentDescription,
                    tint = if (text.isNotBlank()) SurfaceWhite else TextTertiary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun CommunityHeader() {
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
}

@Composable
private fun NewPostButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
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
}

@Composable
private fun CategoryFilterTabs(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "Todos",
        "General",
        "Gramática",
        "Vocabulario",
        "Kanji",
        "Pronunciación",
        "Cultura"
    )

    ScrollableTabRow(
        selectedTabIndex = categories.indexOf(selectedCategory),
        modifier = modifier,
        edgePadding = 0.dp,
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        indicator = {}
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Tab(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) PrimaryGreen else BorderGray,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(
                        color = if (isSelected) PrimaryGreenLight else SurfaceWhite,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Text(
                    text = category,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) PrimaryGreen else TextSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📭", fontSize = 64.sp)
        Text(
            text = "No hay publicaciones",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Sé el primero en compartir algo",
            fontSize = 12.sp,
            color = TextTertiary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun CategorySelectorDialog(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Seleccionar Categoría",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val categories = listOf(
                    "Todos" to "📚",
                    "General" to "💬",
                    "Gramática" to "📖",
                    "Vocabulario" to "📚",
                    "Kanji" to "漢",
                    "Pronunciación" to "🎤",
                    "Cultura" to "🎌"
                )
                
                categories.forEach { (category, icon) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (selectedCategory == category) PrimaryGreenLight else androidx.compose.ui.graphics.Color.Transparent
                            )
                            .clickable { onCategorySelected(category) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = icon,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = category,
                            fontSize = 14.sp,
                            fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedCategory == category) PrimaryGreen else TextPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = PrimaryGreen)
            }
        }
    )
}