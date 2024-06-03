package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.caesarzonapplication.R

enum class AccountTab {
    UserInfo,
    PaymentManagement,
    OrderManagement,
    Reports,
    Returns
}

@Composable
fun AccountScreen(padding: PaddingValues) {
    var selectedTab by remember { mutableStateOf(AccountTab.UserInfo) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoutente),
                contentDescription = "User Profile",
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp)
            )

            Text(text = "Profilo Utente", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(16.dp))

            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                edgePadding = 0.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal])
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                AccountTab.values().forEach { tab ->
                    Tab(
                        text = { Text(text = tab.name) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                AccountTab.UserInfo -> UserInfoSection()
                AccountTab.PaymentManagement -> PaymentManagementSection()
                AccountTab.OrderManagement -> OrderManagementSection()
                AccountTab.Reports -> ReportsSection()
                AccountTab.Returns -> ReturnsSection()
            }
        }
    }
}

@Composable
fun UserInfoSection() {
    var name by remember { mutableStateOf(TextFieldValue("John Doe")) }
    var email by remember { mutableStateOf(TextFieldValue("johndoe@example.com")) }
    var address by remember { mutableStateOf(TextFieldValue("123 Main St, Anytown, USA")) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Indirizzo di spedizione") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = if (passwordVisible) TextFieldValue("password123") else TextFieldValue("********"),
                onValueChange = {},
                label = { Text("Password") },
                enabled = false
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { passwordVisible = !passwordVisible }) {
                Text(if (passwordVisible) "Nascondi" else "Mostra")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Logica per salvare le informazioni aggiornate */ }) {
            Text(text = "Aggiorna Profilo")
        }
    }
}

@Composable
fun PaymentManagementSection() {
    var cardNumber by remember { mutableStateOf(TextFieldValue("")) }
    var cardHolderName by remember { mutableStateOf(TextFieldValue("")) }
    var cvc by remember { mutableStateOf(TextFieldValue("")) }
    var paymentMethods by remember { mutableStateOf(listOf("**** **** **** 1234", "**** **** **** 5678")) }
    var showAddPaymentDialog by remember { mutableStateOf(false) }
    var showRemovePaymentDialog by remember { mutableStateOf(false) }

    Column {
        Text(text = "Metodi di pagamento", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        paymentMethods.forEach { method ->
            Text(text = method, modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showAddPaymentDialog = true }) {
            Text(text = "Aggiungi metodo di pagamento")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { showRemovePaymentDialog = true }) {
            Text(text = "Rimuovi metodo di pagamento")
        }

        if (showAddPaymentDialog) {
            Dialog(onDismissRequest = { showAddPaymentDialog = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.medium)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "Aggiungi Metodo di Pagamento",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Chiudi",
                                modifier = Modifier
                                    .clickable { showAddPaymentDialog = false }
                            )
                        }
                        TextField(
                            value = cardHolderName,
                            onValueChange = { cardHolderName = it },
                            label = { Text("Nome e Cognome del Titolare") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = cardNumber,
                            onValueChange = { cardNumber = it },
                            label = { Text("Numero Carta di Credito") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = cvc,
                            onValueChange = { cvc = it },
                            label = { Text("CVC") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            paymentMethods = paymentMethods + cardNumber.text
                            showAddPaymentDialog = false
                        }) {
                            Text(text = "Salva")
                        }
                    }
                }
            }
        }

        if (showRemovePaymentDialog) {
            Dialog(onDismissRequest = { showRemovePaymentDialog = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.medium)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "Rimuovi Metodo di Pagamento",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Chiudi",
                                modifier = Modifier
                                    .clickable { showRemovePaymentDialog = false }
                            )
                        }
                        paymentMethods.forEach { method ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = method, modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    paymentMethods = paymentMethods - method
                                }) {
                                    Icon(
                                        Icons.Filled.Close,
                                        contentDescription = "Rimuovi"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderManagementSection() {
    val orders = listOf("Ordine #1", "Ordine #2", "Ordine #3") // Dati fittizi

    Column {
        Text(text = "Cronologia ordini", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        orders.forEach { order ->
            Text(text = order, modifier = Modifier.padding(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsSection() {
    var reportText by remember { mutableStateOf(TextFieldValue("")) }
    var expanded by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf("Motivo 1") }
    val reportReasons = listOf("Motivo 1", "Motivo 2", "Motivo 3")

    Column {
        Text(text = "Invia una segnalazione", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedReason,
                onValueChange = {},
                label = { Text("Motivo della segnalazione") },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                reportReasons.forEach { reason ->
                    DropdownMenuItem(
                        text = { Text(text = reason) },
                        onClick = {
                            selectedReason = reason
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = reportText,
            onValueChange = { reportText = it },
            label = { Text("Descrivi il problema") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Logica per inviare la segnalazione */ }) {
            Text(text = "Invia segnalazione")
        }
    }
}

@Composable
fun ReturnsSection() {
    val orders = listOf("Ordine #1", "Ordine #2", "Ordine #3") // Dati fittizi
    var showReturnDialog by remember { mutableStateOf(false) }
    var selectedOrder by remember { mutableStateOf("") }
    var returnReason by remember { mutableStateOf(TextFieldValue("")) }

    Column {
        Text(text = "Cronologia ordini per reso", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        orders.forEach { order ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = order, modifier = Modifier.padding(8.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    selectedOrder = order
                    showReturnDialog = true
                }) {
                    Text(text = "Richiedi reso")
                }
            }
        }

        if (showReturnDialog) {
            Dialog(onDismissRequest = { showReturnDialog = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.medium)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "Richiesta reso per $selectedOrder",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Chiudi",
                                modifier = Modifier
                                    .clickable { showReturnDialog = false }
                            )
                        }
                        TextField(
                            value = returnReason,
                            onValueChange = { returnReason = it },
                            label = { Text("Motivo del reso") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { showReturnDialog = false }) {
                            Text(text = "Invia richiesta")
                        }
                    }
                }
            }
        }
    }
}
