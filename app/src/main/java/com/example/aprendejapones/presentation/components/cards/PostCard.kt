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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.presentation.theme.*
import com.example.aprendejapones.utils.TimeUtils

/**
 * Card de post mejorado con todas las funcionalidades
 */
@Composable
fun PostCard(
    post: FirestorePost,
    isSaved: Boolean = false,
    onLike: (String) -> Unit = {},
    onSave: (String) -> Unit = {},
    onCommentClick: (FirestorePost) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
            .background(SurfaceWhite, RoundedCornerShape(12.dp))
            .clickable { onCommentClick(post) }
            .padding(16.dp)
    ) {
        Column {
            // Header del post
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .border(2.dp, PrimaryGreen, RoundedCornerShape(22.dp))
                        .background(PrimaryGreenLight, RoundedCornerShape(22.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.authorName.firstOrNull()?.uppercase() ?: "?",
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen,
                        fontSize = 18.sp
                    )
                }

                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = post.authorName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = TimeUtils.formatRelativeTime(post.createdAt),
                            fontSize = 11.sp,
                            color = TextTertiary
                        )
                        if (post.category.isNotEmpty() && post.category != "General") {
                            Text(
                                text = "•",
                                fontSize = 11.sp,
                                color = TextTertiary
                            )
                            Text(
                                text = post.category,
                                fontSize = 11.sp,
                                color = PrimaryGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Contenido
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BorderLight)
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

            // Acciones
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                // Comments
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onCommentClick(post) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "💬",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.commentsCount}",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Likes
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onLike(post.id) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "👍",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.likesCount}",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Saves count display
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "🔖",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.savesCount}",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Save button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSaved) PrimaryGreenLight else SurfaceGray)
                        .clickable { onSave(post.id) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isSaved) "📌" else "📍",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isSaved) "Guardado" else "Guardar",
                        fontSize = 11.sp,
                        color = if (isSaved) PrimaryGreen else TextSecondary,
                        fontWeight = if (isSaved) FontWeight.SemiBold else FontWeight.Medium
                    )
                }
            }
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