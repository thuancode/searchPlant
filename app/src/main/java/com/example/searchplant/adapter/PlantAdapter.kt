package com.example.searchplant.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.searchplant.R
import com.example.searchplant.model.Species
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class PlantAdapter(var context: Context, private var listPlant:List<Species>): BaseAdapter() {
    class ViewHolder(row: View) {
        var imagePlant : ImageView
        var textPlant: TextView
        var textKingDom: TextView
        var textFamily: TextView
        var textDescription: TextView
        init {
            imagePlant = row.findViewById(R.id.imagePlant) as ImageView
            textPlant = row.findViewById(R.id.text_plant) as TextView
            textKingDom = row.findViewById(R.id.text_kingdom) as TextView
            textFamily = row.findViewById(R.id.text_family) as TextView
            textDescription = row.findViewById(R.id.text_descreiption) as TextView
        }
    }
    override fun getCount(): Int {
        return listPlant.size
    }

    override fun getItem(position: Int): Any {
        return listPlant[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View?
        val viewHolder : ViewHolder
        if(convertView == null)
        {
            val layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view =layoutInflater.inflate(R.layout.plant_item,null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder

        }else{
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }
        val spec:Species = getItem(position) as Species
        val storageRef = FirebaseStorage.getInstance().reference.child("plant/${spec.getPostID()}.jpg")
        val localFile = File.createTempFile("tempImage","jpg")
        storageRef.getFile(localFile).addOnCompleteListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            viewHolder.imagePlant.setImageBitmap(bitmap)
        }

        viewHolder.textPlant.text = spec.getNamePlant()
        viewHolder.textKingDom.text = spec.getKingDom()
        viewHolder.textFamily.text = spec.getFamily()
        viewHolder.textDescription.text = spec.getDescription()
        return view!!
    }
    fun setFilteredList(list : ArrayList<Species>)
    {
        this.listPlant = list
        notifyDataSetChanged()
    }
}