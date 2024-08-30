package com.example.caesarzonapplication.ui.screens

import ReturnsSection
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel.Companion.userData
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AddressViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.OrdersViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.SupportRequestsViewModel
import com.example.caesarzonapplication.navigation.AccountTabRow
import com.example.caesarzonapplication.ui.components.LoadBar
import com.example.caesarzonapplication.ui.components.OrderManagementSection
import com.example.caesarzonapplication.ui.components.PaymentManagementSection
import com.example.caesarzonapplication.ui.components.SupportSection
import com.example.caesarzonapplication.ui.components.UserAddressInfoSection
import com.example.caesarzonapplication.ui.components.UserInfoSection

@Composable
fun AccountScreen(
    navController: NavController,
    accountInfoViewModel: AccountInfoViewModel,
    addressViewModel: AddressViewModel,
    supportViewModel: SupportRequestsViewModel,
    cardViewModel: CardsViewModel,
    notificationViewModel: NotificationViewModel,
    orderViewModels: OrdersViewModel
) {

val accountTabs = listOf(
    AccountTabRow.Profile,
    AccountTabRow.Addresses,
    AccountTabRow.Cards,
    AccountTabRow.Orders,
    AccountTabRow.Returns,
    AccountTabRow.Support
)

var selectedTab by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        notificationViewModel.getNotification()
    }
Column(
    modifier = Modifier.fillMaxSize()
) {
    if (accountInfoViewModel.isLoading.value || addressViewModel.isLoading.value || supportViewModel.isLoading.value || cardViewModel.isLoading.value) {
        LoadBar()
    }
    ScrollableTabRow(
        selectedTabIndex = selectedTab,
        edgePadding = 5.dp,
        containerColor = TabRowDefaults.secondaryContainerColor,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .tabIndicatorOffset(it[selectedTab])
            ) }
        ) {
            accountTabs.forEachIndexed { index, tab ->
                Tab(
                    text = { Text(text = tab.name) },
                    selected = index == selectedTab,
                    onClick = { selectedTab = index },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedTab) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.name,
                        )
                    },
                    unselectedContentColor = TabRowDefaults.secondaryContentColor,
                    selectedContentColor = TabRowDefaults.primaryContentColor,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(horizontal = 8.dp)
                )
            }
        }
        when (selectedTab) {
            0 -> UserInfoSection(accountInfoViewModel)
            1 -> UserAddressInfoSection(addressViewModel)
            2 -> PaymentManagementSection(cardViewModel)
            3 -> OrderManagementSection(orderViewModels)
            4 -> ReturnsSection(orderViewModels)
            5 -> userData?.username?.let { SupportSection(supportViewModel, it) }
        }
    }
}