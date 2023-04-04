package com.example.searchplant.view.screen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.searchplant.R
import com.example.searchplant.databinding.FragmentOnBoarding1Binding
import com.example.searchplant.databinding.FragmentOnBoarding2Binding
import com.example.searchplant.databinding.FragmentOnBoardingBinding

class onBoarding2 : Fragment() {
    lateinit var binding : FragmentOnBoarding2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoarding2Binding.inflate(inflater, container,false)
        binding.btnScreen3.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
            onBoardingFinish()
        }
        return binding.root
    }
    private fun onBoardingFinish(){
        val sharePref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharePref.edit()
        editor.putBoolean("Finish", true)
        editor.apply()
    }
}