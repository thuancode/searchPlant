package com.example.searchplant.view.screen

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.example.searchplant.R
import com.example.searchplant.databinding.FragmentAddNewArticlesBinding
import com.example.searchplant.databinding.FragmentAddNewPlantBinding
import com.example.searchplant.model.Articles
import com.example.searchplant.model.Species
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.type.Date
import io.grpc.Internal
import java.net.URI
import java.text.SimpleDateFormat

class AddNewArticlesFragment : Fragment() {
    private lateinit var db : FirebaseFirestore
    private var postID : String? = null
    private lateinit var uri : Uri
    private lateinit var binding:FragmentAddNewArticlesBinding
    private lateinit var imageView: ImageView
    companion object{
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        db = FirebaseFirestore.getInstance()
        binding = FragmentAddNewArticlesBinding.inflate(inflater, container,false)
        imageView = binding.imageArtAdd
        binding.btnImage.background = null
        binding.btnImage.setOnClickListener {
            pickImageGallery()
        }
        binding.btnPost.setOnClickListener {

            val sharedPref = requireActivity().getSharedPreferences("sendPostID", Context.MODE_PRIVATE)
            val postUser = sharedPref.getString("postID","")
            if (postUser != null) {
                Log.d(ContentValues.TAG, "Proooooooooooooooooooooooooo::$postID")
                pushDataArticles(postUser)
            }
        }

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun pushDataArticles(postUser: String) {
        val Description = binding.textDescriptionAdd.text.toString()
        val title = binding.textTitle.text.toString()
        val properti = binding.textPropertiesAdd.text.toString()
        val type = binding.textTypeAdd.text.toString()
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(java.util.Date())
        val art = Articles(title,null,Description, properti,type,postUser,currentDate)
        pushData(art){
            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setMessage("Upload Articles ...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            val storageReference = FirebaseStorage.getInstance().getReference("articles/${it}.jpg")
            storageReference.putFile(uri).addOnSuccessListener {
                Toast.makeText(requireContext(),"Successfully uploaded",Toast.LENGTH_SHORT).show()
                if(progressDialog.isShowing) progressDialog.dismiss()
            }.addOnFailureListener{
                Toast.makeText(requireContext(),"Failed",Toast.LENGTH_SHORT).show()
                if(progressDialog.isShowing) progressDialog.dismiss()
            }
        }
    }

    private fun pushData(art : Articles, callback:(String)->Unit){
        db.collection("ARTICLES")
            .add(art)
            .addOnSuccessListener {
                postID = it.id
                db.collection("ARTICLES")
                    .document(postID!!)
                    .update("imageArticles", postID)
                    .addOnSuccessListener {
                        callback(postID!!)
                    }
            }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK)
        {
            uri = data?.data!!
            imageView.setImageURI(data.data)
        }
    }

}