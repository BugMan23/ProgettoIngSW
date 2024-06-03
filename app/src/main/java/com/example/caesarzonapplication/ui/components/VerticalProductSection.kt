import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.Product
import com.example.caesarzonapplication.ui.components.ProductCard


@Composable
fun VerticalProductSection(title: String, products: List<Product>){
    Spacer(modifier = Modifier.height(15.dp))
    LazyColumn {
            items(products){ product ->
                ProductCard(product = product)
                Spacer(modifier = Modifier.height(15.dp))
            }
    }
}