package com.developerstring.financemanagementapp.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.developerstring.financemanagementapp.R
import com.developerstring.financemanagementapp.databinding.ActivityAccountScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
                logOut()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->

            }
            builder.show()
        }
    }
    private fun logOut(){
        // get the google account
        val googleSignInClient: GoogleSignInClient

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // sign out of your account
        mAuth.signOut()
        googleSignInClient.signOut().addOnSuccessListener {
            val signInScreenIntent = Intent(this@AccountScreen, SignInScreen::class.java)
            startActivity(signInScreenIntent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this@AccountScreen,"Error! while logging out",Toast.LENGTH_SHORT).show()
        }
    }
}