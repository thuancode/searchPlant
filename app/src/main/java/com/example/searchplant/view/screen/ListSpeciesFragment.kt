package com.example.searchplant.view.screen

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.searchplant.R
import com.example.searchplant.adapter.PlantAdapter
import com.example.searchplant.databinding.FragmentListSpeciesBinding
import com.example.searchplant.model.Species
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListSpeciesFragment : Fragment() {
    lateinit var binding: FragmentListSpeciesBinding
    private var db = Firebase.firestore
    private val REQUEST_IMAGE_CAPTURE = 100

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

        binding.btnAdd.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }catch (e: ActivityNotFoundException)
            {
                Toast.makeText(requireActivity(),"Error: "+e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
        parentFragmentManager.setFragmentResultListener("species",this, FragmentResultListener { requestKey, result ->
             textData = result.getString("data").toString()
             binding.titleSpec.text = textData
             getDataPlant(textData)
        })
        parentFragmentManager.setFragmentResultListener("Plant_back",this, FragmentResultListener { requestKey, result ->
            textData = result.getString("plant_back").toString()
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
                    findNavController().navigate(R.id.action_speciesFragment_to_profileFragment)
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
                            binding.listPlant.isClickable = true
                            binding.listPlant.setOnItemClickListener { parent, view, position, id ->
                                sendData(list[position].getNamePlant().toString())
                                findNavController().navigate(R.id.action_listSpeciesFragment_to_detailPlantFragment)
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
        parentFragmentManager.setFragmentResult("Plant",bundle)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == Activity.RESULT_OK)){

            val imageBitmap = data?.extras?.get("data") as Bitmap
            sendDataImage(imageBitmap)
            findNavController().navigate(R.id.action_listSpeciesFragment_to_addNewPlantFragment)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun sendDataImage(image: Bitmap)
    {
        val bundle = Bundle()
        bundle.putParcelable("imagePlant",Species(null,null,null,null,null,null,null,null,image))
        parentFragmentManager.setFragmentResult("imagePlant1",bundle)
    }
}
