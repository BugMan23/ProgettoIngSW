package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.isAdmin
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ShoppingCartViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ProductsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.WishlistViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AddressViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.FollowersViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.OrdersViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ReviewViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.SupportRequestsViewModel
import com.example.caesarzonapplication.navigation.AdminBottomBarScreen
import com.example.caesarzonapplication.navigation.AdminNavigationBottomBar
import com.example.caesarzonapplication.navigation.DetailsScreen
import com.example.caesarzonapplication.navigation.NavigationBottomBar
import com.example.caesarzonapplication.navigation.NavigationGraph
import com.example.caesarzonapplication.ui.components.SearchBar

@Composable
fun MainScreen(
    navController: NavHostController,
    accountInfoViewModel: AccountInfoViewModel,
    productsViewModel: ProductsViewModel,
    followersViewModel: FollowersViewModel,
    addressViewModel: AddressViewModel,
    cardsViewModel: CardsViewModel,
    notificationViewModel: NotificationViewModel,
    supportRequestsViewModel: SupportRequestsViewModel,
    reviewViewModel: ReviewViewModel,
    wishlistViewModel: WishlistViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
    ordersViewModel: OrdersViewModel,
    isFromPayPal: Boolean,
    redirectUrl: String
) {
    Scaffold(
        topBar = {
            SearchBar(navController = navController, notificationViewModel)
        },
        bottomBar = {
            if (!isAdmin.value)
                NavigationBottomBar(navController)
            else
                AdminNavigationBottomBar(navController)
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding),
            ) {
                NavigationGraph(
                    navController = navController,
                    productsViewModel = productsViewModel,
                    accountInfoViewModel = accountInfoViewModel,
                    followerViewModel = followersViewModel,
                    addressViewModel = addressViewModel,
                    cardViewModel = cardsViewModel,
                    supportRequestViewModel = supportRequestsViewModel,
                    reviewViewModel = reviewViewModel,
                    wishlistViewModel = wishlistViewModel,
                    notificationViewModel = notificationViewModel,
                    shoppingCartViewModel = shoppingCartViewModel,
                    ordersViewModel = ordersViewModel
                )

                LaunchedEffect(Unit) {
                    if (isFromPayPal) {
                        navController.navigate(DetailsScreen.PaymentSuccessScreen.route+"/${redirectUrl}")
                    }
                }

            }
        }
    )
}
