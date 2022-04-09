package com.developerstring.financemanagementapp.screen

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.developerstring.financemanagementapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityMainBinding

    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth

    // monthly limit
    private var limit = 5600

    // monthly expense
    private var expense = 3500

    // firebase database reference
    private lateinit var database: DatabaseReference

    // get current user
    private lateinit var currentUser: FirebaseUser

    // get userID
    private var userID : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // set the limit in progressbar
        binding.amountProgressbarMainActivity.max = limit

        // database reference
        database = FirebaseDatabase.getInstance().getReference("user data")

        // set the expense in the progressbar
        ObjectAnimator.ofInt(binding.amountProgressbarMainActivity, "progress", expense)
            .setDuration(2000)
            .start()

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()

        // get the current user
        currentUser = mAuth.currentUser!!

        // get Current User ID
        userID = mAuth.currentUser?.uid

        // set profile image
        Glide.with(this).load(currentUser.photoUrl).into(binding.profileImageMainActivity)

        // get the amount and display
        getTotalAmount()

        // on refreshing the activity
        refreshLayout()

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

    private fun getTotalAmount() {

        database.child("amount").child(userID.toString()).child("amount").get().addOnSuccessListener {
            // check the user has amount data if not then send to TotalAmountScreen to set amount
            if (it.exists()) {
                // get the amount form database
                val totalAmount = it.child("amount").value
                // set the amount to the textView
                binding.totalAmountMainActivity.text = totalAmount.toString()
            } else {
                val totalAmountIntent = Intent(this, TotalAmountScreen::class.java)
                startActivity(totalAmountIntent)
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error! While fetching data", Toast.LENGTH_SHORT).show()
        }

    }

    private fun refreshLayout() {
        binding.refreshMainActivity.setOnRefreshListener {
            getTotalAmount()
            binding.refreshMainActivity.isRefreshing = false
        }
    }
}