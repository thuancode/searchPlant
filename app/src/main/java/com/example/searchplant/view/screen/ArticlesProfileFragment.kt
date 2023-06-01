package com.example.searchplant.view.screen

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchplant.R
import com.example.searchplant.adapter.LikeArticlesAdapterProfile
import com.example.searchplant.databinding.FragmentArticlesProfileBinding
import com.example.searchplant.databinding.FragmentLikesProfileBinding
import com.example.searchplant.model.Articles
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ArticlesProfileFragment : Fragment() {
    private var list : ArrayList<Articles> = arrayListOf()
    private var db = Firebase.firestore
    lateinit var binding: FragmentArticlesProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentArticlesProfileBinding.inflate(inflater, container,false)
        val sharedPref = requireActivity().getSharedPreferences("sendPostID", Context.MODE_PRIVATE)
        val postID = sharedPref.getString("postID","")
        if (postID != null) {
            Log.d(ContentValues.TAG, "Proooooooooooooooooooooooooo::$postID")
            getDataArticles(postID)
        }
        return binding.root
    }

    private fun getDataArticles(postID: String) {
        db = FirebaseFirestore.getInstance()
        db.collection("USER").document(postID.toString()).get().addOnSuccessListener {
            val listArt = it.data?.get("listSave") as ArrayList<String>
            for(postId in listArt)
            {
                db.collection("ARTICLES").document(postId).get().addOnCompleteListener { task ->
                    if(task.isSuccessful)
                    {
                        val myData = task.result.toObject(Articles::class.java)
                        if (myData != null) {
                            list.add(myData)
                            Log.d(ContentValues.TAG, "Proooooooooooooooooooooooooo::$myData")
                            val adapter = LikeArticlesAdapterProfile(list)
                            binding.listArticlesSave.layoutManager = LinearLayoutManager(requireContext())
                            binding.listArticlesSave.adapter = adapter
                        }
                    }
                }
            }
        }
    }
}