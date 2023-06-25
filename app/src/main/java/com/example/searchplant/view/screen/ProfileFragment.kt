package com.example.searchplant.view.screen

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.searchplant.R
import com.example.searchplant.databinding.FragmentProfileBinding
import com.example.searchplant.model.Species
import com.example.searchplant.model.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

    class ProfileFragment : Fragment(),NavigationView.OnNavigationItemSelectedListener{
        private val REQUEST_IMAGE_CAPTURE = 100
        lateinit var binding: FragmentProfileBinding
        private lateinit var db : FirebaseFirestore
        private lateinit var navigationView: NavigationView
        private lateinit var drawerLayout: DrawerLayout
        private lateinit var toggle: ActionBarDrawerToggle
        private var selectTab = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        }
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,

        ): View? {
            binding = FragmentProfileBinding.inflate(inflater, container,false)

            drawerLayout = binding.drawView
            navigationView = binding.navView

            toggle = ActionBarDrawerToggle(requireActivity(),drawerLayout,R.string.open,R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            navigationView.setNavigationItemSelectedListener(this)


            binding.buttonNav.background = null
            binding.buttonNav.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.END)
            }

            binding.bottomNavigationView1.background = null
            binding.btnBack2.background = null
            binding.btnAdd.setOnClickListener {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
                }catch (e: ActivityNotFoundException)
                {
                    Toast.makeText(requireActivity(),"Error: "+e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }

            val articlesLayout = binding.layoutArt
            val speciesLayout = binding.layoutSpec
            val likesLayout = binding.layoutLikes

            val articlesImage = binding.imageArt
            val speciesImage = binding.imageSpecies
            val likesImage = binding.imageLike

            val articlesText = binding.textArticles
            val speciesText = binding.textSpecies
            val likeText = binding.textLike


            binding.btnBack2.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            }
            articlesLayout.setOnClickListener {
                if(selectTab != 1)
                {

                    childFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView,ArticlesProfileFragment())
                        .commit()

                    speciesText.visibility = View.GONE
                    likeText.visibility = View.GONE

                    speciesImage.setImageResource(R.drawable.baseline_grass_24)
                    likesImage.setImageResource(R.drawable.baseline_favorite_24)

                    speciesLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                    likesLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                    articlesText.visibility = View.VISIBLE
                    articlesImage.setImageResource(R.drawable.baseline_article_24_select)
                    articlesLayout.setBackgroundResource(R.drawable.round_background)

                    val scaleAnimation = ScaleAnimation(0.8f,1.0f,1f,1f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f)
                    scaleAnimation.duration = 200
                    scaleAnimation.fillAfter = true
                    articlesLayout.startAnimation(scaleAnimation)
                    selectTab = 1
                }
            }
            speciesLayout.setOnClickListener {
                if(selectTab != 2)
                {
                    childFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView,SpeciesProfileFragment())
                        .commit()

                    articlesText.visibility = View.GONE
                    likeText.visibility = View.GONE

                    articlesImage.setImageResource(R.drawable.baseline_article_24)
                    likesImage.setImageResource(R.drawable.baseline_favorite_24)

                    articlesLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                    likesLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                    speciesText.visibility = View.VISIBLE
                    speciesImage.setImageResource(R.drawable.baseline_grass_24_select)
                    speciesLayout.setBackgroundResource(R.drawable.round_background)

                    val scaleAnimation = ScaleAnimation(0.8f,1.0f,1f,1f,Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f)
                    scaleAnimation.duration = 200
                    scaleAnimation.fillAfter = true
                    speciesLayout.startAnimation(scaleAnimation)

                    selectTab = 2
                }
            }
            likesLayout.setOnClickListener {
                if(selectTab != 3)
                {
                    childFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView,LikesProfileFragment())
                        .commit()

                    articlesText.visibility = View.GONE
                    speciesText.visibility = View.GONE

                    articlesImage.setImageResource(R.drawable.baseline_article_24)
                    speciesImage.setImageResource(R.drawable.baseline_grass_24)

                    articlesLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                    speciesLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                    likeText.visibility = View.VISIBLE
                    likesImage.setImageResource(R.drawable.baseline_favorite_24_select)
                    likesLayout.setBackgroundResource(R.drawable.round_background)

                    val scaleAnimation = ScaleAnimation(0.8f,1.0f,1f,1f,Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f)
                    scaleAnimation.duration = 200
                    scaleAnimation.fillAfter = true
                    likesLayout.startAnimation(scaleAnimation)
                    selectTab = 3
                }
            }
            binding.bottomNavigationView1.setOnNavigationItemReselectedListener{
                when(it.itemId) {
                    R.id.home -> {
                        findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
                    }
                    R.id.profile -> {
                        findNavController().navigate(R.id.action_profileFragment_self)
                    }
                }
            }

            val sharedPref = requireActivity().getSharedPreferences("sendPostID", Context.MODE_PRIVATE)
            val postID = sharedPref.getString("postID","")
            if (postID != null) {
                getDataUser(postID)
            }
            return binding.root
        }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == Activity.RESULT_OK)){

            val imageBitmap = data?.extras?.get("data") as Bitmap
            sendData(imageBitmap)
            findNavController().navigate(R.id.action_profileFragment_to_addNewPlantFragment)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun sendData(image: Bitmap)
    {
        val bundle = Bundle()
        bundle.putParcelable("imagePlant", Species(null,null,null,null,null,null,null,null,image))
        parentFragmentManager.setFragmentResult("imagePlant1",bundle)
    }
    private fun getDataUser(textData: String){
        db = FirebaseFirestore.getInstance()
        db.collection("USER")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result) {
                        val myData = document.toObject(User::class.java)
                        if(myData.getPostID().toString() == textData)
                        {
                            binding.textfullName.text = myData.getFullName()
                            binding.textAddress.text = myData.getAddress()
                            val storageRef = FirebaseStorage.getInstance().reference.child("avatar/${myData.getPostID()}.jpg")
                            val localFile = File.createTempFile("tempPlant","jpg")
                            storageRef.getFile(localFile).addOnCompleteListener {
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                binding.imageAvatar.setImageBitmap(bitmap)
                            }
                        }
                    }
                }
            }
    }

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when(item.itemId) {
                R.id.nav_profile -> {

                    findNavController().navigate(R.id.action_profileFragment_to_informationFragment)

                    Toast.makeText(activity, "Click profile", Toast.LENGTH_SHORT).show()
                    Log.d(ContentValues.TAG, "THuannnnnnnnnnnnnnnnnnnn::profile")
                    return false
                }
                R.id.nav_setting -> {
                    Toast.makeText(activity, "Click setting", Toast.LENGTH_SHORT).show()
                    Log.d(ContentValues.TAG, "THuannnnnnnnnnnnnnnnnnnn::setting")

                    return false
                }
                R.id.nav_logout -> {
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                    val sharePref = requireActivity().getSharedPreferences("remember", Context.MODE_PRIVATE)
                    val editor = sharePref.edit()
                    editor.putString("remember","false")
                    editor.apply()
                    Toast.makeText(activity, "Log out successfull", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            return true
        }

}