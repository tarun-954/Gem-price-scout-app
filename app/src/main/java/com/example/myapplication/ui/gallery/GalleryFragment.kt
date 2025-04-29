package com.example.myapplication.ui.gallery

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentGalleryBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

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

        // Show demo graph on track button click
        trackButton.setOnClickListener {
            showDemoGraph(lineChart)
            resultText.text = "Showing demo price trend"
            editText.text.clear()
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
