package com.developerstring.financemanagementapp.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.developerstring.financemanagementapp.R
import com.developerstring.financemanagementapp.databinding.ActivityAccountScreenBinding
import com.developerstring.financemanagementapp.databinding.ActivityMonthlyExpenseScreenBinding
import com.developerstring.financemanagementapp.firebase.monthlyExpense.NewLimitData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MonthlyExpenseScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityMonthlyExpenseScreenBinding
    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth
    // set Firebase Database
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityMonthlyExpenseScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()
        // get Current User
        val currentUser = mAuth.currentUser
        // get Current User ID
        val userID = mAuth.currentUser?.uid

        // firebase Database Instance and reference(main path)
        database = FirebaseDatabase.getInstance().getReference("user data").child("limit")

        // onclick close img
        binding.closeImgMonthlyExpenseScreen.setOnClickListener {
            finish()
        }

        // onclick change limit
        binding.changeLimitMonthlyExpense.setOnClickListener {

            // get the new limit from the InputText
            val newLimit = binding.newLimitMonthlyExpenseTextInputEditText.text.toString()

            // store the new limit in the data class
            val newLimitData = NewLimitData(newLimit)

            // set the data in Firebase Database
            database.child(userID.toString()).child("limit").setValue(newLimitData).addOnSuccessListener {
                Toast.makeText(this,"new value has set",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show()
            }

        }
    }
}