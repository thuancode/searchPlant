package com.example.searchplant.view.screen

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchplant.R
import com.example.searchplant.adapter.SpeciesAdapterProfile
import com.example.searchplant.adapter.TypeAdapterHome
import com.example.searchplant.databinding.FragmentHomeBinding
import com.example.searchplant.model.Species
import com.example.searchplant.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding
    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var db : FirebaseFirestore
    private var listType:ArrayList<String> = arrayListOf()
    private var list : ArrayList<Species> = arrayListOf()

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
        getDataSpecies()
        binding.btnSpe.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_speciesFragment)

        }
        binding.btnIden.setOnClickListener{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }catch (e:ActivityNotFoundException)
            {
                Toast.makeText(requireActivity(),"Error: "+e.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnAdd.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }catch (e:ActivityNotFoundException)
            {
                Toast.makeText(requireActivity(),"Error: "+e.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
        binding.bottomNavigationView.setOnNavigationItemReselectedListener{
            when(it.itemId) {
                R.id.home -> {
                    findNavController().navigate(R.id.action_homeFragment_self)
                }
                R.id.profile -> {
                    findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
                }
            }
        }
        binding.imageAvatar.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
        val sharedPref = requireActivity().getSharedPreferences("sendPostID", Context.MODE_PRIVATE)
        val postID = sharedPref.getString("postID","")
        if (postID != null) {
            getDataUser(postID)
        }
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == RESULT_OK)){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            sendData(imageBitmap)
            findNavController().navigate(R.id.action_homeFragment_to_addNewPlantFragment)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun sendData(image: Bitmap)
    {
        val bundle = Bundle()
        bundle.putParcelable("imagePlant",Species(null,null,null,null,null,null,null,null,image))
        parentFragmentManager.setFragmentResult("imagePlant1",bundle)
    }
    @SuppressLint("SetTextI18n")
    private fun getDataUser(textData: String){
        db = FirebaseFirestore.getInstance()
        db.collection("USER")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result) {
                        val myData = document.toObject(User::class.java)
                        if(myData.getPostID().toString() == textData)
                        {
                            val name = myData.getFullName().toString().split(" ")
                            val name1 = name[name.size - 1]
                            name1.toUpperCase()
                            binding.textView7.text = "HELLO ${name1},"
                            val storageRef = FirebaseStorage.getInstance().reference.child("avatar/${myData.getPostID()}.jpg")
                            val localFile = File.createTempFile("tempPlant","jpg")
                            storageRef.getFile(localFile).addOnCompleteListener {myData.getFullName().toString().split(" ")
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                binding.imageAvatar.setImageBitmap(bitmap)
                            }
                        }
                    }
                }
            }
    }
    private fun getDataSpecies() {
        db = FirebaseFirestore.getInstance()
        db.collection("SPECIES").get().addOnCompleteListener {
            val tempList = ArrayList<Species>()
            val tempListType = ArrayList<String>()
            for (document in it.result) {
                val specie = document.toObject(Species::class.java)
                if(list.size == 0)
                {
                    tempList.add(specie)
                    tempListType.add(specie.getType().toString())

                }else {
                    var isNewType = true
                    for (type in listType) {
                        if (specie.getType().toString() == type) {
                            isNewType = false
                            break
                        }
                    }
                    if (isNewType) {
                        tempList.add(specie)
                        tempListType.add(specie.getType().toString())
                    }
                }
            }
            list.addAll(tempList)
            listType.addAll(tempListType)
            val adapter = TypeAdapterHome(list)
            binding.listPlant.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            binding.listPlant.adapter = adapter
        }
    }
}
