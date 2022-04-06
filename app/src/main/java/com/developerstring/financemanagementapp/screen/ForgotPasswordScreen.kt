package com.developerstring.financemanagementapp.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.developerstring.financemanagementapp.R
import com.developerstring.financemanagementapp.databinding.ActivityCreateAccountScreenBinding
import com.developerstring.financemanagementapp.databinding.ActivityForgotPasswordScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ForgotPasswordScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityForgotPasswordScreenBinding
    // firebase Auth
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityForgotPasswordScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // get firebase instance
        mAuth = FirebaseAuth.getInstance()

        // onclick close img
        binding.closeImgForgotPasswordScreen.setOnClickListener {
            finish()
        }

        binding.submitForgotPassScreen.setOnClickListener {
            // get the email
            val email = binding.emailForgotPassScreen.text.toString()
            // send user the password change link to their email
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task->
                if (task.isSuccessful){
                    Toast.makeText(this,"Password reset link sent successfully to your email",Toast.LENGTH_SHORT).show()
                    Handler().postDelayed({
                        finish()
                    },2000)
                } else {
                    val e = task.exception.toString()
                    Toast.makeText(this,e,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}