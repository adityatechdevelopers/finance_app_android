package com.developerstring.financemanagementapp.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.developerstring.financemanagementapp.R
import com.developerstring.financemanagementapp.databinding.ActivityAccountProfileScreenBinding
import com.developerstring.financemanagementapp.databinding.ActivityProfileScreenBinding
import com.google.firebase.auth.FirebaseAuth

class AccountProfileScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityAccountProfileScreenBinding
    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityAccountProfileScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()
        // get Current User
        val currentUser = mAuth.currentUser

        // Set Value of Text
        binding.idNameAccountProfileScreen.text = currentUser?.displayName
        binding.idEmailAccountProfileScreen.text = currentUser?.email

        // Set user Profile Photo
        Glide.with(this).load(currentUser?.photoUrl).into(binding.profileImageAccountProfileScreen)

        // onclick close img
        binding.closeImgAccountProfileScreen.setOnClickListener {
            finish()
        }

    }
}