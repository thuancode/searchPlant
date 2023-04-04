package com.example.searchplant.view.screen

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.searchplant.R
import com.example.searchplant.databinding.FragmentOnBoardingBinding
import com.example.searchplant.databinding.FragmentSplashBinding


class onBoarding : Fragment() {
    lateinit var binding : FragmentOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPagerId)
        binding = FragmentOnBoardingBinding.inflate(inflater, container,false)
        binding.btnScreen1.setOnClickListener {
            viewPager?.currentItem = 1
        }
        return binding.root
    }

}