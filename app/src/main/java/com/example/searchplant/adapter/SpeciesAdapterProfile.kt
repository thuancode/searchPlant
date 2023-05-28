package com.example.searchplant.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.searchplant.R
import com.example.searchplant.model.Species
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class SpeciesAdapterProfile(private var listPlant:List<Species>):
    RecyclerView.Adapter<SpeciesAdapterProfile.ViewHolder>() {

    private lateinit var mListener:onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.species_profile_item,parent,false)
        return ViewHolder(itemView,mListener)
    }
    override fun getItemCount(): Int {
        return listPlant.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem =  listPlant[position]

        holder.textPlant.text = currentItem.getNamePlant().toString().trim()
        val storageRef = FirebaseStorage.getInstance().reference.child("plant/${currentItem.getPostID()}.jpg")
            val localFile = File.createTempFile("tempImage","jpg")
            storageRef.getFile(localFile).addOnCompleteListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                holder.imagePlant.setImageBitmap(bitmap)
            }
    }
    class ViewHolder(itemView: View,clickListener: onItemClickListener):RecyclerView.ViewHolder(itemView) {

        val imagePlant = itemView.findViewById(R.id.imageSpeciesPro) as ImageView
        val textPlant = itemView.findViewById(R.id.textName) as TextView
        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}