package com.example.searchplant.view.screen

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchplant.R
import com.example.searchplant.adapter.ArticlesAdapter
import com.example.searchplant.databinding.FragmentArticlesBinding
import com.example.searchplant.model.Articles
import com.example.searchplant.model.Species
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ArticlesFragment : Fragment() {
    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var binding: FragmentArticlesBinding
    private var db = Firebase.firestore
    private var listLike:ArrayList<String> = arrayListOf()
    private var listSave:ArrayList<String> = arrayListOf()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentArticlesBinding.inflate(inflater, container,false)

            binding.btnBack1.setOnClickListener {
                findNavController().navigate(R.id.action_articlesFragment_to_homeFragment)
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
            binding.titleSpec.text = "Articles"
            val btnNavView = binding.bottomNavigationView2
            btnNavView.background = null
            val btnBack = binding.btnBack1
            btnBack.background = null

            getDataArticles()

            binding.bottomNavigationView2.setOnNavigationItemReselectedListener{
                when(it.itemId) {
                    R.id.home -> {
                        findNavController().navigate(R.id.action_articlesFragment_to_homeFragment)
                    }
                    R.id.profile -> {
                        findNavController().navigate(R.id.action_articlesFragment_to_profileFragment)
                    }
                }
            }

            return binding.root
        }
        private fun getDataArticles(){
            var list: ArrayList<Articles> = ArrayList()
            db = FirebaseFirestore.getInstance()
            db.collection("ARTICLES")
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        for (document in it.result) {
                            val myData = document.toObject(Articles::class.java)
                            list.add(myData)
                            val sharedPref = requireActivity().getSharedPreferences("sendPostID", Context.MODE_PRIVATE)
                            val postID = sharedPref.getString("postID","")
                            if (postID != null) {
                                db = FirebaseFirestore.getInstance()
                                db.collection("USER").document(postID).get().addOnSuccessListener { task ->
                                    listLike = task.data?.get("listLikeArt") as ArrayList<String>
                                    listSave = task.data?.get("listSave") as ArrayList<String>
                                    Log.d(ContentValues.TAG, "--------DEPZAIII2--------------${listSave},...$listLike")
                                    var adapter = ArticlesAdapter(list,listLike,listSave,postID)
                                    binding.listArticles.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                                    binding.listArticles.adapter = adapter
                                    adapter.setOnClickItemListener(object : ArticlesAdapter.onItemClickListener{
                                        override fun onItemClick(position: Int) {
                                            val postIDcheck = list[position].getImageArticles().toString()
                                            sendData(list[position].getImageArticles().toString())
                                            Log.d(ContentValues.TAG, "---------------------$postIDcheck")
                                            findNavController().navigate(R.id.action_articlesFragment_to_detailArticlesFragment)
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
            bundle.putString("data",textSend)
            parentFragmentManager.setFragmentResult("articles",bundle)
        }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == Activity.RESULT_OK)){

            val imageBitmap = data?.extras?.get("data") as Bitmap
            sendDataImage(imageBitmap)
            findNavController().navigate(R.id.action_articlesFragment_to_addNewPlantFragment)
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
