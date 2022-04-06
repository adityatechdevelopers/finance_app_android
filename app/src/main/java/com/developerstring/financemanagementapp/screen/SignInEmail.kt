package com.developerstring.financemanagementapp.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.developerstring.financemanagementapp.databinding.ActivitySignInEmailBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignInEmail : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivitySignInEmailBinding

    // firebase Auth
    private lateinit var mAuth: FirebaseAuth

    // firebase User
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivitySignInEmailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // get firebase instance
        mAuth = FirebaseAuth.getInstance()

        // get the current user
        user = mAuth.currentUser!!

        // onclick forgot password
        binding.forgotPasswordSignInEmail.setOnClickListener {
            val forgotPasswordIntent = Intent(this@SignInEmail, ForgotPasswordScreen::class.java)
            startActivity(forgotPasswordIntent)
        }

        // onclick create account
        binding.createAccountSignInEmail.setOnClickListener {
            val createAccountIntent = Intent(this@SignInEmail, CreateAccountScreen::class.java)
            startActivity(createAccountIntent)
            finish()
        }

        // show password checkbox
        binding.showPasswordCheckboxSignInEmail.setOnClickListener {
            // show password on checkbox checked
            if (binding.showPasswordCheckboxSignInEmail.isChecked) {
                binding.passwordSignInEmail.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }

            // hide password if checkbox not checked
            if (!binding.showPasswordCheckboxSignInEmail.isChecked) {
                binding.passwordSignInEmail.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }

        // onclick sign in
        binding.signInBtnEmail.setOnClickListener {
            // get the email and password
            val email = binding.emailSignInEmail.text.toString()
            val password = binding.passwordSignInEmail.text.toString()

            // sign in  with email and password
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        // for checking is sign in task completed or not
                        if (task.isSuccessful) {
                            // send user to main activity
                            val mainActivityIntent = Intent(this, MainActivity::class.java)
                            startActivity(mainActivityIntent)
                            finish()
                        } else {
                            // is got any error while sign in then get that error and show to the user
                            val e = task.exception.toString()
                            Toast.makeText(this, e, Toast.LENGTH_SHORT).show()
                        }
                    }
                )

        }
    }
}