package com.example.searchplant.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.searchplant.databinding.FragmentSpeciesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class serviceSpecies() : interfaceSpecies {
    lateinit var binding: FragmentSpeciesBinding
    override fun sortSpecies(sPec: ArrayList<Species>): ArrayList<Species>{
        val list = sPec.distinctBy { it.getSpecies() } as ArrayList<Species>
        val listSort = list.sortedBy { it.getSpecies() }
        return ArrayList(listSort)
    }

    override fun printSpecies(inflater: LayoutInflater, container: ViewGroup?, context: Context) {
        TODO("Not yet implemented")
    }
}
