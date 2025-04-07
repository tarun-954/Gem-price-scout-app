package com.example.myapplication.ui.gallery

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentGalleryBinding
import com.example.myapplication.models.PriceHistoryEntry
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val client = OkHttpClient()
    private lateinit var lineChart: com.github.mikephil.charting.charts.LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Access UI components from binding
        val editText = binding.searchBox
        val trackButton = binding.searchBtn
        val resultText = binding.textResult
        lineChart = binding.lineChart

        // Setup chart
        setupChart()

        // Optional: Observe LiveData if you want
        galleryViewModel.text.observe(viewLifecycleOwner) {
            resultText.text = it
        }

        // Handle click on "Track Product"
        trackButton.setOnClickListener {
            val input = editText.text.toString().trim()

            if (input.isEmpty()) {
                resultText.text = "Please enter a product name or paste a link."
            } else if (input.contains("amazon") || input.contains("flipkart")) {
                resultText.text = "Tracking product link: $input"

                // Extract product ID and fetch price history
                val productId = extractProductId(input)
                if (productId != null) {
                    fetchPriceHistory(productId, input)
                } else {
                    resultText.text = "Could not extract product ID from link."
                }
            } else {
                resultText.text = "Searching for product: \"$input\""
                // TODO: Add search feature later
            }
        }

        return root
    }

    private fun setupChart() {
        // Configure chart appearance
        lineChart.setNoDataText("No price history available")
        lineChart.setNoDataTextColor(Color.BLACK)
        lineChart.setDrawGridBackground(false)
        lineChart.setDrawBorders(false)
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(true)

        // Configure X axis
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            private val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

            override fun getFormattedValue(value: Float): String {
                return dateFormat.format(Date(value.toLong()))
            }
        }

        // Configure Y axis
        val leftAxis = lineChart.axisLeft
        leftAxis.textSize = 10f
        leftAxis.textColor = Color.BLACK

        // Remove right Y axis
        lineChart.axisRight.isEnabled = false

        // Configure description
        val description = Description()
        description.text = "Price History"
        description.textSize = 12f
        lineChart.description = description

        // Set animation
        lineChart.animateX(1000)
    }

    private fun extractProductId(url: String): String? {
        return when {
            url.contains("amazon") -> {
                // Amazon product ID extraction
                // Format: https://www.amazon.in/dp/B0BDJH3V3Q/ or similar
                val pattern = "/dp/([A-Z0-9]{10})".toRegex()
                val matchResult = pattern.find(url)
                matchResult?.groupValues?.get(1)
            }
            url.contains("flipkart") -> {
                // Flipkart product ID extraction (simplified)
                val pattern = "/p/([a-zA-Z0-9]+)".toRegex()
                val matchResult = pattern.find(url)
                matchResult?.groupValues?.get(1)
            }
            else -> null
        }
    }

    private fun fetchPriceHistory(productId: String, productUrl: String) {
        binding.textResult.text = "Fetching price history..."

        // Use Coroutines for asynchronous API call
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val priceHistory = fetchPriceHistoryFromApi(productId, productUrl)

                withContext(Dispatchers.Main) {
                    if (priceHistory.isNotEmpty()) {
                        updateChart(priceHistory)
                        binding.textResult.text = "Price history loaded successfully!"
                    } else {
                        binding.textResult.text = "No price history available for this product."
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.textResult.text = "Error fetching price history: ${e.message}"
                }
            }
        }
    }

    private suspend fun fetchPriceHistoryFromApi(productId: String, productUrl: String): List<PriceHistoryEntry> {
        // Use Rainforest API for Amazon product pricing
        // Note: This is a paid API but they offer a free tier
        // You'll need to sign up at https://www.rainforestapi.com/
        val apiKey = "YOUR_RAINFOREST_API_KEY" // Replace with your actual API key

        val baseUrl = if (productUrl.contains("amazon")) {
            "https://api.rainforestapi.com/request?api_key=$apiKey&type=product&amazon_domain=amazon.in&asin=$productId"
        } else {
            // For Flipkart, you might need a different API
            // This is a placeholder - you'll need to find a suitable API for Flipkart
            return generateDummyPriceData() // Temporary solution
        }

        // For testing purposes, return dummy data instead of making actual API calls
        return generateDummyPriceData()

        // Uncomment the below code when you have a valid API key
        /*
        val request = Request.Builder()
            .url(baseUrl)
            .build()

        val response = client.newCall(request).execute()
        val jsonData = response.body?.string()

        if (jsonData != null) {
            return parsePriceHistoryJson(jsonData)
        }

        return emptyList()
        */
    }

    private fun parsePriceHistoryJson(jsonData: String): List<PriceHistoryEntry> {
        // Parse the JSON response from the API
        // Note: This implementation will depend on the actual API response format
        val priceHistory = mutableListOf<PriceHistoryEntry>()

        try {
            val jsonObject = JSONObject(jsonData)
            val product = jsonObject.getJSONObject("product")

            // Parse price history data - this structure will depend on the actual API
            if (product.has("price_history")) {
                val priceHistoryData = product.getJSONArray("price_history")

                for (i in 0 until priceHistoryData.length()) {
                    val entry = priceHistoryData.getJSONObject(i)
                    val date = entry.getLong("date")
                    val price = entry.getDouble("price").toFloat()

                    priceHistory.add(PriceHistoryEntry(date, price, "Amazon"))
                }
            }
        } catch (e: Exception) {
            // Handle parsing exceptions
            e.printStackTrace()
        }

        return priceHistory
    }

    private fun generateDummyPriceData(): List<PriceHistoryEntry> {
        // Generate dummy price history data for testing
        val priceHistory = mutableListOf<PriceHistoryEntry>()
        val currentTime = System.currentTimeMillis()
        val dayInMillis = TimeUnit.DAYS.toMillis(1)

        // Generate price points for the last 30 days
        for (i in 30 downTo 0) {
            val date = currentTime - (i * dayInMillis)
            // Generate a price between 10000 and 15000
            val basePrice = 12000f
            val variation = (Math.sin(i.toDouble() / 3) * 1500).toFloat()
            val price = basePrice + variation

            priceHistory.add(PriceHistoryEntry(date, price, "Amazon"))
        }

        return priceHistory
    }

    private fun updateChart(priceHistory: List<PriceHistoryEntry>) {
        // Convert price history to chart entries
        val entries = priceHistory.map { Entry(it.date.toFloat(), it.price) }

        // Create dataset
        val dataSet = LineDataSet(entries, "Price (₹)")
        dataSet.color = Color.BLUE
        dataSet.lineWidth = 2f
        dataSet.setCircleColor(Color.BLUE)
        dataSet.circleRadius = 3f
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawValues(false)
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.BLUE
        dataSet.fillAlpha = 30

        // Create LineData object
        val lineData = LineData(dataSet)

        // Set data to chart
        lineChart.data = lineData
        lineChart.invalidate() // Refresh chart
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}