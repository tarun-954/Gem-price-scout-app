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
import java.net.URLEncoder

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
                    val encodedUrl = URLEncoder.encode(input, "UTF-8")
                    val apiUrl = "http://192.168.1.104:5000/track?url=$encodedUrl"

                    Log.d("API_CALL", "Calling: $apiUrl")

                    val queue = Volley.newRequestQueue(requireContext())
                    val stringRequest = StringRequest(
                        Request.Method.GET, apiUrl,
                        { response ->
                            try {
                                val json = JSONObject(response)
                                val title = json.getString("title")
                                val price = json.getString("price")

                                resultText.text = "Title: $title\nPrice: ₹$price"
                                lineChart.visibility = View.GONE // Hide demo chart if API succeeds
                            } catch (e: Exception) {
                                resultText.text = "Failed to parse result. Showing demo graph..."
                                e.printStackTrace()
                                showDemoGraph(lineChart)
                            }
                        },
                        { error ->
                            resultText.text = "Error: ${error.message}. Showing demo graph..."
                            error.printStackTrace()
                            showDemoGraph(lineChart)
                        })

                    queue.add(stringRequest)

                } catch (e: Exception) {
                    resultText.text = "Encoding failed: ${e.message}"
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
