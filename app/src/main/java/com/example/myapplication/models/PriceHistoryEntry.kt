package com.example.myapplication.models

data class PriceHistoryEntry(
    val date: Long, // Unix timestamp
    val price: Float,
    val source: String // e.g. "Amazon", "Flipkart"
)