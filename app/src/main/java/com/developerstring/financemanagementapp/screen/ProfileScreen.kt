package com.developerstring.financemanagementapp.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.developerstring.financemanagementapp.R
import com.developerstring.financemanagementapp.databinding.ActivityProfileScreenBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityProfileScreenBinding
    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityProfileScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()
        // get Current User
        val currentUser = mAuth.currentUser

        // Set Value of Text
        binding.idNameProfileScreen.text = currentUser?.displayName
        binding.idEmailProfileScreen.text = currentUser?.email

        // Set user Profile Photo
        Glide.with(this).load(currentUser?.photoUrl).into(binding.profileImageProfileScreen)

        // on click next button
        binding.nextBtnProfileScreen.setOnClickListener {
            val intent = Intent(this@ProfileScreen, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}