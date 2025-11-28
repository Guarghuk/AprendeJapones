package com.example.aprendejapones.presentation.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aprendejapones.presentation.theme.*

@Composable
fun ShopScreen(
    onBack: () -> Unit,
    viewModel: ShopViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ShopEffect.ShowToast -> {
                    // Toast handled by snackbar below
                }
                is ShopEffect.PurchaseSuccess -> {
                    // Success handled
                }
                is ShopEffect.PurchaseFailed -> {
                    // Failure handled
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
        ShopHeader(
            userCoins = state.userCoins,
            streakShields = state.streakShieldsOwned,
            onBack = onBack
        )

        // Content
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Streak Shields Section
                item {
                    Text(
                        text = "🛡️ Protección",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(
                    items = state.items.filter { it.type == ShopItemType.STREAK_SHIELD },
                    key = { it.id }
                ) { item ->
                    ShopItemCard(
                        item = item,
                        userCoins = state.userCoins,
                        streakShieldsOwned = state.streakShieldsOwned,
                        onPurchase = { viewModel.onEvent(ShopEvent.PurchaseItem(item.id)) }
                    )
                }

                // Badges Section
                item {
                    Text(
                        text = "🏅 Insignias",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                items(
                    items = state.items.filter { it.type == ShopItemType.BADGE },
                    key = { it.id }
                ) { item ->
                    ShopItemCard(
                        item = item,
                        userCoins = state.userCoins,
                        onPurchase = { viewModel.onEvent(ShopEvent.PurchaseItem(item.id)) }
                    )
                }

                // Avatars Section
                item {
                    Text(
                        text = "👤 Avatares",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                items(
                    items = state.items.filter { it.type == ShopItemType.AVATAR },
                    key = { it.id }
                ) { item ->
                    ShopItemCard(
                        item = item,
                        userCoins = state.userCoins,
                        onPurchase = { viewModel.onEvent(ShopEvent.PurchaseItem(item.id)) }
                    )
                }

                // Themes Section
                item {
                    Text(
                        text = "🎨 Temas",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                items(
                    items = state.items.filter { it.type == ShopItemType.THEME },
                    key = { it.id }
                ) { item ->
                    ShopItemCard(
                        item = item,
                        userCoins = state.userCoins,
                        onPurchase = { viewModel.onEvent(ShopEvent.PurchaseItem(item.id)) }
                    )
                }

                // Spacer at bottom
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun ShopHeader(
    userCoins: Int,
    streakShields: Int,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
    ) {
        // Title bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text(
                    text = "← Volver",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "🛍️ Tienda",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.width(60.dp))
        }

        // User coins display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .border(2.dp, AccentBlue.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .background(AccentBlue.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "💧 $userCoins",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentBlue
                    )
                    Text(
                        text = "Monedas disponibles",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(BorderGray)
                )
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🛡️ $streakShields",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                    Text(
                        text = "Escudos de racha",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun ShopItemCard(
    item: ShopItem,
    userCoins: Int,
    streakShieldsOwned: Int = 0,
    onPurchase: () -> Unit
) {
    val canAfford = userCoins >= item.price
    val isOwned = item.isPurchased
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                2.dp,
                if (isOwned) SuccessGreen.copy(alpha = 0.5f) else BorderGray,
                RoundedCornerShape(10.dp)
            )
            .background(
                if (isOwned) SuccessGreen.copy(alpha = 0.05f) else SurfaceWhite,
                RoundedCornerShape(10.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .border(2.dp, BorderGray, RoundedCornerShape(28.dp))
                    .background(BackgroundGray, RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.icon,
                    fontSize = 28.sp
                )
            }

            // Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = item.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = item.description,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
                
                // Show owned count for streak shields
                if (item.type == ShopItemType.STREAK_SHIELD && streakShieldsOwned > 0) {
                    Text(
                        text = "Tienes: $streakShieldsOwned",
                        fontSize = 10.sp,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Price/Purchase button
            if (isOwned && item.type != ShopItemType.STREAK_SHIELD) {
                Box(
                    modifier = Modifier
                        .border(2.dp, SuccessGreen, RoundedCornerShape(8.dp))
                        .background(SuccessGreen.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "✓ Comprado",
                        fontSize = 11.sp,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .border(
                            2.dp,
                            if (canAfford) PrimaryGreen else ErrorRed.copy(alpha = 0.5f),
                            RoundedCornerShape(8.dp)
                        )
                        .background(
                            if (canAfford) PrimaryGreen else ErrorRed.copy(alpha = 0.1f),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable(enabled = canAfford) { onPurchase() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "💧 ${item.price}",
                        fontSize = 12.sp,
                        color = if (canAfford) SurfaceWhite else ErrorRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
