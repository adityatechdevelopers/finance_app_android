package com.developerstring.financemanagementapp.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.developerstring.financemanagementapp.databinding.ActivityAccountScreenBinding
import com.google.firebase.auth.FirebaseAuth

class AccountScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityAccountScreenBinding
    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityAccountScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()
        // get Current User
        val currentUser = mAuth.currentUser

        // Set user Profile Photo
        Glide.with(this).load(currentUser?.photoUrl).into(binding.profileImageAccountScreen)

        // onclick close img
        binding.closeImgAccountScreen.setOnClickListener {
            // close the activity
            finish()
        }

        // onclick monthly expense
        binding.setFinancialsAccountScreen.setOnClickListener {
            val monthlyExpenseIntent = Intent(this@AccountScreen,SetFinancialsScreen::class.java)
            startActivity(monthlyExpenseIntent)
        }

        // onclick profile
        binding.profileAccountAccountScreen.setOnClickListener {
            val accountProfileScreenIntent = Intent(this@AccountScreen,AccountProfileScreen::class.java)
            startActivity(accountProfileScreenIntent)
        }

        // onclick logout
        binding.logoutAccountScreen.setOnClickListener {

            // show a alert dialog
            val builder = AlertDialog.Builder(this@AccountScreen)
            builder.setTitle("Confirm Logout")
            builder.setMessage("Are you sure you want to logout")
            builder.setPositiveButton("Logout") { dialog, which ->
                mAuth.signOut()
                val signInScreenIntent = Intent(this@AccountScreen, SignInScreen::class.java)
                startActivity(signInScreenIntent)
                finish()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->

            }
            builder.show()
        }
    }
}