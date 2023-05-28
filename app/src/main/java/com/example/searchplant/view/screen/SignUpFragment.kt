package com.example.searchplant.view.screen

import android.content.Intent
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
import com.example.searchplant.databinding.FragmentSignUpBinding
import com.example.searchplant.model.User
import com.example.searchplant.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class SignUpFragment : Fragment() {
    lateinit var binding : FragmentSignUpBinding
    private lateinit var  viewModel : LoginViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container,false)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.btnSignUp.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPass.text.toString().trim()
            val pHone = binding.edtPhone.text.toString().trim()
            val fullName = binding.edtFullName.text.toString().trim()
            if(email.isEmpty() || password.isEmpty()||pHone.isEmpty()||fullName.isEmpty())
            {
                Toast.makeText(activity,"Không được để trống", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.checkEmailAndPassword(email,password)
            listenerErrorEvent()
            listenerSuccessEvent()
        }
        return binding.root
    }
    private fun listenerErrorEvent()
    {
        viewModel.isErrorEvent.observe(viewLifecycleOwner)
        {
            Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
        }
        return
    }
    private fun listenerSuccessEvent()
    {
        viewModel.isSuccessEvent.observe(viewLifecycleOwner)
        {
            if(it)
            {
                val email = binding.edtEmail.text.toString().trim()
                val password = binding.edtPass.text.toString().trim()
                val pHone = binding.edtPhone.text.toString().trim()
                val fullName = binding.edtFullName.text.toString().trim()
                val address = binding.edtAddress.text.toString().trim()
                createAccount(email,password,fullName,pHone,address)
            }
        }
        return
    }

    private fun createAccount(eMail:String, passWord:String, fullName: String, pHone:String,Address:String)
    {
        val listLike:ArrayList<String> = arrayListOf()
        val listLikeArt:ArrayList<String> = arrayListOf()
        val listSave:ArrayList<String> = arrayListOf()

        val user = User(null,eMail,fullName, pHone, passWord,Address,listLike,listLikeArt,listSave)

        db.collection("USER").whereEqualTo("email",eMail).get().addOnSuccessListener { task->
            if(task.isEmpty)
            {
                auth.createUserWithEmailAndPassword(eMail, passWord)
                    .addOnCompleteListener(requireActivity()){ it ->
                        if (it.isSuccessful) {
                            auth.currentUser?.sendEmailVerification()
                                ?.addOnSuccessListener {
                                    db.collection("USER")
                                        .add(user)
                                        .addOnSuccessListener{
                                            val postID = it.id
                                            db.collection("USER")
                                                .document(postID)
                                                .update("postID", postID)
                                                .addOnSuccessListener {
                                                    Toast.makeText(activity, "Xác nhận Email đăng ký", Toast.LENGTH_SHORT).show()
                                                    findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                                                }
                                        }
                                }
                                ?.addOnFailureListener {
                                    Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(activity, "Gmail này đã tồn tại", Toast.LENGTH_SHORT).show()

                    }
            }
        }

    }

}