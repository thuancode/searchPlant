package com.example.searchplant.view.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.searchplant.R
import com.example.searchplant.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container,false)
        val btnNavView = binding.bottomNavigationView
        btnNavView.background = null

        binding.btnSpe.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_speciesFragment)
        }

        binding.bottomNavigationView.setOnNavigationItemReselectedListener{
            when(it.itemId) {
                R.id.home -> {
                    findNavController().navigate(R.id.action_homeFragment_self)
                }
                R.id.profile -> {
                    // Respond to navigation item 2 reselection
                }
            }
        }
        return binding.root
    }


}