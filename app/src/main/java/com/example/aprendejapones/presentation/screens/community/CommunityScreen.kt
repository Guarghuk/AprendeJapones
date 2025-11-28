package com. example.aprendejapones. presentation.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation. border
import androidx.compose.foundation. layout.*
import androidx.compose. foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose. foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose. runtime.*
import androidx.compose. ui.Alignment
import androidx. compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui. unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aprendejapones.presentation.components.cards.PostCard
import com.example.aprendejapones.presentation.theme.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Pantalla de Comunidad renovada
 */
@Composable
fun CommunityScreen(
    onNavigateToNewPost: () -> Unit = {},
    onNavigateToPostDetail: (String) -> Unit = {},
    onNavigateToProfile: (String) -> Unit = {},
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Manejar efectos
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CommunityEffect.NavigateToNewPost -> onNavigateToNewPost()
                is CommunityEffect.NavigateToPostDetail -> onNavigateToPostDetail(effect. postId)
                is CommunityEffect.NavigateToUserProfile -> onNavigateToProfile(effect.userId)
                is CommunityEffect.ShowToast -> {
                    // TODO: Mostrar toast (SnackBar)
                    android.util.Log.d("CommunityScreen", "Toast: ${effect.message}")
                }
            }
        }
    }

    // Mostrar error
    state.error?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel. onEvent(CommunityEvent.DismissError) },
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
        onEvent = viewModel::onEvent,
        onNavigateToNewPost = onNavigateToNewPost,
        onNavigateToPostDetail = onNavigateToPostDetail,
        onNavigateToProfile = onNavigateToProfile
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommunityContent(
    state: CommunityState,
    onEvent: (CommunityEvent) -> Unit,
    onNavigateToNewPost: () -> Unit,
    onNavigateToPostDetail: (String) -> Unit,
    onNavigateToProfile: (String) -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing)

    if (state.isLoading && ! state.isRefreshing) {
        Box(
            modifier = Modifier. fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
        return
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { onEvent(CommunityEvent.RefreshPosts) }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Header
            item {
                CommunityHeader()
            }

            // Botón nueva publicación
            item {
                NewPostButton(
                    onClick = onNavigateToNewPost,
                    modifier = Modifier. padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Filter tabs
            item {
                CategoryFilterTabs(
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { onEvent(CommunityEvent. FilterByCategory(it)) },
                    modifier = Modifier. padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Posts
            items(
                items = state.filteredPosts,
                key = { it.id }
            ) { post ->
                PostCard(
                    post = post,
                    isLiked = post.id in state.likedPostIds,
                    isSaved = post.id in state.savedPostIds,
                    onLike = { onEvent(CommunityEvent.LikePost(post.id)) },
                    onComment = { onNavigateToPostDetail(post.id) },
                    onSave = { onEvent(CommunityEvent.SavePost(post.id)) },
                    onAuthorClick = { onNavigateToProfile(post.authorId) },
                    modifier = Modifier. padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // Empty state
            if (state.filteredPosts.isEmpty() && ! state.isLoading) {
                item {
                    EmptyState(
                        modifier = Modifier.padding(32.dp)
                    )
                }
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
        contentAlignment = Alignment. Center
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
        colors = ButtonDefaults. buttonColors(containerColor = PrimaryGreen),
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
        edgePadding = 0. dp,
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        indicator = {}
    ) {
        categories. forEach { category ->
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
                    modifier = Modifier. padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier. fillMaxWidth(),
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