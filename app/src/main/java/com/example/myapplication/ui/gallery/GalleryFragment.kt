package com.example.myapplication.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Access UI components from binding
        val editText = binding.editTextProductSearch
        val trackButton = binding.btnTrackProduct
        val resultText = binding.textResult

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

                // TODO: Add logic to extract product ID

            } else {
                resultText.text = "Searching for product: \"$input\""

                // TODO: Add search feature later

            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
