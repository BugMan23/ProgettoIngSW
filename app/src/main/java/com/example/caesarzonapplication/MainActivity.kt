package com.example.caesarzonapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.model.database.AppDatabase
import com.example.caesarzonapplication.model.repository.userRepository.AddressRepository
import com.example.caesarzonapplication.model.repository.userRepository.CardRepository
import com.example.caesarzonapplication.model.repository.userRepository.FollowerRepository
import com.example.caesarzonapplication.model.repository.userRepository.ProfileImageRepository
import com.example.caesarzonapplication.model.repository.userRepository.UserRepository
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModelFactory
import com.example.caesarzonapplication.model.viewmodels.NotificationViewModel
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.FollowersViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.FollowersViewModelFactory
import com.example.caesarzonapplication.ui.screens.MainScreen
import com.example.caesarzonapplication.ui.theme.CaesarzonApplicationTheme

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         val productsViewModel: ProductsViewModel by viewModels()
         val accountInfoViewModel by viewModels<AccountInfoViewModel>{
             AccountInfoViewModelFactory(UserRepository(AppDatabase.getDatabase(this).userDao()),
                 ProfileImageRepository(AppDatabase.getDatabase(this).profileImageDao())) }

        val followersViewModel: FollowersViewModel by viewModels<FollowersViewModel>{
            FollowersViewModelFactory(FollowerRepository(AppDatabase.getDatabase(this).followerDao()))}
        val notificationViewModel: NotificationViewModel by viewModels()
         val wishlistViewModel: ProductsViewModel by viewModels()
         val friendsViewModel: ProductsViewModel by viewModels()

        setContent{
            CaesarzonApplicationTheme{
                val navController = rememberNavController()
                KeycloakService().getBasicToken()

                MainScreen(
                    navController = navController,
                    accountInfoViewModel = accountInfoViewModel,
                    productsViewModel = productsViewModel,
                    notificationViewModel = notificationViewModel,
                    followersViewModel = followersViewModel
                )
            }
        }
    }
}


