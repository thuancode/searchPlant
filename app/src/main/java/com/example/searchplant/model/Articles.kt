package com.example.searchplant.model

import com.google.type.Date

data class Articles ( private var titleArticles:String ?= null,
                      private var imageArticles: String?= null,
                      private var Description:String ?= null,
                      private var properties:String ?= null,
                      private var type:String ?= null,
                      private var userPost:String ?= null,
                      private var datePost:String ?= null){
    fun getTitleArticles(): String? {
        return this.titleArticles
    }
    fun getImageArticles(): String? {
        return this.imageArticles
    }
    fun getDescription(): String? {
        return this.Description
    }
    fun getProperties(): String? {
        return this.properties
    }
    fun getType(): String? {
        return this.type
    }
    fun getUserPost(): String? {
        return this.userPost
    }
    fun getDatePost(): String? {
        return this.datePost
    }
}