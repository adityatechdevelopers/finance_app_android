package com.developerstring.financemanagementapp.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.developerstring.financemanagementapp.R
import com.developerstring.financemanagementapp.databinding.ActivityNotVerifiedEmailScreenBinding
import com.developerstring.financemanagementapp.databinding.ActivityVerifyEmailScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class NotVerifiedEmailScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityNotVerifiedEmailScreenBinding
    // firebase Auth
    private lateinit var mAuth: FirebaseAuth
    // firebase User
    private lateinit var user: FirebaseUser
    // email address text
    private var emailText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityNotVerifiedEmailScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // get firebase instance
        mAuth = FirebaseAuth.getInstance()
        // get the current user
        user = mAuth.currentUser!!

        // set text in emailText
        emailText = "We have sent verification link to \n" +
                "${user.email}, to confirm it's your email address"

        // onclick continue button
        binding.continueNotVerifyEmailScreen.setOnClickListener {
            user.sendEmailVerification().addOnSuccessListener {
                val verifyEmailScreenIntent = Intent(this,VerifyEmailScreen::class.java)
                startActivity(verifyEmailScreenIntent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this,"Error! Please try again after some time",Toast.LENGTH_SHORT).show()
            }
        }
    }
}