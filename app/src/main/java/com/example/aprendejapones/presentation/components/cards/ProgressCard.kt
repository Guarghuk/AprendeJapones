package com.example.aprendejapones.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aprendejapones.domain.repository.CategoryProgress
import com.example.aprendejapones.presentation.theme.*

/**
 * Card que muestra el progreso general del usuario
 */
@Composable
fun ProgressCard(
    progressList: List<CategoryProgress>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(SurfaceWhite, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Tu Progreso",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                progressList.forEach { progress ->
                    ProgressItem(
                        category = getCategoryDisplayName(progress.category),
                        progress = progress.progress,
                        itemsLearned = progress.itemsLearned,
                        totalItems = progress.totalItems
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressItem(
    category: String,
    progress: Int,
    itemsLearned: Int,
    totalItems: Int
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category,
                fontSize = 11.sp,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$progress%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        LinearProgressIndicator(
            progress = progress / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(6.dp),
            color = PrimaryGreen,
            trackColor = BackgroundGray
        )

        Text(
            text = "$itemsLearned / $totalItems items",
            fontSize = 9.sp,
            color = TextTertiary,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

private fun getCategoryDisplayName(category: String): String {
    return when (category) {
        "hiragana" -> "あ Hiragana"
        "katakana" -> "ア Katakana"
        "kanji" -> "漢 Kanji"
        "grammar" -> "📖 Gramática"
        "vocabulary" -> "💬 Vocabulario"
        else -> category
    }
}