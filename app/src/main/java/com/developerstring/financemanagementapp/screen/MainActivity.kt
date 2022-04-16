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
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityMainBinding

    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth

    // monthly limit
    private var limit = 0

    // monthly expense
    private var spent = 0

    // monthly limit progressbar
    private var limitPB = 0

    // monthly expense progressbar
    private var spentPB = 0

    // firebase database reference
    private lateinit var database: DatabaseReference

    // get current user
    private lateinit var currentUser: FirebaseUser

    // get userID
    private var userID: String? = null

    // set the time and date
    private lateinit var currentMonth: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // database reference
        database = FirebaseDatabase.getInstance().getReference("user data")

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()

        // get the current user
        currentUser = mAuth.currentUser!!

        // get Current User ID
        userID = mAuth.currentUser?.uid

        // to get the current month
        currentMonth = SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(Date())

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

        // onclick profile image
        binding.editFinancialsImgMainActivity.setOnClickListener {
            // send user to Account Screen
            val editFinancialsIntent = Intent(this@MainActivity, SetFinancialsScreen::class.java)
            startActivity(editFinancialsIntent)
        }

        // onclick add transactions
        binding.addTransactionsMainActivity.setOnClickListener {
            // send user to Add Transactions Screen
            val addTransactionsIntent = Intent(this@MainActivity, AddTransactionsScreen::class.java)
            startActivity(addTransactionsIntent)
        }

        // onclick financial report
        binding.financeReportMainActivity.setOnClickListener {
            // send user to Add Transactions Screen
            val financialReportIntent = Intent(this@MainActivity, FinancialReportScreen::class.java)
            startActivity(financialReportIntent)
        }

        // onclick transaction
        binding.viewAllTransactionMainActivity.setOnClickListener {
            // send user to Add Transactions Screen
            val transactionsIntent = Intent(this@MainActivity, TransactionScreen::class.java)
            startActivity(transactionsIntent)
        }

    }

    private fun getTotalAmount() {

        database.child("amount").child(userID.toString()).child("amount").get()
            .addOnSuccessListener {
                // check the user has amount data if not then send to TotalAmountScreen to set amount
                if (it.exists()) {
                    // get the amount form database
                    val totalAmount = it.child("amount").value
                    // get the limit and display
                    getLimitData()
                    // get the spent and display
                    getSpentData()
                    // set the amount to the textView
                    binding.totalAmountMainActivity.text = totalAmount.toString()
                } else {
                    val totalAmountIntent = Intent(this, TotalAmountScreen::class.java)
                    startActivity(totalAmountIntent)
                    finish()
                }
            }.addOnFailureListener {
            Toast.makeText(this, "Error! While fetching amount", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getLimitData() {
        database.child("amount").child(userID.toString()).child("limit").get()
            .addOnSuccessListener {
                // check the user has amount data if not then send to TotalAmountScreen to set amount
                if (it.exists()) {
                    // get the amount form database
                    limit = it.child("limit").value.toString().toInt()
                    // set the limit in EditText
                    binding.expanseLimitMainActivity.text = limit.toString()

                } else {
                    Toast.makeText(this, "Please set a Monthly Limit", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
            Toast.makeText(this, "Error! While fetching limit", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshLayout() {
        binding.refreshMainActivity.setOnRefreshListener {
            getTotalAmount()
            getLimitData()
            getSpentData()
            setLSProgressBar()
            binding.refreshMainActivity.isRefreshing = false
        }
    }

    private fun getSpentData() {
        database.child("amount").child(userID.toString()).child("spent").child(currentMonth).get()
            .addOnSuccessListener {
                // check the user has amount data if not then send to TotalAmountScreen to set amount
                if (it.exists()) {
                    // get the amount form database
                    spent = it.child("spent").value.toString().toInt()
                    // set the spent in EditText
                    binding.spentDoneMainActivity.text = spent.toString()

                    // set the Limit and Spent progressbar
                    setLSProgressBar()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error! While fetching spent data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setLSProgressBar() {

        limitPB = limit*10
        spentPB = spent*10

        // set the limit in progressbar
        binding.amountProgressbarMainActivity.max = limitPB

        // set the expense in the progressbar
        ObjectAnimator.ofInt(binding.amountProgressbarMainActivity, "progress", spentPB)
            .setDuration(2000)
            .start()
    }
}