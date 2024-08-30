package com.example.caesarzonapplication.model.viewmodels

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.productDTOS.BuyDTO
import com.example.caesarzonapplication.model.dto.productDTOS.ChangeCartDTO
import com.example.caesarzonapplication.model.dto.productDTOS.ProductCartDTO
import com.example.caesarzonapplication.model.dto.productDTOS.ProductCartWithImage
import com.example.caesarzonapplication.model.dto.productDTOS.SendProductCartDTO
import com.example.caesarzonapplication.model.dto.productDTOS.UnvailableDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.basicToken
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.example.caesarzonapplication.model.utils.BitmapConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL
import java.util.UUID

class ShoppingCartViewModel(): ViewModel() {

    private val client = OkHttpClient()
    val gson = Gson()
    val bitmapConverter = BitmapConverter()

    private val _productsInShoppingCart: MutableStateFlow<List<ProductCartWithImage>> = MutableStateFlow(emptyList())
    val productsInShoppingCart: StateFlow<List<ProductCartWithImage>> = _productsInShoppingCart

    private var _buyLaterProducts: MutableStateFlow<List<ProductCartWithImage>> = MutableStateFlow(emptyList())
    val buyLaterProducts: StateFlow<List<ProductCartWithImage>> =_buyLaterProducts

    private val _errorMessages: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessages: StateFlow<String> = _errorMessages

    private val _total: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total

    private var productCartId = ArrayList<String>()

    //Agigungi messaggio di errore
    suspend fun loadProductImage(productId: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val uuidProduct = UUID.fromString(productId)
            val manageURL = URL("http://25.49.50.144:8090/product-api/image/$uuidProduct")
            val request = Request.Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful || response.body == null) {
                    return@withContext null
                }
                val imageByteArray = response.body?.byteStream()?.readBytes()

