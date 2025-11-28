package com.example.aprendejapones.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation. clickable
import androidx.compose. foundation.layout.*
import androidx.compose.foundation. shape.CircleShape
import androidx.compose. foundation.shape.RoundedCornerShape
import androidx.compose. material. icons.Icons
import androidx.compose.material.icons.filled. Bookmark
import androidx.compose.material. icons.filled.BookmarkBorder
import androidx.compose.material. icons.filled.ChatBubbleOutline
import androidx.compose. material.icons.filled. Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose. material3.*
import androidx.compose.runtime. Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose. ui.text.font.FontWeight
import androidx.compose.ui. unit.dp
import androidx.compose. ui.unit.sp
import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.presentation.theme.*
import java.text.SimpleDateFormat
import java. util.*

/**
 * Card de post mejorado con todas las funcionalidades
 */
@Composable
fun PostCard(
    post: FirestorePost,
    isLiked: Boolean = false,
    isSaved: Boolean = false,
    onLike: () -> Unit = {},
    onComment: () -> Unit = {},
    onSave: () -> Unit = {},
    onAuthorClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier. fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults. cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16. dp)) {
            // Header con autor
            PostHeader(
                authorName = post.authorName,
                authorPhotoUrl = post.authorPhotoUrl,
                createdAt = post.createdAt,
                onAuthorClick = onAuthorClick
            )

            Spacer(modifier = Modifier. height(12.dp))

            // Contenido del post
            Text(
                text = post.content,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category badge
            CategoryBadge(category = post.category)

            Spacer(modifier = Modifier. height(12.dp))

            HorizontalDivider(color = BorderGray, thickness = 1.dp)

            Spacer(modifier = Modifier. height(8.dp))

            // Actions (Like, Comment, Save)
            PostActions(
                likesCount = post.likesCount,
                commentsCount = post.commentsCount,
                isLiked = isLiked,
                isSaved = isSaved,
                onLike = onLike,
                onComment = onComment,
                onSave = onSave
            )
        }
    }
}

@Composable
private fun PostHeader(
    authorName: String,
    authorPhotoUrl: String?,
    createdAt: Long,
    onAuthorClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onAuthorClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(PrimaryGreenLight, CircleShape),
            contentAlignment = Alignment. Center
        ) {
            Text(
                text = authorName. firstOrNull()?.toString()?.uppercase() ?: "? ",
                fontSize = 16. sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        Spacer(modifier = Modifier. width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = authorName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = formatTimestamp(createdAt),
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun CategoryBadge(category: String) {
    val (icon, color) = getCategoryIconAndColor(category)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color. copy(alpha = 0.15f),
        modifier = Modifier. border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = icon,
                fontSize = 14.sp
            )
            Text(
                text = category,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = color
            )
        }
    }
}

@Composable
private fun PostActions(
    likesCount: Int,
    commentsCount: Int,
    isLiked: Boolean,
    isSaved: Boolean,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier. fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Like button
        ActionButton(
            icon = if (isLiked) Icons. Default.Favorite else Icons.Default.FavoriteBorder,
            count = likesCount,
            tint = if (isLiked) Color.Red else TextSecondary,
            onClick = onLike
        )

        // Comment button
        ActionButton(
            icon = Icons.Default.ChatBubbleOutline,
            count = commentsCount,
            tint = TextSecondary,
            onClick = onComment
        )

        // Save button
        IconButton(onClick = onSave) {
            Icon(
                imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default. BookmarkBorder,
                contentDescription = if (isSaved) "Guardado" else "Guardar",
                tint = if (isSaved) AccentOrange else TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier. clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = count. toString(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = tint
        )
    }
}

/**
 * Formatea timestamp a formato legible
 */
private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60_000 -> "Ahora"
        diff < 3_600_000 -> "${diff / 60_000}m"
        diff < 86_400_000 -> "${diff / 3_600_000}h"
        diff < 604_800_000 -> "${diff / 86_400_000}d"
        else -> {
            val sdf = SimpleDateFormat("dd MMM", Locale("es"))
            sdf.format(Date(timestamp))
        }
    }
}

/**
 * Obtiene icono y color según categoría
 */
private fun getCategoryIconAndColor(category: String): Pair<String, Color> {
    return when (category) {
        "General" -> "💬" to Color(0xFF9E9E9E)
        "Gramática" -> "📖" to Color(0xFF2196F3)
        "Vocabulario" -> "📚" to Color(0xFF9C27B0)
        "Kanji" -> "漢" to Color(0xFFE91E63)
        "Pronunciación" -> "🎤" to Color(0xFFFF9800)
        "Cultura" -> "🎌" to Color(0xFFF44336)
        else -> "💬" to Color(0xFF9E9E9E)
    }
}