package com.example.aprendejapones.presentation.screens.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.data.local.preferences.PreferencesManager
import com.example.aprendejapones.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

/**
 * ViewModel para ShopScreen
 */
@HiltViewModel
class ShopViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    companion object {
        private val PURCHASED_ITEMS = stringSetPreferencesKey("purchased_items")
        private val STREAK_SHIELDS_OWNED = intPreferencesKey("streak_shields_owned")
    }

    private val _state = MutableStateFlow(ShopState())
    val state: StateFlow<ShopState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<ShopEffect>()
    val effects: SharedFlow<ShopEffect> = _effects.asSharedFlow()

    init {
        loadData()
    }

    fun onEvent(event: ShopEvent) {
        when (event) {
            is ShopEvent.PurchaseItem -> purchaseItem(event.itemId)
            is ShopEvent.LoadData -> loadData()
            is ShopEvent.DismissError -> dismissError()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                // Load user coins
                val user = userRepository.getCurrentUser()
                val userCoins = user?.drops ?: 0

                // Load purchased items from preferences
                val prefs = preferencesManager.dataStore.data.first()
                val purchasedItems = prefs[PURCHASED_ITEMS] ?: emptySet()
                val streakShields = prefs[STREAK_SHIELDS_OWNED] ?: 0

                // Create shop items
                val items = createShopItems(purchasedItems)

                _state.update {
                    it.copy(
                        userCoins = userCoins,
                        items = items,
                        purchasedItems = purchasedItems,
                        streakShieldsOwned = streakShields,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error cargando la tienda: ${e.message}"
                    )
                }
            }
        }
    }

    private fun createShopItems(purchasedItems: Set<String>): List<ShopItem> {
        return listOf(
            // Streak Shields (consumable - can buy multiple)
            ShopItem(
                id = "streak_shield",
                icon = "🛡️",
                name = "Escudo de Racha",
                description = "Protege tu racha por 1 día si no puedes estudiar",
                price = 50,
                type = ShopItemType.STREAK_SHIELD,
                isPurchased = false // Always available to purchase
            ),
            
            // Badges
            ShopItem(
                id = "badge_sakura",
                icon = "🌸",
                name = "Insignia Sakura",
                description = "Muestra tu amor por Japón con esta hermosa insignia",
                price = 25,
                type = ShopItemType.BADGE,
                isPurchased = purchasedItems.contains("badge_sakura")
            ),
            ShopItem(
                id = "badge_samurai",
                icon = "⚔️",
                name = "Insignia Samurai",
                description = "Para los guerreros dedicados del estudio",
                price = 40,
                type = ShopItemType.BADGE,
                isPurchased = purchasedItems.contains("badge_samurai")
            ),
            ShopItem(
                id = "badge_dragon",
                icon = "🐉",
                name = "Insignia Dragón",
                description = "Símbolo de poder y sabiduría",
                price = 75,
                type = ShopItemType.BADGE,
                isPurchased = purchasedItems.contains("badge_dragon")
            ),
            ShopItem(
                id = "badge_sensei",
                icon = "👨‍🏫",
                name = "Insignia Sensei",
                description = "Para los maestros del japonés",
                price = 100,
                type = ShopItemType.BADGE,
                isPurchased = purchasedItems.contains("badge_sensei")
            ),
            
            // Avatars
            ShopItem(
                id = "avatar_kitsune",
                icon = "🦊",
                name = "Avatar Kitsune",
                description = "El zorro místico de las leyendas japonesas",
                price = 30,
                type = ShopItemType.AVATAR,
                isPurchased = purchasedItems.contains("avatar_kitsune")
            ),
            ShopItem(
                id = "avatar_tanuki",
                icon = "🦝",
                name = "Avatar Tanuki",
                description = "El travieso mapache de los cuentos",
                price = 30,
                type = ShopItemType.AVATAR,
                isPurchased = purchasedItems.contains("avatar_tanuki")
            ),
            ShopItem(
                id = "avatar_daruma",
                icon = "🎎",
                name = "Avatar Daruma",
                description = "Símbolo de perseverancia y buena suerte",
                price = 45,
                type = ShopItemType.AVATAR,
                isPurchased = purchasedItems.contains("avatar_daruma")
            ),
            
            // Themes (special)
            ShopItem(
                id = "theme_night",
                icon = "🌙",
                name = "Tema Noche",
                description = "Un tema oscuro elegante para estudiar de noche",
                price = 60,
                type = ShopItemType.THEME,
                isPurchased = purchasedItems.contains("theme_night")
            ),
            ShopItem(
                id = "theme_zen",
                icon = "🎍",
                name = "Tema Zen",
                description = "Colores tranquilos inspirados en jardines japoneses",
                price = 80,
                type = ShopItemType.THEME,
                isPurchased = purchasedItems.contains("theme_zen")
            )
        )
    }

    private fun purchaseItem(itemId: String) {
        viewModelScope.launch {
            val currentState = _state.value
            val item = currentState.items.find { it.id == itemId } ?: return@launch

            // Check if already purchased (except streak shields)
            if (item.isPurchased && item.type != ShopItemType.STREAK_SHIELD) {
                _effects.emit(ShopEffect.PurchaseFailed("Ya tienes este artículo"))
                return@launch
            }

            // Check if enough coins
            if (currentState.userCoins < item.price) {
                _effects.emit(ShopEffect.PurchaseFailed("No tienes suficientes monedas"))
                return@launch
            }

            try {
                // Spend coins
                val success = userRepository.spendDrops(item.price)
                if (!success) {
                    _effects.emit(ShopEffect.PurchaseFailed("Error al procesar la compra"))
                    return@launch
                }

                // Save purchase
                if (item.type == ShopItemType.STREAK_SHIELD) {
                    // Increment streak shields count
                    preferencesManager.dataStore.edit { prefs ->
                        val current = prefs[STREAK_SHIELDS_OWNED] ?: 0
                        prefs[STREAK_SHIELDS_OWNED] = current + 1
                    }
                    _state.update { 
                        it.copy(
                            userCoins = it.userCoins - item.price,
                            streakShieldsOwned = it.streakShieldsOwned + 1
                        )
                    }
                } else {
                    // Add to purchased items
                    preferencesManager.dataStore.edit { prefs ->
                        val current = prefs[PURCHASED_ITEMS] ?: emptySet()
                        prefs[PURCHASED_ITEMS] = current + itemId
                    }
                    
                    // Update state
                    val newPurchasedItems = currentState.purchasedItems + itemId
                    val updatedItems = createShopItems(newPurchasedItems)
                    _state.update { 
                        it.copy(
                            userCoins = it.userCoins - item.price,
                            items = updatedItems,
                            purchasedItems = newPurchasedItems
                        )
                    }
                }

                _effects.emit(ShopEffect.PurchaseSuccess(item.name))
                _effects.emit(ShopEffect.ShowToast("¡Compraste ${item.name}!"))

            } catch (e: Exception) {
                _effects.emit(ShopEffect.PurchaseFailed("Error: ${e.message}"))
            }
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}
