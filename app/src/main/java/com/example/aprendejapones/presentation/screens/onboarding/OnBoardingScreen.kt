package com.example.aprendejapones.presentation.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aprendejapones.presentation.screens.onboarding.pages.FeaturesPage
import com.example.aprendejapones.presentation.screens.onboarding.pages.ReadyPage
import com.example.aprendejapones.presentation.screens.onboarding.pages.WelcomePage
import com.example.aprendejapones.presentation.theme.*
import kotlinx.coroutines.launch

/**
 * Pantalla de Onboarding
 * Muestra bienvenida y características de la app después del registro
 * El nombre ya se obtuvo durante el registro, no se pide de nuevo
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    // Manejar efectos
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                OnboardingEffect.NavigateToHome -> onComplete()
                is OnboardingEffect.ShowToast -> {
                    // TODO: Implementar toast
                }
            }
        }
    }

    // Sincronizar pager con state
    LaunchedEffect(pagerState.currentPage) {
        viewModel.onEvent(OnboardingEvent.PageChanged(pagerState.currentPage))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Pager de páginas
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> WelcomePage()
                    1 -> FeaturesPage()
                    2 -> ReadyPage(userName = state.userName)
                }
            }

            // Indicadores y botones
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Indicadores de página
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == pagerState.currentPage) 24.dp else 8.dp, 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == pagerState.currentPage) PrimaryGreen
                                    else BorderGray
                                )
                        )
                    }
                }

                // Botones
                if (pagerState.currentPage < 2) {
                    // Páginas 0-1: Siguiente y Saltar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextButton(
                            onClick = { viewModel.onEvent(OnboardingEvent.SkipOnboarding) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Saltar", color = TextSecondary, fontSize = 14.sp)
                        }

                        Button(
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .border(2.dp, PrimaryGreen, RoundedCornerShape(8.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(14.dp)
                        ) {
                            Text(
                                "Siguiente",
                                color = SurfaceWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    // Página final: Comenzar
                    Button(
                        onClick = { viewModel.onEvent(OnboardingEvent.CompleteOnboarding) },
                        enabled = !state.isCreatingProfile,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, PrimaryGreen, RoundedCornerShape(8.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryGreen,
                            disabledContainerColor = PrimaryGreen.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        if (state.isCreatingProfile) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = SurfaceWhite,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "¡Comenzar!",
                                color = SurfaceWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}