                if (imageByteArray != null) {
                    val convertedImage = bitmapConverter.converterByteArray2Bitmap(imageByteArray)
                    return@withContext convertedImage
                } else {
                    println("Response body is null")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore nel caricamento dell'immagine")
            }
            return@withContext null
        }
    }


    fun getCart(){
        viewModelScope.launch {
            try{
                doGetCart()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doGetCart(){
        val manageUrl = URL("http://25.49.50.144:8090/product-api/cart")
        val request =  Request.Builder().url(manageUrl).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        _productsInShoppingCart.value = emptyList()
        _buyLaterProducts.value = emptyList()

        withContext(Dispatchers.IO){
                try{
                    val response = client.newCall(request).execute()
                    val responseBody = response.body?.string()

                    if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                        println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    }

                    println("Risposta dal server: $responseBody")
                    val cart = object : TypeToken<List<ProductCartDTO>>() {}.type
                    val products = gson.fromJson<List<ProductCartDTO>>(responseBody, cart)
                    for(product in products){
                        val image = loadProductImage(product.id)
                        if(product.buyLater)
                            _buyLaterProducts.value += ProductCartWithImage(product, image)
                        else
                            _productsInShoppingCart.value += ProductCartWithImage(product, image)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    println("Errore durante la chiamata: ${e.message}")
                }
        }
    }

    fun saveForLaterOrChangeQuantityAndSize(productId: String, action: Int, size: String, quantity: Int) {
        viewModelScope.launch {
            try {
                doSaveForLaterOrChangeQuantityAndSize(productId, action, size, quantity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun doSaveForLaterOrChangeQuantityAndSize(productId: String, num: Int, size: String, quantity: Int) {
        val manageUrl = URL("http://25.49.50.144:8090/product-api/cart/product/${productId}?action=${num}")

        val changeDTO = ChangeCartDTO(quantity, size)
        val json = gson.toJson(changeDTO)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder().url(manageUrl).put(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("Risposta dal server: $responseBody")

                val productToMove = _productsInShoppingCart.value.find { it.product.id == productId }
                    ?: _buyLaterProducts.value.find { it.product.id == productId }

                if (productToMove != null) {
                    if (num == 0) {
                        // Salva per dopo
                        _buyLaterProducts.update {
                            it + productToMove.copy(product = productToMove.product.copy(quantity = quantity, size = size))
                        }
                    } else {
                        // Riporta nel carrello
                        _productsInShoppingCart.update {
                            it + productToMove.copy(product = productToMove.product.copy(quantity = quantity, size = size))
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }



    fun addProductCart(productId: String, size: String, quantity: Int){
        viewModelScope.launch {
            try{
                doAddProductCart(productId, size, quantity)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doAddProductCart(productId: String, size: String, quantity: Int){
        val manageUrl = URL("http://25.49.50.144:8090/product-api/cart")
        val prodDTO = SendProductCartDTO(productId, quantity, size)
        val json = gson.toJson(prodDTO)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder().url(manageUrl).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("ripsosta dal server: $responseBody")

            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }


    fun deleteProductCart(productId: String){
        viewModelScope.launch {
            try{
                doDeleteProductCart(productId)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doDeleteProductCart(productId: String){
        val manageUrl = URL("http://25.49.50.144:8090/product-api/cart/$productId")
        val request = Request.Builder().url(manageUrl).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("ripsosta dal server: $responseBody")
                _productsInShoppingCart.value = _productsInShoppingCart.value.filterNot { it.product.id == productId }
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    fun deleteCart(){
        viewModelScope.launch {
            try{
                doDeleteCart()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doDeleteCart(){
        val manageUrl = URL("http://25.49.50.144:8090/product-api/cart")
        val request = Request.Builder().url(manageUrl).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("ripsosta dal server: $responseBody")

            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    fun checkAvailability(){
        viewModelScope.launch {
            try{
                doCheckAvailability()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doCheckAvailability(){
        val manageUrl = URL("http://25.49.50.144:8090/product-api/pre-order")

        for(product in _productsInShoppingCart.value){
            productCartId.add(product.product.id)
        }

        val json = gson.toJson(productCartId)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder().url(manageUrl).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){

            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody != null){
                    println("Chiamata fallita o disponibilità non disponibile. Codice di stato: ${response.code}")
                    var errorMessage = ""
                    val availability = object : TypeToken<List<UnvailableDTO>>() {}.type
                    val gsonAvailability : List<UnvailableDTO> = gson.fromJson(responseBody, availability)
                    for(av in gsonAvailability){
                        errorMessage+=av.name+" "
                        for(a in av.availabilities){
                            if(a.size != null){
                                errorMessage+=a.size+" "
                            }
                            errorMessage+=a.amount.toString()+"\n"
                        }
                    }
                    _errorMessages.value = errorMessage
                }


                println("ripsosta dal server: $responseBody")
                for(product in _productsInShoppingCart.value){
                    _total.value += product.product.discountTotal
                }

            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }

        }

    }
    fun clearErrorMessages() {
        _errorMessages.value = ""
    }

    fun purchase(
        addressID: String,
        cardID: String,
        payPal: Boolean,
        context: Context
    ){
        viewModelScope.launch {
            try {
                doPurchase(addressID, cardID, payPal, context)
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    suspend fun doPurchase( addressID: String,  cardID: String, paypal: Boolean, context: Context){
        val manageUrl = URL("http://25.49.50.144:8090/product-api/purchase?pay-method=${paypal}")

        val buyDTO = BuyDTO(addressID, cardID, _total.value, productCartId)
        val json = gson.toJson(buyDTO)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(JSON)

        val request = Request.Builder().url(manageUrl).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody == "Errore..."){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    _errorMessages.value += "Problemi nell'acquisto, controllare dati o saldo carta."
                    return@withContext
                }

                if (paypal) {
                    withContext(Dispatchers.Main) {
                        if (responseBody != null) {
                            openPaypalLink(responseBody, context)
                        }
                    }
                }

                println("Risposta dal server: $responseBody")

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun openPaypalLink(link: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
        }
        // Verifica se c'è almeno un'app che può gestire l'intent prima di lanciarlo
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            println("Nessuna app disponibile per gestire l'intent")
        }
    }

}