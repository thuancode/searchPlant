package com.example.searchplant.view.screen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.searchplant.R
import com.example.searchplant.databinding.FragmentLoginBinding

import com.example.searchplant.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    lateinit var binding : FragmentLoginBinding
    private lateinit var  viewModel : LoginViewModel
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View?{
        auth = FirebaseAuth.getInstance()
        binding = FragmentLoginBinding.inflate(inflater, container,false)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        checkbox()
        binding.textFogot.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetFragment)
        }
        binding.textCreate.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.checkRemember.setOnCheckedChangeListener{_,isChecked->
            if(isChecked)
            {
                checkRemember()
            }else if(!isChecked)
            {
                unCheckRemember()
            }
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val pass = binding.edtPass.text.toString().trim()
            if(email.isEmpty() || pass.isEmpty())
            {
                Toast.makeText(activity,"Không được để trống Email hoặc Password",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.checkEmailAndPassword(email,pass)
            signInWithEmailAndPassword(email,pass)
            Firebase.auth.signOut()
        }
        listenerErrorEvent()
        return binding.root
    }

    private fun checkbox() {
        val sharedPref = requireActivity().getSharedPreferences("remember", Context.MODE_PRIVATE)
        val checkbox = sharedPref.getString("remember","")
        val emailCheck = binding.edtEmail.text.toString().trim()
        val passCheck = binding.edtPass.text.toString().trim()

        if(checkbox.equals("true"))
        {
                if(emailCheck != "" ||passCheck != "")
                {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
        }else if(checkbox.equals("false"))
        {
            Toast.makeText(activity,"Vui lòng đăng nhập",Toast.LENGTH_SHORT).show()
        }
    }

    private fun unCheckRemember() {
        val sharePref = requireActivity().getSharedPreferences("remember", Context.MODE_PRIVATE)
        val editor = sharePref.edit()
        editor.putString("remember","false")
        editor.apply()
    }
    private fun checkRemember() {
        val sharePref = requireActivity().getSharedPreferences("remember", Context.MODE_PRIVATE)
        val editor = sharePref.edit()
        editor.putString("remember","true")
        editor.apply()
    }
    private fun listenerErrorEvent()
    {
        viewModel.isErrorEvent.observe(viewLifecycleOwner)
        {
            Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
        }
    }
    private fun signInWithEmailAndPassword(email:String,pass:String) {
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener(requireActivity()){
                if (it.isSuccessful) {
                    val verification = auth.currentUser?.isEmailVerified
                    if (verification == true) {
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {
                        Toast.makeText(activity, "Vui lòng xác nhận Email", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                }
            }
    }
}