package com.example.searchplant.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.searchplant.R
import com.example.searchplant.model.Articles
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class SaveArticlesAdapterProfile (private val listArticles:ArrayList<Articles>)
    : RecyclerView.Adapter<SaveArticlesAdapterProfile.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SaveArticlesAdapterProfile.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.species_profile_item,parent,false)
        return SaveArticlesAdapterProfile.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listArticles[position]
        holder.textPlant.text = currentItem.getTitleArticles().toString().trim()
        val storageRef = FirebaseStorage.getInstance().reference.child("articles/${currentItem.getImageArticles()}.jpg")
        val localFile = File.createTempFile("tempImage","jpg")
        storageRef.getFile(localFile).addOnCompleteListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.imagePlant.setImageBitmap(bitmap)
        }
    }



    override fun getItemCount(): Int {
        return listArticles.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imagePlant = itemView.findViewById(R.id.imageSpeciesPro) as ImageView
        val textPlant = itemView.findViewById(R.id.textName) as TextView
    }
}