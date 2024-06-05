import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.EmptyShoppingCart
import com.example.caesarzonapplication.ui.components.HorizontalProductSection
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.ui.components.ProductCard
import com.example.caesarzonapplication.viewmodels.HomeViewModel
import com.example.caesarzonapplication.viewmodels.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartScreen(padding: PaddingValues, shoppingCartViewModel: ProductsViewModel, homeViewModel: HomeViewModel) {
    Scaffold(
        topBar = { Column {
            Spacer(modifier = Modifier.height(45.dp))
            AppTopBar()
        } },
        bottomBar = { NavigationBottomBar(navController = rememberNavController()) },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center, // Centra la LazyColumn verticalmente
                horizontalAlignment = Alignment.CenterHorizontally // Centra la LazyColumn orizzontalmente
            ) {
                if(shoppingCartViewModel.productInShoppingCart.isEmpty()){
                    item {
                        EmptyShoppingCart()
                    }
                } else {
                    items(shoppingCartViewModel.productInShoppingCart){ product ->
                        Spacer(modifier = Modifier.height(20.dp))
                        ProductCard(product = product)
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    HorizontalProductSection(title = "Altri prodotti", products = homeViewModel.products)
                }
            }
        }
    )
}

