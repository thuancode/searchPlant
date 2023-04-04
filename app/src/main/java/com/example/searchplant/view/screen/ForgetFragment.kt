package com.example.searchplant.view.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.searchplant.R
import com.example.searchplant.databinding.FragmentForgetBinding
import com.example.searchplant.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ForgetFragment : Fragment() {
    lateinit var binding : FragmentForgetBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgetBinding.inflate(inflater, container,false)
        auth = Firebase.auth

        binding.btnResetPass.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            if(email.isEmpty())
            {
                Toast.makeText(activity, "Vui lòng nhập địa chỉ Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if(!checkEmail(email))
            {
                Toast.makeText(activity, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            resetPass(email)
        }
        Firebase.auth.signOut()
        return binding.root
    }
    private fun checkEmail(eMail:String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(eMail).matches()
    }
    private fun resetPass(eMail:String) {
        auth.sendPasswordResetEmail(eMail)
            .addOnCompleteListener{
                Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
            }    }
}