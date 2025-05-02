package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
//import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import java.util.concurrent.TimeUnit

class Productdetailed : AppCompatActivity() {


        private lateinit var gemRecyclerView: RecyclerView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_productdetailed)

            val searchIcon: ImageView = findViewById(R.id.searchIcon) // Use the actual ID of your search image
            searchIcon.setOnClickListener {
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)

            gemRecyclerView = findViewById(R.id.gemRecyclerView)
            gemRecyclerView.layoutManager = GridLayoutManager(this, 2)

            // Load gem data
            val fakeApi = FakeGemApiService()
            val gems = fakeApi.getGems()

            gemRecyclerView.adapter = GemAdapter(gems)

            // Schedule price alert for "Ruby"
            schedulePriceCheck(this, gemName = "Ruby", alertPrice = 60.0f)
        }
        }
        private fun schedulePriceCheck(context: Context, gemName: String, alertPrice: Float) {
            val work = PeriodicWorkRequestBuilder<PriceCheckWorker>(15, TimeUnit.MINUTES)
                .setInputData(
                    workDataOf(
                        "gem_name" to gemName,
                        "alert_price" to alertPrice
                    )
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "PriceCheck_$gemName",
                ExistingPeriodicWorkPolicy.REPLACE,
                work
            )
        }
}




