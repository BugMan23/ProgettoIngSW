package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.viewmodels.AdminInfoViewModel

@Composable
fun SupportRequest(adminInfoViewModel: AdminInfoViewModel, padding: PaddingValues) {
    if(adminInfoViewModel.supportRequests.isNotEmpty()){
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            items(adminInfoViewModel.supportRequests) { support ->
                SupportUser(support, adminInfoViewModel)
            }
        }
    }else{
        Text(
            modifier = Modifier
                .padding(top = 150.dp)
                .padding(horizontal = 80.dp),
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ), text = "Non si sono richieste di assistenza"
        )
    }
}