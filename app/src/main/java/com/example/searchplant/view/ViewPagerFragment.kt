package com.example.searchplant.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.searchplant.adapter.ViewPagerAdapter
import com.example.searchplant.databinding.FragmentViewPagerBinding
import com.example.searchplant.view.screen.onBoarding
import com.example.searchplant.view.screen.onBoarding1
import com.example.searchplant.view.screen.onBoarding2

class ViewPagerFragment : Fragment() {
    lateinit var binding : FragmentViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentViewPagerBinding.inflate(inflater, container,false)
        val fragmentList = arrayListOf<Fragment>(
            onBoarding(),
            onBoarding1(),
            onBoarding2()
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.viewPagerId.adapter = adapter
        return binding.root
    }

}