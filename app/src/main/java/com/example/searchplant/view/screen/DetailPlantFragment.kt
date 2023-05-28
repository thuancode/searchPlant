package com.example.searchplant.view.screen

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
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
import com.example.searchplant.databinding.FragmentDetailPlantBinding
import com.example.searchplant.databinding.FragmentListSpeciesBinding
import com.example.searchplant.model.Species
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

class DetailPlantFragment : Fragment() {
    lateinit var binding: FragmentDetailPlantBinding
    private var db = Firebase.firestore
    private val REQUEST_IMAGE_CAPTURE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailPlantBinding.inflate(inflater, container,false)
        val btnNavView = binding.bottomNavigationView1
        btnNavView.background = null

        parentFragmentManager.setFragmentResultListener("Plant",this, FragmentResultListener { requestKey, result ->
            val textData = result.getString("plant").toString()
            getDataPlant(textData)
        })

        binding.btnAdd.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }catch (e: ActivityNotFoundException)
            {
                Toast.makeText(requireActivity(),"Error: "+e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
        binding.bottomNavigationView1.setOnNavigationItemReselectedListener{
            when(it.itemId) {
                R.id.home -> {
                    findNavController().navigate(R.id.action_detailPlantFragment_to_homeFragment)
                }
                R.id.profile -> {
                    findNavController().navigate(R.id.action_detailPlantFragment_to_profileFragment)
                }
            }
        }
        return binding.root
    }


    private fun sendData(textSend:String)
    {
        val bundle = Bundle()
        bundle.putString("plant_back",textSend)
        parentFragmentManager.setFragmentResult("Plant_back",bundle)
    }
    private fun getDataPlant(textData: String){
        db = FirebaseFirestore.getInstance()
        db.collection("SPECIES")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result) {
                            val myData = document.toObject(Species::class.java)
                            if(myData.getNamePlant().toString() == textData)
                            {
                                binding.textNamePlant.text = myData.getNamePlant()
                                binding.textDescription.text = myData.getDescription()
                                binding.textFamily.text = myData.getFamily()
                                binding.textKingDom.text = myData.getKingDom()
                                binding.textProperti.text = myData.getProperties()
                                binding.textType.text = myData.getType()
                                val sharedPref = requireActivity().getSharedPreferences("sendPostID", Context.MODE_PRIVATE)
                                val postID = sharedPref.getString("postID","")
                                if (postID != null) {
                                    getDataHeart(postID,myData.getPostID().toString())
                                }
                                sendData(myData.getSpecies().toString())
                                val storageRef = FirebaseStorage.getInstance().reference.child("plant/${myData.getPostID()}.jpg")
                                val localFile = File.createTempFile("tempPlant","jpg")
                                storageRef.getFile(localFile).addOnCompleteListener {
                                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                    binding.imagePlant.setImageBitmap(bitmap)
                                }
                            }
                        }
                    }
                }
            }
    private fun getDataHeart(postIDEmail:String, postIDSpe:String) {
        Log.d(ContentValues.TAG, "---------------------$postIDEmail....$postIDSpe")
        db = FirebaseFirestore.getInstance()
        db.collection("USER").document(postIDEmail).get().addOnSuccessListener {
            val list = it.data?.get("listLike") as ArrayList<String>
            if(list.size == 0)
            {
                binding.floatingBtnHeart.setColorFilter(Color.WHITE)
                binding.floatingBtnHeart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.redbtn))
            }
            val tempList = ArrayList<String>()
            for(id in list){
                Log.d(ContentValues.TAG, id.toString())
                if(id != postIDSpe) {
                    binding.floatingBtnHeart.setColorFilter(Color.WHITE)
                    binding.floatingBtnHeart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.redbtn))
                }else {
                    binding.floatingBtnHeart.setColorFilter(Color.RED)
                    binding.floatingBtnHeart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
                }
                tempList.add(id)
            }
        }
        binding.floatingBtnHeart.setOnClickListener {



            db.collection("USER").document(postIDEmail).get().addOnSuccessListener {
                val list = it.data?.get("listLike") as ArrayList<String>
                if(list.contains(postIDSpe))
                {
                    list.remove(postIDSpe)
                    binding.floatingBtnHeart.setColorFilter(Color.WHITE)
                    binding.floatingBtnHeart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.redbtn))
                }else
                {
                    list.add(postIDSpe)
                    binding.floatingBtnHeart.setColorFilter(Color.RED)
                    binding.floatingBtnHeart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
                }
                db.collection("USER").document(postIDEmail)
                    .update("listLike", list)
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == Activity.RESULT_OK)){

            val imageBitmap = data?.extras?.get("data") as Bitmap
            sendDataImage(imageBitmap)
            findNavController().navigate(R.id.action_detailPlantFragment_to_addNewPlantFragment)
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

