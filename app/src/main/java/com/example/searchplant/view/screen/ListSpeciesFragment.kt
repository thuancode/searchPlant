package com.example.searchplant.view.screen

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.searchplant.R
import com.example.searchplant.adapter.PlantAdapter
import com.example.searchplant.databinding.FragmentListSpeciesBinding
import com.example.searchplant.model.Species
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListSpeciesFragment : Fragment() {
    lateinit var binding: FragmentListSpeciesBinding
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var textData : String = ""
        binding = FragmentListSpeciesBinding.inflate(inflater, container,false)
        val btnNavView = binding.bottomNavigationView2
        btnNavView.background = null
        val btnBack = binding.btnBack1
        btnBack.background = null
        parentFragmentManager.setFragmentResultListener("species",this, FragmentResultListener { requestKey, result ->
             textData = result.getString("data").toString()
             binding.titleSpec.text = textData
             getDataPlant(textData)
        })
        binding.btnBack1.setOnClickListener{
            findNavController().navigate(R.id.action_listSpeciesFragment_to_speciesFragment)
        }
        binding.bottomNavigationView2.setOnNavigationItemReselectedListener{
            when(it.itemId) {
                R.id.home -> {
                    findNavController().navigate(R.id.action_speciesFragment_to_homeFragment)
                }
                R.id.profile -> {
                    // Respond to navigation item 2 reselection
                }
            }
        }


        return binding.root
    }

    private fun getDataPlant(key:String){
        var list: ArrayList<Species> = ArrayList()
        db = FirebaseFirestore.getInstance()
        db.collection("SPECIES")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result) {
                        val myData = document.toObject(Species::class.java)
                        if(myData.getSpecies().toString() == key)
                        {
                            list.add(myData)
                            Log.d(ContentValues.TAG, "---------------------$myData")
                            val adapter = PlantAdapter(requireActivity(),list)
                            binding.listPlant.adapter = adapter
                        }
                    }
                }
            }

    }

}
