package com.example.caesarzonapplication.model.dto

import android.graphics.Bitmap

data class ProductWithImage(
    val product: ProductDTO,
    val image: Bitmap?
)