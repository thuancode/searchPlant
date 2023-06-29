package com.example.searchplant.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.searchplant.R
import com.example.searchplant.model.Species



class SpeciesAdapter(var context:Context, private var listSpe:List<Species>):BaseAdapter(){
    private var listAZ = mutableListOf<Char>().apply{addAll(('A'..'Z')+('a'..'z'))} as ArrayList<Char>
    private  var i = 0
    class ViewHolder(row: View) {
        var textSpec: TextView
        var textDeco: TextView

        init {
            textSpec = row.findViewById(R.id.text_itemSpec) as TextView
            textDeco= row.findViewById(R.id.text_decor) as TextView

        }
    }
    override fun getCount(): Int {
        return listSpe.size
    }
    override fun getItem(position: Int): Any {
        return listSpe[position]
    }
    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }
    override fun getView(position: Int, convertView: View?, p2: ViewGroup?): View {
        val view : View?
        val viewHolder : ViewHolder
        if(convertView == null)
        {
            val layoutInflater:LayoutInflater = LayoutInflater.from(context)
            view =layoutInflater.inflate(R.layout.species_item,null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder

        }else{
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }
        val spec:Species = getItem(position) as Species
        val firstSpecies = spec.getSpecies().toString()
        val textFirst = firstSpecies[0]
        if(i == 0)
        {
            viewHolder.textDeco.text = textFirst.toString()
            i+=1
        }else{
            viewHolder.textDeco.text = check(textFirst).toString()
        }
        viewHolder.textSpec.text = spec.getSpecies().toString()
        return view!!
    }
    fun setFilteredList(list : ArrayList<Species>)
    {
        this.listSpe = list
        notifyDataSetChanged()
    }
    private fun check(textFirst:Char):Char
    {   var text : Char = ' '
        for(a in listAZ){
            if (textFirst == a)
                {
                    text = textFirst
                    listAZ.remove(a)
                    return text
                }
        }
        return text
    }
}