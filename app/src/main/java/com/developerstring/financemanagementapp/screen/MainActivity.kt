package com.developerstring.financemanagementapp.screen

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.developerstring.financemanagementapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityMainBinding
    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth
    // monthly limit
    private var limit = 5600
    // monthly expense
    private var expense = 3500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // set the limit in progressbar
        binding.amountProgressbarMainActivity.max = limit

        // set the expense in the progressbar
        ObjectAnimator.ofInt(binding.amountProgressbarMainActivity,"progress",expense)
            .setDuration(2000)
            .start()

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()

        // get the current user
        val currentUser = mAuth.currentUser

        // set profile image
        Glide.with(this).load(currentUser?.photoUrl).into(binding.profileImageMainActivity)

        // onclick profile image
        binding.profileImageMainActivity.setOnClickListener {
            // send user to Account Screen
            val profileScreenIntent = Intent(this@MainActivity, AccountScreen::class.java)
            startActivity(profileScreenIntent)
        }

        // onclick add transactions
        binding.addTransactionsMainActivity.setOnClickListener {
            // send user to Add Transactions Screen
            val addTransactionsIntent = Intent(this@MainActivity, AddTransactionsScreen::class.java)
            startActivity(addTransactionsIntent)
        }

        // onclick transaction
        binding.viewAllTransactionMainActivity.setOnClickListener {
            // send user to Add Transactions Screen
            val transactionsIntent = Intent(this@MainActivity, TransactionScreen::class.java)
            startActivity(transactionsIntent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}