package com.example.myapplication




import android.content.Context

object UserPreferences {
    fun saveAlertPrice(context: Context, gemName: String, price: Float) {
        val prefs = context.getSharedPreferences("GEM_ALERTS", Context.MODE_PRIVATE)
        prefs.edit().putFloat(gemName, price).apply()
    }

    fun getAlertPrice(context: Context, gemName: String): Float {
        val prefs = context.getSharedPreferences("GEM_ALERTS", Context.MODE_PRIVATE)
        return prefs.getFloat(gemName, Float.MAX_VALUE)
    }
}
