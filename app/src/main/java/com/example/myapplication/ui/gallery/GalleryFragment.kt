package com.example.myapplication.ui.gallery

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.databinding.FragmentGalleryBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.json.JSONObject

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel = ViewModelProvider(this)[GalleryViewModel::class.java]
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val editText = binding.searchBox
        val trackButton = binding.searchBtn
        val resultText = binding.textResult
        val lineChart = binding.lineChart

        galleryViewModel.text.observe(viewLifecycleOwner) {
            resultText.text = it
        }

        trackButton.setOnClickListener {
            val input = editText.text.toString().trim()

            if (input.isEmpty()) {
                resultText.text = "Please enter a product name or paste a link."
            } else if (input.contains("amazon")) {
                resultText.text = "Tracking product link..."

                try {
                    // Use raw URL instead of encoded for now
                    val apiUrl = "http://10.35.136.125:5000/track?url=$input"
                    // val apiUrl = "http://10.0.2.2:5000/track?url=$input" // ← Use this if testing on emulator

                    Log.d("API_CALL", "Calling: $apiUrl")

                    val queue = Volley.newRequestQueue(requireContext())
                    val stringRequest = StringRequest(
                        Request.Method.GET, apiUrl,
                        { response ->
                            Log.d("API_RESPONSE", "Response: $response")
                            try {
                                val json = JSONObject(response)
                                val title = json.optString("title", "N/A")
                                val price = json.optString("price", "N/A")

                                if (title == "N/A" || price == "N/A") {
                                    resultText.text = "Received null values from API. Showing demo graph..."
                                    showDemoGraph(lineChart)
                                } else {
                                    resultText.text = "Title: $title\nPrice: ₹$price"
                                    lineChart.visibility = View.GONE
                                }
                            } catch (e: Exception) {
                                resultText.text = "Failed to parse result. Showing demo graph..."
                                e.printStackTrace()
                                showDemoGraph(lineChart)
                            }
                        },
                        { error ->
                            Log.e("API_ERROR", "Error: ${error.message}", error)
                            resultText.text = "Error: ${error.message ?: "Unknown error"}. Showing demo graph..."
                            showDemoGraph(lineChart)
                        }
                    )

                    stringRequest.setShouldCache(false) // prevent cached 'null' responses
                    queue.add(stringRequest)

                } catch (e: Exception) {
                    resultText.text = "Encoding or request failed: ${e.message}"
                    e.printStackTrace()
                    showDemoGraph(lineChart)
                }

            } else {
                resultText.text = "Only Amazon product links are supported for now."
            }
        }

        return root
    }

    private fun showDemoGraph(chart: LineChart) {
        val demoData = listOf(
            Entry(1f, 999f),
            Entry(2f, 899f),
            Entry(3f, 949f),
            Entry(4f, 879f),
            Entry(5f, 999f)
        )

        val dataSet = LineDataSet(demoData, "Demo Price Trend")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK
        dataSet.setCircleColor(Color.RED)
        dataSet.lineWidth = 2f

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.description.text = "Demo Price Graph"
        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.invalidate()
        chart.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
