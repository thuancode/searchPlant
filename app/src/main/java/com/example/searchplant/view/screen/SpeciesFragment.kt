package com.example.searchplant.view.screen

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.searchplant.R
import com.example.searchplant.adapter.SpeciesAdapter
import com.example.searchplant.databinding.FragmentSpeciesBinding
import com.example.searchplant.model.Species
import com.example.searchplant.model.serviceSpecies
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SpeciesFragment : Fragment() {
    lateinit var binding : FragmentSpeciesBinding
    private var db = Firebase.firestore
    private val REQUEST_IMAGE_CAPTURE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpeciesBinding.inflate(inflater, container,false)
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_speciesFragment_to_homeFragment)
        }
        binding.btnAdd.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }catch (e: ActivityNotFoundException)
            {
                Toast.makeText(requireActivity(),"Error: "+e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
        val btnNavView = binding.bottomNavigationView1
        btnNavView.background = null
        val btnBack = binding.btnBack
        btnBack.background = null
        getDataSpecies()

        binding.bottomNavigationView1.setOnNavigationItemReselectedListener{
            when(it.itemId) {
                R.id.home -> {
                    findNavController().navigate(R.id.action_speciesFragment_to_homeFragment)
                }
                R.id.profile -> {
                    findNavController().navigate(R.id.action_speciesFragment_to_profileFragment)
                }
            }
        }
        return binding.root
    }
    private fun getDataSpecies(){
        var list: ArrayList<Species> = ArrayList()
        db = FirebaseFirestore.getInstance()
        db.collection("SPECIES")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result) {
                        val myData = document.toObject(Species::class.java)
                        list.add(myData)
                        Log.d(TAG, "---------------------$myData")
                        val serSpec = serviceSpecies()
                        list = serSpec.sortSpecies(list)
                        val adapter = SpeciesAdapter(requireActivity(),list)
                        binding.listSpec.adapter = adapter
                        binding.listSpec.isClickable = true
                        binding.listSpec.setOnItemClickListener { parent, vixew, position, id ->
                            sendData(list[position].getSpecies().toString())
                            findNavController().navigate(R.id.action_speciesFragment_to_listSpeciesFragment)
                        }
                }
            }
        }
    }
    private fun sendData(textSend:String)
    {
        val bundle = Bundle()
        bundle.putString("data",textSend)
        parentFragmentManager.setFragmentResult("species",bundle)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == Activity.RESULT_OK)){

            val imageBitmap = data?.extras?.get("data") as Bitmap
            sendDataImage(imageBitmap)
            findNavController().navigate(R.id.action_speciesFragment_to_addNewPlantFragment)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun sendDataImage(image: Bitmap)
    {
        val bundle = Bundle()
        bundle.putParcelable("imagePlant", Species(null,null,null,null,null,null,null,null,image))
        parentFragmentManager.setFragmentResult("imagePlant1",bundle)
    }
}