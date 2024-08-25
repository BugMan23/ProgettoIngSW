package com.example.caesarzonapplication.model.viewmodels.adminViewModels

import com.example.caesarzonapplication.model.dto.SendProductDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL
import java.util.UUID

class AdminProductViewModel {

    val client = OkHttpClient()
    val gson = Gson()

    fun addProduct(productDTO: SendProductDTO){

        val manageURL = URL("http://25.49.50.144:8090/product-api/product")
        val jsonProduct = gson.toJson(productDTO)

        val requestBody = jsonProduct.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request
            .Builder()
            .url(manageURL)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.newCall(request).execute().use { response ->
                    withContext(Dispatchers.Main) {
                        if (!response.isSuccessful) {
                            println("Errore nella creazione del prodotto: ${response.message}")
                        } else {
                            println("Prodotto aggiunto con successo")
                        }
                    }
                }
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateProduct(){
        /*TODO*/
    }

    fun deleteProduct(productID: UUID){
        println("id del prodtto: "+productID)
        val manageURL = URL("http://25.49.50.144:8090/product-api/product?productID=$productID")

        val request = Request
            .Builder()
            .url(manageURL)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .delete()
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()

                println("response: ${response.message} ${response.code}")
                if (!response.isSuccessful) {
                    println("Errore nella cancellazione del prodotto: ${response.message}")
                    println("Body: ${response.body?.string()}") // Stampa il body della risposta per maggiori dettagli
                } else {
                    println("Prodotto eliminato con successo")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}