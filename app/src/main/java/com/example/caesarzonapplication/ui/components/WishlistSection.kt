package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.dto.productDTOS.SingleWishlistProductDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.globalUsername
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.WishlistViewModel
import kotlinx.coroutines.launch

@Composable
fun WishlistSection(visibility: Int, wishlistViewModel: WishlistViewModel) {

    var selectedWishlistId by remember { mutableStateOf<String?>(null) }
    var newWishlistName by remember { mutableStateOf("") }
    val wishProduct by wishlistViewModel.wishProduct.collectAsState()

    var showPopup by rememberSaveable { mutableStateOf(false) }
    var showPopupMessage by rememberSaveable { mutableStateOf("") }

    // Variabile wishlists che cambia dinamicamente
    val wishlists by when (visibility) {
        0 -> wishlistViewModel.wishlistsPublic.collectAsState()
        1 -> wishlistViewModel.wishlistsShared.collectAsState()
        2 -> wishlistViewModel.wishlistsPrivate.collectAsState()
        else -> wishlistViewModel.wishlistsPublic.collectAsState() // default case
    }
    if (showPopup) { GenericMessagePopup(message = showPopupMessage, onDismiss = { showPopup = false }) }

    LaunchedEffect(Unit) {
       wishlistViewModel.getUserWishlists(globalUsername.value, visibility)
    }

    TextField(
        value = newWishlistName,
        onValueChange = { newWishlistName = it },
        label = { Text("Nome nuova lista") }
    )
    Button(modifier = Modifier.padding(8.dp), onClick = {
    wishlistViewModel.addWishlist(newWishlistName, globalUsername.value, visibility) }) {
        Text(text = "Aggiungi lista")
    }

    for (wishlist in wishlists) {
        Column(modifier = Modifier.padding(10.dp)) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = wishlist.name)
            Row {
                Button(modifier = Modifier.padding(8.dp), onClick = {
                    if (selectedWishlistId == wishlist.id) {
                        selectedWishlistId = null
                    } else {
                        selectedWishlistId = wishlist.id
                        wishlistViewModel.getWishlistProductsByWishlistID(wishlist.id, globalUsername.value)
                    }
                }) {
                    Text(text = if (selectedWishlistId == wishlist.id) "Nascondi" else "Visualizza")
                }
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        wishlistViewModel.deleteAllWishlistProductsByWishlistID(wishlist.id)
                        showPopupMessage = "Lista svuotata con successo";
                        showPopup = true
                    }) {
                    Text(text = "Svuota")
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        wishlistViewModel.deleteWishlist(wishlist.id);
                        showPopupMessage = "Lista eliminata con successo";
                        showPopup = true
                    }) {
                    Text(text = "Elimina")
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            }

            if (selectedWishlistId == wishlist.id && wishProduct?.singleWishListProductDTOS != null) {
                WishlistProductList(
                    wishProduct?.singleWishListProductDTOS,
                    wishlistViewModel,
                    wishlist.id
                )
            }
            when (visibility) {
                0 ->Row{
                    Button(modifier = Modifier.padding(10.dp), onClick = { wishlistViewModel.updateUserWishlists(wishlist.id, 1) }) { Text(text = "Rendi condivisa") }
                    Button(modifier = Modifier.padding(10.dp), onClick = { wishlistViewModel.updateUserWishlists(wishlist.id, 0) }) { Text(text = "Rendi privata") }
                }
                1 -> Row{
                    Button(modifier = Modifier.padding(10.dp), onClick = { wishlistViewModel.updateUserWishlists(wishlist.id, 2) }) { Text(text = "Rendi condivisa") }
                    Button(modifier = Modifier.padding(10.dp), onClick = { wishlistViewModel.updateUserWishlists(wishlist.id, 0) }) { Text(text = "Rendi pubblica") }
                }
                2 -> Row{
                    Button(modifier = Modifier.padding(10.dp), onClick = { wishlistViewModel.updateUserWishlists(wishlist.id, 0) }) { Text(text = "Rendi pubblica") }
                    Button(modifier = Modifier.padding(10.dp), onClick = { wishlistViewModel.updateUserWishlists(wishlist.id, 2) }) { Text(text = "Rendi privata") }
                }
            }
        }
    }
}
