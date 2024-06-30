package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.dto.SingleWishlistProductDTO
import com.example.caesarzonapplication.viewmodels.WishlistViewModel
import java.util.UUID

@Composable
fun WishlistProductList(
    productList: List<SingleWishlistProductDTO>?,
    wishlistViewModel: WishlistViewModel,
    wishlistId: UUID
) {

    Spacer(modifier = Modifier.height(8.dp))
    if (productList != null) {
        for(product in productList) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = String.format("€%.2f", product.price),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Button(onClick = 
                    { wishlistViewModel.removeWishlistProduct(wishlistId, product.productId) })
                {
                    Text(text = "Rimuovi")   
                }
            }
        }
    }
}