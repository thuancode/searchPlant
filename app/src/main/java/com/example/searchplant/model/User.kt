package com.example.searchplant.model
data class User(private var postID:String ?= null,
                private var email:String ?= null,
                private var fullName:String ?= null,
                private var Phone: String?= null,
                private var password:String ?= null,
                private var address:String ?= null,
                private var ListFollow:ArrayList<String> ?= null,
                private var ListLike:ArrayList<String> ?= null,
                private var ListLikeArt:ArrayList<String> ?= null,
                private var ListSave:ArrayList<String> ?= null) {

    fun getPostID(): String?
    {
        return this.postID
    }
    fun getListFollow(): ArrayList<String>?
    {
        return this.ListFollow
    }
    fun getEmail(): String? {
        return this.email
    }
    fun getFullName():String?{
        return this.fullName
    }
    fun getPhone():String?{
        return this.Phone
    }
    fun getPassword():String?{
        return this.password
    }
    fun getAddress():String?{
        return this.address
    }
    fun getListLike(): ArrayList<String>?
    {
        return this.ListLike
    }
    fun getListSave(): ArrayList<String>?
    {
        return this.ListSave
    }
    fun getListLikeArt(): ArrayList<String>?
    {
        return this.ListLikeArt
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (postID != other.postID) return false
        if (email != other.email) return false
        if (fullName != other.fullName) return false
        if (Phone != other.Phone) return false
        if (password != other.password) return false
        if (address != other.address) return false
        if (ListLike != other.ListLike) return false
        if (ListLikeArt != other.ListLikeArt) return false
        if (ListSave != other.ListSave) return false

        return true
    }

    override fun hashCode(): Int {
        var result = postID?.hashCode() ?: 0
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (fullName?.hashCode() ?: 0)
        result = 31 * result + (Phone?.hashCode() ?: 0)
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (ListLike?.hashCode() ?: 0)
        result = 31 * result + (ListLikeArt?.hashCode() ?: 0)
        result = 31 * result + (ListSave?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "User(postID=$postID, email=$email, fullName=$fullName, Phone=$Phone, password=$password, address=$address, ListLike=$ListLike, ListLikeArt=$ListLikeArt, ListSave=$ListSave)"
    }


}