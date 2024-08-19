package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.dto.ProductDTO
import com.example.caesarzonapplication.model.dto.SendAvailabilityDTO
import com.example.caesarzonapplication.model.dto.SendProductDTO
import com.example.caesarzonapplication.model.viewmodels.AdminViewModels.AdminProductViewModel
import java.util.UUID

@Composable
fun AddProductScreen() {

    val adminProductViewModel = AdminProductViewModel()

    var productName by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var brand by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf( "") }
    var discount by rememberSaveable { mutableStateOf("") }
    var primaryColor by rememberSaveable { mutableStateOf("") }
    var secondaryColor by rememberSaveable { mutableStateOf("") }
    var isClothing by rememberSaveable { mutableStateOf(false) }
    var availability by rememberSaveable { mutableStateOf(listOf<SendAvailabilityDTO>()) }
    var sport by rememberSaveable { mutableStateOf("") }

    var selectedSize by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Aggiungi prodotto",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                color = Color.Black
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item { Spacer(modifier = Modifier.padding(top = 10.dp)) }
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descrizione") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item { Spacer(modifier = Modifier.padding(top = 10.dp)) }
                item {
                    OutlinedTextField(
                        value = brand,
                        onValueChange = { brand = it },
                        label = { Text("Brand") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item { Spacer(modifier = Modifier.padding(top = 10.dp)) }
                item {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Prezzo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item { Spacer(modifier = Modifier.padding(top = 10.dp)) }
                item {
                    OutlinedTextField(
                        value = discount,
                        onValueChange = { discount = it },
                        label = { Text("Sconto") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item { Spacer(modifier = Modifier.padding(top = 10.dp)) }
                item {
                    OutlinedTextField(
                        value = primaryColor,
                        onValueChange = { primaryColor = it },
                        label = { Text("Colore primario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item { Spacer(modifier = Modifier.padding(top = 10.dp)) }
                item {
                    OutlinedTextField(
                        value = secondaryColor,
                        onValueChange = { secondaryColor = it },
                        label = { Text("Colore secondario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item { Spacer(modifier = Modifier.padding(top = 10.dp)) }
                item {
                    OutlinedTextField(
                        value = sport,
                        onValueChange = { sport = it },
                        label = { Text("Sport") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item { Spacer(modifier = Modifier.padding(top = 10.dp)) }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = selectedSize,
                            onValueChange = { selectedSize = it },
                            label = { Text("Taglia") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Quantità") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            if (selectedSize.isNotBlank() && quantity.isNotBlank()) {
                                val qty = quantity.toIntOrNull() ?: 0
                                if (qty > 0) {
                                    availability = availability.toMutableList().apply {
                                        add(SendAvailabilityDTO(amount = qty, size = selectedSize))
                                    }
                                    selectedSize = ""
                                    quantity = ""
                                }
                            }
                        }) {
                            Text("Aggiungi")
                        }
                    }
                }
                item { Spacer(modifier = Modifier.padding(top = 10.dp)) }
                // Display the list of added availabilities
                items(availability.size) { availabilityIndex ->
                    val availabilityItem = availability[availabilityIndex]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Taglia: ${availabilityItem.size}",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Quantità: ${availabilityItem.amount}",
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            availability = availability.toMutableList().apply {
                                removeAt(availabilityIndex)
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Remove")
                        }
                    }
                }

                item {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { isClothing = !isClothing }) {
                        if (isClothing)
                            Text(text = "Abbigliamento")
                        else
                            Text(text = "Attrezzatura")
                    }
                }
                item {
                    Button(
                        onClick = {
                        adminProductViewModel.addProduct(
                            SendProductDTO(
                                name = productName,
                                description = description,
                                brand = brand,
                                price = price.toDouble(),
                                discount = discount.toDouble(),
                                primaryColor = primaryColor,
                                secondaryColor = secondaryColor,
                                is_clothing = isClothing,
                                sport = sport,
                                availabilities = availability
                            )
                        ) }
                    ) {
                        Text(text = "Aggiungi prodotto")
                    }
                }
            }
        }
    }
}