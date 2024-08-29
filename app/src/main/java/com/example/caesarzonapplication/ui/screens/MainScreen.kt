package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.isAdmin
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.logged
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AddressViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.FollowersViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ReviewViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.SupportRequestsViewModel
import com.example.caesarzonapplication.navigation.AdminNavigationBottomBar
import com.example.caesarzonapplication.navigation.NavigationBottomBar
import com.example.caesarzonapplication.navigation.NavigationGraph
import com.example.caesarzonapplication.ui.components.NotificationsPopup
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
    reviewViewModel: ReviewViewModel
){

    var showNotificationsPopup by rememberSaveable { mutableStateOf(false) }
    val notifications by notificationViewModel.userNotification.collectAsState()
    notificationViewModel.getNotification()
    Scaffold(
        topBar = { SearchBar(navController) },
        bottomBar =
        {
            if(!isAdmin.value)
                NavigationBottomBar(navController)
            else
                AdminNavigationBottomBar(navController)
        },
        floatingActionButton = {
            if(logged.value){
                FloatingActionButton(
                    onClick = {
                        showNotificationsPopup = !showNotificationsPopup
                        // Carica le notifiche solo quando si apre il popup
                        if (showNotificationsPopup) {
                            notificationViewModel.getNotification()
                            notificationViewModel.updateNotification()
                        }
                    },
                    containerColor = Color(100, 104, 208),
                    contentColor = Color(238, 137, 60),
                    shape = CircleShape,
                    content = {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                    },
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .padding(end = 16.dp)
                )
                if (notifications.isNotEmpty()) {
                    Text(
                        text = "${notifications.size}",
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Red, shape = CircleShape)
                            .padding(end = 16.dp)
                            .padding(bottom = 22.dp),
                    )
                }
                // Mostra il popup delle notifiche se showNotificationsPopup è true
                if (showNotificationsPopup) {
                    NotificationsPopup(
                        notificationViewModel = notificationViewModel,
                        notifications = notificationViewModel.userNotification,
                        onDismissRequest = { showNotificationsPopup = false }
                    )
                }
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding),
            ) {
                NavigationGraph(
                    navController =navController,
                    isAdmin = isAdmin,
                    logged = logged,
                    productsViewModel = productsViewModel,
                    accountInfoViewModel = accountInfoViewModel,
                    followerViewModel = followersViewModel,
                    addressViewModel = addressViewModel,
                    cardViewModel = cardsViewModel,
                    notificationViewModel = notificationViewModel,
                    supportRequestViewModel = supportRequestsViewModel,
                    reviewViewModel = reviewViewModel
                )

            }
        }
    )
}