package com.example.searchplant.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Species(private var postID:String ?= null,
                   private var namePlant:String ?= null,
                   private var species: String?= null,
                   private var kingdom:String ?= null,
                   private var Family:String ?= null,
                   private var Description:String ?= null,
                   private var properties:String ?= null,
                   private var type:String ?= null,
                   private var image:Bitmap ?=null
): Parcelable {
    fun setPostID(post: String)
    {
        this.postID = post
    }
    fun getPostID(): String? {
        return this.postID
    }
    fun getImagePlant(): Bitmap? {
        return this.image
    }

    fun setImagePlant(image: Bitmap?) {
        this.image = image
    }
    fun getNamePlant(): String? {
        return this.namePlant
    }

    fun setNamePlant(namePlant1: String) {
        this.namePlant = namePlant1

    }

    fun getSpecies(): String? {
        return this.species
    }

    fun setSpecies(species1: String) {
        this.species = species1
    }

    fun getKingDom(): String? {
        return this.kingdom
    }

    fun setKingDom(kingdom1: String) {
        this.kingdom = kingdom1
    }

    fun getFamily(): String? {
        return this.Family
    }

    fun setFamily(Family1: String) {
        this.Family = Family1
    }

    fun getDescription(): String? {
        return this.Description
    }

    fun setDescription(description1: String) {
        this.Description = description1
    }
    fun getProperties(): String? {
        return this.properties
    }

    fun setProperties(properties1: String) {
        this.properties = properties1
    }
    fun getType(): String? {
        return this.type
    }

    fun setType(Type1: String) {
        this.type = Type1
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Species

        if (namePlant != other.namePlant) return false
        if (species != other.species) return false
        if (kingdom != other.kingdom) return false
        if (Family != other.Family) return false
        if (Description != other.Description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = namePlant.hashCode()
        result = 31 * result + species.hashCode()
        result = 31 * result + kingdom.hashCode()
        result = 31 * result + Family.hashCode()
        result = 31 * result + Description.hashCode()
        return result
    }
}