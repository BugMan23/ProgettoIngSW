package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.traceEventEnd
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.Product
import kotlinx.coroutines.launch

class ShoppingCartViewModel: ViewModel() {
    private val _productsInShoppingCart = mutableStateListOf<Product>()

    val productInShoppingCart: SnapshotStateList<Product> get() = _productsInShoppingCart

    init{
        loadShoppingCartProduct()
    }

    private fun loadShoppingCartProduct(){
        viewModelScope.launch {
            _productsInShoppingCart.addAll(
                listOf(
                    Product("Calzettoni da calcio puzzolenti", R.drawable.ic_calcio, 100.00, "Calzettoni da calcio fetidi", true,0),
                    Product("Paradenti masticato", R.drawable.ic_lotta, 20.00, "Un paradenti consumato", true,2),
                    Product("Racchette rotte", R.drawable.ic_tennis, 10.00, "C'è il manico ma non la racchetta", true,3),
                    Product("Tanga anaerobico", R.drawable.ic_atletica, 40.00, "Intimo utile per rinfrscare il sotto palla", true,1),
                    Product("Capocuffia", R.drawable.ic_nuoto, 30.00, "Una cuffia da nuoto con la stampa di una capocchia", true,5),
                    Product("Pallone bucato", R.drawable.ic_pallavolo, 104.00, "Palla bucata", true,9)
                )
            )
        }
    }

    /*
    GEMINI esempio
    fun loadProductsFromServer() {
    viewModelScope.launch {
        try {
            val products = repository.getProductsFromApi()
            _products.value = products
        } catch (e: Exception) {
            _errorMessage.value = e.message
        }
    }
}*/
    /*
    fun saveProductToDatabase(product: Product) {
    viewModelScope.launch {
        repository.saveProductToDatabase(product)
    }
}

     */

    fun addProduct(product: Product){
        if(_productsInShoppingCart.contains(product)){
            val existingProduct = _productsInShoppingCart.find { it == product }
            existingProduct?.copy(quantity = product.quantity+1)
        }else{
            _productsInShoppingCart.add(product.copy(quantity = 1))
        }

    }

    fun incriseProduct(product: Product){
        val product = _productsInShoppingCart.find { it == product }
        product?.let {
            if(it.quantity<10)
                it.quantity+=1
        }
    }

    fun decreaseProduct(product: Product){
        val product = _productsInShoppingCart.find { it == product }
        product?.let {
            if(it.quantity>0)
                it.quantity-=1
        }
    }

    fun deleteProduct(name: String){
        println("Prodotto eliminato: "+name)
        _productsInShoppingCart.removeAll{ it.name == name }
    }
}