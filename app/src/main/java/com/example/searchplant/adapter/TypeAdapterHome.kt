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

class TypeAdapterHome(private var listPlant:List<Species>):
    RecyclerView.Adapter<TypeAdapterHome.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeAdapterHome.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_types,parent,false)
        return TypeAdapterHome.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TypeAdapterHome.ViewHolder, position: Int) {
        val currentItem =  listPlant[position]
        holder.textType.text = currentItem.getType().toString().trim()

        val storageRef = FirebaseStorage.getInstance().reference.child("plant/${currentItem.getPostID()}.jpg")
        val localFile = File.createTempFile("tempImage","jpg")
        storageRef.getFile(localFile).addOnCompleteListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.imageType.setImageBitmap(bitmap)
        }
    }

    override fun getItemCount(): Int {
        return listPlant.size
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val imageType = itemView.findViewById(R.id.imageTypeHome) as ImageView
        val textType = itemView.findViewById(R.id.textTypeHome) as TextView
        val textType1 = itemView.findViewById(R.id.textTypeHome) as TextView

    }
}