package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ShoppingCartViewModel
import com.example.caesarzonapplication.navigation.DetailsScreen
import com.example.caesarzonapplication.ui.components.EmptyShoppingCart
import com.example.caesarzonapplication.ui.components.ShoppingCartCard

@Composable
fun ShoppingCartScreen(navController: NavHostController, shoppingCartViewModel: ShoppingCartViewModel) {
    val shoppingCartProducts by shoppingCartViewModel.productsInShoppingCart.collectAsState()
    val buyLaterProducts by shoppingCartViewModel.buyLaterProducts.collectAsState()
    val errorMessage by shoppingCartViewModel.errorMessages.collectAsState()
    val canNavigate by shoppingCartViewModel.canNavigate.collectAsState()

    LaunchedEffect(Unit) {
        shoppingCartViewModel.getCart()
    }

    if (errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {
                shoppingCartViewModel.clearErrorMessages()
                shoppingCartViewModel.changeOnNavigate()
            },
            title = { Text(text = "Sono cambiate delle disponibilità") },
            text = { Text(text = errorMessage) },
            confirmButton = {
                Button(
                    onClick = {
                        shoppingCartViewModel.clearErrorMessages()
                        shoppingCartViewModel.changeOnNavigate()

                    }
                ) {
                    Text("OK")
                }
            }
        )
    }


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (shoppingCartProducts.isEmpty() && buyLaterProducts.isEmpty()) {
            item {
                EmptyShoppingCart()
            }
        } else {
            item {
                Text(
                    text = "Prodotti nel Carrello",
                    fontSize = 22.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
            items(shoppingCartProducts) { shoppingCartProduct ->
                ShoppingCartCard(
                    product = shoppingCartProduct,
                    shoppingCartViewModel = shoppingCartViewModel,
                    navController = navController,
                )
                Spacer(modifier = Modifier.height(30.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { navController.navigate("home") },
                        modifier = Modifier
                            .padding(15.dp)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        )
                    ) {
                        Text(
                            text = "Continua a comprare",
                            style = TextStyle(fontSize = 16.sp),
                            softWrap = false
                        )
                    }
                    Button(
                        onClick = {
                            shoppingCartViewModel.checkAvailability()
                            if(canNavigate){
                                navController.navigate(DetailsScreen.CheckOutScreen.route)
                            }
                        },
                        modifier = Modifier
                            .padding(15.dp)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green
                        )
                    ) {
                        Text(
                            text = "Procedi",
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                }
            }

            if (buyLaterProducts.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Salvati per dopo",
                        fontSize = 22.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                items(buyLaterProducts) { buyLaterProduct ->
                    ShoppingCartCard(
                        product = buyLaterProduct,
                        shoppingCartViewModel = shoppingCartViewModel,
                        navController = navController,
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}
