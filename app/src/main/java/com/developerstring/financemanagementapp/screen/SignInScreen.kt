package com.developerstring.financemanagementapp.screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.developerstring.financemanagementapp.R
import com.developerstring.financemanagementapp.databinding.ActivitySignInScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.system.exitProcess

class SignInScreen : AppCompatActivity() {

    // View Binding
    private lateinit var binding: ActivitySignInScreenBinding
    // creating a constant object
    companion object{
        private const val RC_SIGN_IN = 100
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivitySignInScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // onclick google sign in button
        binding.signInBtnSignInScreen.setOnClickListener {
            signIn()
        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInScreen", "successful")
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInScreen", "Google sign in failed", e)
                }
            } else{
                Log.w("SignInScreen", exception.toString() )
            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInScreen", "signInWithCredential:success")
                    // Sign In Successful and send user to next Activity
                    val intent = Intent(this@SignInScreen, ProfileScreen::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignInScreen ", "signInWithCredential:failure", task.exception)
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // close the app
        moveTaskToBack(true)
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(1)
    }
}