package com.example.aprendejapones.presentation.screens.newpost

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.aprendejapones.presentation.theme.*

@Composable
fun NewPostScreen(
    onBack: () -> Unit,
    viewModel: NewPostViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is NewPostEffect.NavigateBack -> onBack()
                is NewPostEffect.ShowToast -> {
                    // TODO: Implement toast
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        // Header
        NewPostHeader(
            onBack = onBack,
            onPublish = { viewModel.onEvent(NewPostEvent.PublishPost) },
            canPublish = state.content.isNotBlank()
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Category selector
            CategorySelector(
                selectedCategory = state.selectedCategory,
                onCategorySelected = { category ->
                    viewModel.onEvent(NewPostEvent.SelectCategory(category))
                }
            )

            // Content input
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
                    .background(SurfaceWhite, RoundedCornerShape(10.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "¿Qué quieres compartir?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    TextField(
                        value = state.content,
                        onValueChange = { viewModel.onEvent(NewPostEvent.UpdateContent(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp),
                        placeholder = {
                            Text(
                                "Escribe tu pregunta, duda o comentario aquí...",
                                fontSize = 12.sp,
                                color = TextTertiary
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = SurfaceWhite,
                            unfocusedContainerColor = SurfaceWhite,
                            disabledContainerColor = SurfaceWhite,
                            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    )

                    Text(
                        text = "${state.content.length}/500 caracteres",
                        fontSize = 10.sp,
                        color = TextTertiary,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 4.dp)
                    )
                }
            }

            // Tips card
            TipsCard()
        }
    }
}

@Composable
private fun NewPostHeader(
    onBack: () -> Unit,
    onPublish: () -> Unit,
    canPublish: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text(
                    text = "✕ Cancelar",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Nueva Publicación",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            TextButton(
                onClick = onPublish,
                enabled = canPublish
            ) {
                Text(
                    text = "Publicar",
                    fontSize = 12.sp,
                    color = if (canPublish) PrimaryGreen else TextTertiary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CategorySelector(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf(
        "General" to "💬",
        "Gramática" to "📖",
        "Vocabulario" to "📚",
        "Kanji" to "漢",
        "Pronunciación" to "🎤",
        "Cultura" to "🎌"
    )

    Column {
        Text(
            text = "Categoría",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.take(3).forEach { (category, icon) ->
                CategoryChip(
                    category = category,
                    icon = icon,
                    isSelected = selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.drop(3).forEach { (category, icon) ->
                CategoryChip(
                    category = category,
                    icon = icon,
                    isSelected = selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    category: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) PrimaryGreen else BorderGray
    val backgroundColor = if (isSelected) PrimaryGreenLight else SurfaceWhite

    Box(
        modifier = modifier
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.widthIn(max = 70.dp) // Limita máximo ancho
        ) {
            Text(
                icon,
                fontSize = if (icon == "漢") 16.sp else 20.sp // Ajusta Kanji
            )
            Text(
                text = category,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) PrimaryGreen else TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
@Composable
private fun TipsCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, AccentOrange.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .background(AccentOrangeLight, RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💡", fontSize = 20.sp)
                Text(
                    text = "Consejos para publicar",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentOrangeDark,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Column(
                modifier = Modifier.padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TipItem("Sé claro y específico en tu pregunta")
                TipItem("Usa la categoría correcta")
                TipItem("Sé respetuoso con otros usuarios")
                TipItem("Revisa si tu duda ya fue respondida")
            }
        }
    }
}

@Composable
private fun TipItem(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text(
            text = "•",
            fontSize = 11.sp,
            color = TextSecondary,
            modifier = Modifier.padding(end = 6.dp)
        )
        Text(
            text = text,
            fontSize = 11.sp,
            color = TextSecondary,
            lineHeight = 15.sp
        )
    }
}