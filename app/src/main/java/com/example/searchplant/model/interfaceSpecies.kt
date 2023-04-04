package com.example.searchplant.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup


interface interfaceSpecies {
    fun sortSpecies(sPec:ArrayList<Species>): ArrayList<Species>
    fun printSpecies(inflater: LayoutInflater, container: ViewGroup?, context: Context)
}