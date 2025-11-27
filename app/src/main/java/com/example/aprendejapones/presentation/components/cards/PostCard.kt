package com.example.aprendejapones.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Card de publicación en comunidad
 */
@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    post: FirestorePost,
    onLike: (String) -> Unit = {},
    onSave: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(SurfaceWhite, RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Column {
            // Header del post
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(2.dp, PrimaryGreen, RoundedCornerShape(20.dp))
                        .background(PrimaryGreenLight, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.authorName.firstOrNull()?.toString() ?: "?",
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen,
                        fontSize = 16.sp
                    )
                }

                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = post.authorName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = formatTimestamp(post.createdAt),
                        fontSize = 10.sp,
                        color = TextTertiary
                    )
                }
            }

            // Contenido
            Text(
                text = post.content,
                fontSize = 12.sp,
                color = TextPrimary,
                lineHeight = 16.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Acciones
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "💬 ${post.commentsCount} respuestas",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                Text(
                    text = "👍 ${post.likesCount}",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    modifier = Modifier.clickable { onLike(post.id) }
                )
                Text(
                    text = "📌 Guardar",
                    fontSize = 10.sp,
                    color = PrimaryGreen,
                    modifier = Modifier.clickable { onSave(post.id) }
                )
            }
        }
    }
}

/**
 * Format timestamp to readable string
 */
private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Hace un momento"
        diff < 3600000 -> "Hace ${diff / 60000} min"
        diff < 86400000 -> "Hace ${diff / 3600000}h"
        else -> {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.format(Date(timestamp))
        }
    }
}