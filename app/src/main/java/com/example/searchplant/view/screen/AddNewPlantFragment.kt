package com.example.searchplant.view.screen

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentResultListener
import com.example.searchplant.databinding.FragmentAddNewPlantBinding
import com.example.searchplant.model.Species
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream



class AddNewPlantFragment : Fragment() {
    lateinit var binding: FragmentAddNewPlantBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var spe :Species
    private lateinit var storageRef : StorageReference
    private var postID : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db = FirebaseFirestore.getInstance()
        binding = FragmentAddNewPlantBinding.inflate(inflater, container,false)
        storageRef = FirebaseStorage.getInstance().reference

        parentFragmentManager.setFragmentResultListener("imagePlant1",this, FragmentResultListener { requestKey, result ->
            spe = result.getParcelable("imagePlant")!!
            binding.imageNewPlant.setImageBitmap(spe.getImagePlant())
        })
        binding.btnDone.setOnClickListener {
            val Type = binding.textType.text.toString()
            val family = binding.textFamily.text.toString()
            val namePlant = binding.textNamePlant.text.toString()
            val kigdom = binding.textKingDom.text.toString()
            val properti = binding.textProperties.text.toString()
            val species = binding.textSpecies.text.toString()
            val descrip = binding.textDescription.text.toString()
            val spec = Species(null,namePlant,species, kigdom, family, descrip ,properti,Type,null)
            pushData(spec){
                val bitmap = spe.getImagePlant()
                if (bitmap != null) {
                    //val name = db.collection("SPECIES").document().id
                    val imageRef = storageRef.child("plant/${it}.jpg")
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnCompleteListener {
                        if (it.isSuccessful) {
                            imageRef.downloadUrl.addOnSuccessListener {
                                val dowloadUrl = it.toString()
                                Toast.makeText(requireContext(), "đăng ảnh thành công", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val exception = it.exception
                        }
                    }

                }
            }

        }
        return binding.root
    }
    private fun pushData(spe : Species,callback:(String)->Unit){
        db.collection("SPECIES")
            .add(spe)
            .addOnSuccessListener {
                postID = it.id
                db.collection("SPECIES")
                    .document(postID!!)
                    .update("postID", postID)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Đăng bài thành công", Toast.LENGTH_SHORT).show()
                        callback(postID!!)
                    }
            }
    }



}