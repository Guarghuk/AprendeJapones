package com.example.aprendejapones.presentation.screens.shop

/**
 * Data class for shop items
 */
data class ShopItem(
    val id: String,
    val icon: String,
    val name: String,
    val description: String,
    val price: Int,
    val type: ShopItemType,
    val isPurchased: Boolean = false
)

enum class ShopItemType {
    STREAK_SHIELD,
    BADGE,
    AVATAR,
    THEME
}

/**
 * Estado de ShopScreen
 */
data class ShopState(
    val userCoins: Int = 0,
    val items: List<ShopItem> = emptyList(),
    val purchasedItems: Set<String> = emptySet(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val streakShieldsOwned: Int = 0
)

/**
 * Eventos de Shop
 */
sealed class ShopEvent {
    data class PurchaseItem(val itemId: String) : ShopEvent()
    object LoadData : ShopEvent()
    object DismissError : ShopEvent()
}

/**
 * Efectos secundarios
 */
sealed class ShopEffect {
    data class ShowToast(val message: String) : ShopEffect()
    data class PurchaseSuccess(val itemName: String) : ShopEffect()
    data class PurchaseFailed(val reason: String) : ShopEffect()
}
