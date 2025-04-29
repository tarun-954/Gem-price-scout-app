package com.example.myapplication



data class GemItem(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String? = null,
    val imageResId: Int? = null
)
