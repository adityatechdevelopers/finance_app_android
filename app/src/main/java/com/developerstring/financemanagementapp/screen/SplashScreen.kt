package com.developerstring.financemanagementapp.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.developerstring.financemanagementapp.R
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()
        // get current user
        val user = mAuth.currentUser

        Handler().postDelayed({

            if (user != null){
                val mainActivityIntent = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(mainActivityIntent)
                finish()
            } else {
                val signInScreenIntent = Intent(this@SplashScreen, SignInScreen::class.java)
                startActivity(signInScreenIntent)
                finish()
            }
        },3000)

    }
}