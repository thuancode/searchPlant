package com.example.searchplant.view.screen

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchplant.R
import com.example.searchplant.adapter.SpeciesAdapterProfile
import com.example.searchplant.databinding.FragmentDetailPlantBinding
import com.example.searchplant.databinding.FragmentHomeBinding
import com.example.searchplant.databinding.FragmentSpeciesBinding
import com.example.searchplant.databinding.FragmentSpeciesProfileBinding
import com.example.searchplant.model.Species
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SpeciesProfileFragment : Fragment() {
    lateinit var binding : FragmentSpeciesProfileBinding
    private var db = Firebase.firestore
    private var list : ArrayList<Species> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpeciesProfileBinding.inflate(inflater, container,false)
        val sharedPref = requireActivity().getSharedPreferences("sendPostID", Context.MODE_PRIVATE)
        val postID = sharedPref.getString("postID","")
        if (postID != null) {
            Log.d(ContentValues.TAG, "Proooooooooooooooooooooooooo::$postID")
            getDataSpecies(postID)
        }

        return binding.root
    }

    private fun getDataSpecies(postID: String?) {
        db = FirebaseFirestore.getInstance()
        db.collection("USER").document(postID.toString()).get().addOnSuccessListener {
            val listSpe = it.data?.get("listLike") as ArrayList<String>
            Log.d(ContentValues.TAG, "listLike::$listSpe")
            for(postId in listSpe)
            {
                db.collection("SPECIES").document(postId).get().addOnCompleteListener { task ->
                    if(task.isSuccessful)
                    {
                        val myData = task.result.toObject(Species::class.java)
                        if (myData != null) {
                            list.add(myData)
                            Log.d(ContentValues.TAG, "Proooooooooooooooooooooooooo::$myData")
                            val adapter = SpeciesAdapterProfile(list)
                            binding.listSpeciecsPro.layoutManager = LinearLayoutManager(requireContext())
                            binding.listSpeciecsPro.adapter = adapter
                            adapter.setOnItemClickListener(object : SpeciesAdapterProfile.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    sendData(list[position].getNamePlant().toString())
                                    Log.d(ContentValues.TAG, "-------------THUAN--------$list")
                                    findNavController().navigate(R.id.action_profileFragment_to_detailPlantFragment)
                                }
                            })
                        }
                    }
                }
            }
        }
    }
    private fun sendData(textSend:String)
    {
        val bundle = Bundle()
        bundle.putString("plant",textSend)
        Log.d(ContentValues.TAG, "-----------THUANN----------$textSend")
        parentFragmentManager.setFragmentResult("Plant",bundle)
    }
}