package com.example.myapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.example.myapplication.Productdetailed
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Find ImageViews by ID
        val imageView1: ImageView = binding.root.findViewById(R.id.imageView1)
        val imageView2: ImageView = binding.root.findViewById(R.id.imageView2)

        // Set click listeners to open ImageDetailActivity
        imageView1.setOnClickListener {
            openImageDetailActivity()
        }

        imageView2.setOnClickListener {
            openImageDetailActivity()
        }

        return root
    }

    private fun openImageDetailActivity() {
        val intent = Intent(requireContext(), Productdetailed::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
