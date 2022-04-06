package com.developerstring.financemanagementapp.screen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.developerstring.financemanagementapp.databinding.ActivityVerifyEmailScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class VerifyEmailScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityVerifyEmailScreenBinding
    // firebase Auth
    private lateinit var mAuth: FirebaseAuth
    // firebase User
    private lateinit var user: FirebaseUser
    // email verification boolean, if email verified true else false
    private var emailVerified : Boolean = false
    // email address text
    private var emailText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityVerifyEmailScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // get firebase instance
        mAuth = FirebaseAuth.getInstance()
        // get the current user
        user = mAuth.currentUser!!

        // set text in emailText
        emailText = "We will send a verification link to \n" +
                "${user.email}"

        // set emailText in textView
        binding.emailAddressTextVerifyEmail.text = emailText

        // onclick Resend TextView
        binding.resendVerifyLinkVerifyEmailScreen.setOnClickListener {
            // send email verification again
            user.sendEmailVerification()
        }

        // onclick continue button
        binding.continueVerifyEmailScreen.setOnClickListener {
            checkEmailVerification()
            if (emailVerified){
                // make error text invisible
                binding.errorTextVerifyEmail.visibility = View.INVISIBLE
                Toast.makeText(this,"Thank you for verifying your email",Toast.LENGTH_SHORT).show()
            } else {
                // make error text visible
                binding.errorTextVerifyEmail.visibility = View.VISIBLE
            }
        }

    }
    private fun checkEmailVerification(){
        // check the user is verified or not
        emailVerified = user.isEmailVerified
    }
}