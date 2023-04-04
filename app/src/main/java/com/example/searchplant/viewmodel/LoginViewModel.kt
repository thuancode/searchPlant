package com.example.searchplant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel(){
    private var _isSuccessEvent : MutableLiveData<Boolean> = MutableLiveData()
    val isSuccessEvent : LiveData<Boolean>
        get() = _isSuccessEvent

    private var _isErrorEvent : MutableLiveData<String> = MutableLiveData()
    val isErrorEvent : LiveData<String>
        get() = _isErrorEvent


    fun checkEmailAndPassword(email : String, pass : String)
    {
        if(!emailValid(email))
        {
            _isErrorEvent.postValue("Email không hợp lệ")
            return
        }
        else if(!passValid(pass))
        {
            _isErrorEvent.postValue("Password không hợp lệ")
            return
        }
        _isSuccessEvent.postValue(true)
        return
    }

    private fun emailValid(email:String) : Boolean
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun passValid(password: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{11}$".toRegex()
        return regex.matches(password)
    }

}