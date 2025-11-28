package com.example.aprendejapones.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aprendejapones.R
import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.presentation.theme.*
import com.example.aprendejapones.utils.TimeUtils

/**
 * Card de post mejorado con todas las funcionalidades
 */
@Composable
fun PostCard(
    post: FirestorePost,
    modifier: Modifier = Modifier,
    isSaved: Boolean = false,
    onLike: (String) -> Unit = {},
    onSave: (String) -> Unit = {},
    onCommentClick: (FirestorePost) -> Unit = {},
    onProfileClick: (String) -> Unit = {}
) {
    val savedLabel = stringResource(R.string.saved_label)
    val saveLabel = stringResource(R.string.save_label)

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
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .clickable { onProfileClick(post.authorId) }
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
                            text = TimeUtils.formatRelativeTime(post.createdAt.time),
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
                    .background(BorderGray)
            )

            // Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                    ActionButton(
                        icon = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        text = post.likes.size.toString(),
                        onClick = { onLike(post.id) },
                        tint = if (post.isLiked) Color.Red else TextTertiary
                    )
                    ActionButton(
                        icon = Icons.Outlined.Comment,
                        text = post.comments.size.toString(),
                        onClick = { onCommentClick(post) }
                    )
                }
                ActionButton(
                    text = if (isSaved) savedLabel else saveLabel,
                    onClick = { onSave(post.id) }
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    onClick: () -> Unit,
    tint: Color = TextTertiary
) {
    Row(
        modifier = modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = text,
            fontSize = 12.sp,
            color = tint,
            fontWeight = FontWeight.Medium
        )
    }
}