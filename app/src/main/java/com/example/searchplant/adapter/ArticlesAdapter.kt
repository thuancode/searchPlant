package com.example.searchplant.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.searchplant.R
import com.example.searchplant.R.*
import com.example.searchplant.model.Articles
import com.example.searchplant.model.Species
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class ArticlesAdapter(private var listArticle:List<Articles>,private var listLike:ArrayList<String>,private var listSave:ArrayList<String>,private val idUser:String)
    :RecyclerView.Adapter<ArticlesAdapter.ViewHolder>(){
    private var db = Firebase.firestore
    private lateinit var mListener : ArticlesAdapter.onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnClickItemListener(listener: onItemClickListener)
    {
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.article_item,parent,false)
        return ViewHolder(itemView,mListener)
    }

    override fun getItemCount(): Int {
        return listArticle.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        db = FirebaseFirestore.getInstance()
        val currentItem =  listArticle[position]
        val storageRef1 = FirebaseStorage.getInstance().reference.child("avatar/${currentItem.getUserPost()}.jpg")
        val localFile1 = File.createTempFile("tempImage1","jpg")
        storageRef1.getFile(localFile1).addOnCompleteListener {
            val bitmap1 = BitmapFactory.decodeFile(localFile1.absolutePath)
            holder.userPost.setImageBitmap(bitmap1)
        }
        holder.titleArticle.text = currentItem.getTitleArticles().toString().trim()
        holder.datePost.text = currentItem.getDatePost().toString().trim()
        val postId = currentItem.getImageArticles()
        holder.likeArticle.isChecked = postId in listLike
        holder.saveArticle.isChecked = postId in listSave
        holder.saveArticle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                listSave.remove(postId)
            } else {
                listSave.add(postId.toString())
            }
            db.collection("USER").document(idUser).update("listSave",listSave)
        }

        holder.likeArticle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                listLike.remove(postId)
            } else {
                listLike.add(postId.toString())
            }
            db.collection("USER").document(idUser).update("listLikeArt",listLike)

        }
        db.collection("USER").document(currentItem.getUserPost().toString()).get().addOnSuccessListener {
            holder.userNamePost.text = it.data?.get("fullName").toString()
        }
        val storageRef = FirebaseStorage.getInstance().reference.child("articles/${currentItem.getImageArticles()}.jpg")
        val localFile = File.createTempFile("tempImage","jpg")
        storageRef.getFile(localFile).addOnCompleteListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.imageArticle.setImageBitmap(bitmap)
        }

    }

    class ViewHolder(itemView: View,listener: onItemClickListener):RecyclerView.ViewHolder(itemView) {
        val imageArticle = itemView.findViewById(R.id.ivArticle) as ImageView
        val titleArticle = itemView.findViewById(R.id.tvArticleContent) as TextView
        val likeArticle = itemView.findViewById(R.id.checkHeart) as CheckBox
        val saveArticle = itemView.findViewById(R.id.checkSave) as CheckBox
        val userPost = itemView.findViewById(R.id.ivUser) as CircleImageView
        val userNamePost = itemView.findViewById(R.id.tvUserName) as TextView
        val datePost = itemView.findViewById(R.id.tvDate) as TextView


        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(list : ArrayList<Articles>)
    {
        this.listArticle = list
        notifyDataSetChanged()
    }
}