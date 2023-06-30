package com.example.searchplant.view.screen

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.searchplant.databinding.FragmentInformationBinding
import com.example.searchplant.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class InformationFragment : Fragment() {
    private lateinit var db : FirebaseFirestore
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var binding: FragmentInformationBinding
    private lateinit var postId : String

    companion object{
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentInformationBinding.inflate(inflater, container,false)



        binding.camera.setOnClickListener {
            camera()
        }

        binding.txtEdit.setOnClickListener {
            create()
        }



        val sharedPref = requireActivity().getSharedPreferences("sendPostID", Context.MODE_PRIVATE)
        val postID = sharedPref.getString("postID","")
        if (postID != null) {
            user(postID)
            postId = postID
        }
        return binding.root
    }

    private fun camera() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK)
       {
           binding.img.setImageURI(data?.data)
           updateImage(data?.data,postId)
       }
    }

    private fun updateImage(data: Uri?, postId: String) {
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Upload Avatar ...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val storageReference = FirebaseStorage.getInstance().getReference("avatar/${postId}.jpg")
        storageReference.putFile(data!!).addOnSuccessListener {
            Toast.makeText(requireContext(),"Successfully uploaded", Toast.LENGTH_SHORT).show()
            if(progressDialog.isShowing) progressDialog.dismiss()
        }.addOnFailureListener{
            Toast.makeText(requireContext(),"Failed", Toast.LENGTH_SHORT).show()
            if(progressDialog.isShowing) progressDialog.dismiss()
        }
    }

    private fun create() {
        val editText = binding.edtDiachi
        val textValue = editText.text.toString()

        val editTen = binding.edtName
        val textTen = editTen.text.toString()

        val editSdt = binding.edtSdt
        val textSdt = editSdt.text.toString()


        val sharedPref = requireActivity().getSharedPreferences("sendPostID", Context.MODE_PRIVATE)
        val postID = sharedPref.getString("postID","")
       // val currentUser = mAuth.currentUser
        val documentRef = db.collection("USER").document(postID.toString())
        documentRef.update(
            "address", textValue,
            "fullName", textTen,
            "phone", textSdt
        )
    }

    private fun user(textData: String) {
        db = FirebaseFirestore.getInstance()
        db.collection("USER")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val myData = document.toObject(User::class.java)
                        if (myData.getPostID().toString() == textData) {
                            binding.edtName.setText(myData.getFullName())
                            binding.edtDiachi.setText(myData.getAddress())
                            binding.edtEmail.setText(myData.getEmail())
                            binding.edtSdt.setText(myData.getPhone())
                        }
                    }
                }
            }

    }


}