package com.example.searchplant.view.screen

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
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
import androidx.navigation.fragment.findNavController
import com.example.searchplant.R
import com.example.searchplant.databinding.FragmentDetailArticlesBinding
import com.example.searchplant.model.Articles
import com.example.searchplant.model.Species
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class DetailArticlesFragment : Fragment() {
    lateinit var binding: FragmentDetailArticlesBinding
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailArticlesBinding.inflate(inflater, container,false)
        binding.bottomNavigationView2.background = null

        parentFragmentManager.setFragmentResultListener("articles",this,{requestKey,result->
            val textData = result.getString("data").toString()
            getDataArticles(textData)
        })


        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_detailArticlesFragment_to_addNewArticlesFragment)
        }

        binding.bottomNavigationView2.setOnNavigationItemReselectedListener{
            when(it.itemId) {
                R.id.home -> {
                    findNavController().navigate(R.id.action_detailArticlesFragment_to_homeFragment)
                }
                R.id.profile -> {
                    findNavController().navigate(R.id.action_detailArticlesFragment_to_profileFragment)
                }
            }
        }

        return binding.root
    }
    private fun getDataArticles(textData: String) {
        db = FirebaseFirestore.getInstance()
        db.collection("ARTICLES")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    for(document in it.result)
                    {
                        val myData = document.toObject(Articles::class.java)
                        if(myData.getImageArticles().toString() == textData)
                        {
                            binding.textTitleArt.text = myData.getTitleArticles()
                            binding.textType.text = myData.getType()
                            binding.textProperties.text = myData.getProperties()
                            binding.textDescription.text = myData.getDescription()
                            binding.tvDate.text = myData.getDatePost()
                            db.collection("USER").document(myData.getUserPost().toString()).get().addOnSuccessListener {task->
                                binding.tvUserName.text = task.data?.get("fullName").toString()

                            }

                            val storageRef1 = FirebaseStorage.getInstance().reference.child("avatar/${myData.getUserPost()}.jpg")
                            val localFile1 = File.createTempFile("avatarUser","jpg")
                            storageRef1.getFile(localFile1).addOnCompleteListener{
                                val bitmap = BitmapFactory.decodeFile(localFile1.absolutePath)
                                binding.avatarUser.setImageBitmap(bitmap)
                            }

                            val sharedPref = requireActivity().getSharedPreferences("sendPostID", Context.MODE_PRIVATE)
                            val postID = sharedPref.getString("postID","")

                            if (postID != null) {
                                getDataHeart(postID,myData.getImageArticles().toString())
                                setUserFollow(myData.getUserPost().toString(),postID)
                            }

                            val storageRef = FirebaseStorage.getInstance().reference.child("articles/${myData.getImageArticles()}.jpg")
                            val localFile = File.createTempFile("tempArticles","jpg")
                            storageRef.getFile(localFile).addOnCompleteListener{
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                binding.imageArticles.setImageBitmap(bitmap)
                            }

                        }
                    }
                }
            }
    }

    private fun setUserFollow(idUser: String,idFollow :String) {

        binding.button.setOnClickListener {
            db = FirebaseFirestore.getInstance()
            db.collection("USER").document(idUser).get().addOnSuccessListener {
                val listFollow = it.data?.get("listFollow") as ArrayList<String>
                if (listFollow.contains(idFollow)) {
                    listFollow.remove(idFollow)
                } else {
                    listFollow.add(idFollow)
                }
                db.collection("USER").document(idUser)
                    .update("listFollow", listFollow)
            }
        }
    }

    private fun getDataHeart(postIDEmail:String, postIDSpe:String) {
        Log.d(ContentValues.TAG, "---------------------$postIDEmail....$postIDSpe")
        db = FirebaseFirestore.getInstance()
        db.collection("USER").document(postIDEmail).get().addOnSuccessListener {
            val list = it.data?.get("listLikeArt") as ArrayList<String>
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
                val list = it.data?.get("listLikeArt") as ArrayList<String>
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
                    .update("listLikeArt", list)
            }
        }
    }

    private fun sendDataImage(image: Bitmap)
    {
        val bundle = Bundle()
        bundle.putParcelable("imagePlant", Species(null,null,null,null,null,null,null,null,image))
        parentFragmentManager.setFragmentResult("imagePlant1",bundle)
    }
}