package com.developerstring.financemanagementapp.screen

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.developerstring.financemanagementapp.databinding.ActivitySetFinancialsScreenBinding
import com.developerstring.financemanagementapp.firebase.monthlyExpense.NewLimitData
import com.developerstring.financemanagementapp.firebase.monthlyExpense.NewTotalAmountData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SetFinancialsScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivitySetFinancialsScreenBinding

    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth

    // set Firebase Database
    private lateinit var database: DatabaseReference

    // set the userID
    private var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivitySetFinancialsScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()
        // get Current User ID
        userID = mAuth.currentUser?.uid

        // firebase Database Instance and reference(main path)
        database = FirebaseDatabase.getInstance().getReference("user data").child("amount")

        // onclick close img
        binding.closeImgMonthlyExpenseScreen.setOnClickListener {
            finish()
        }

        // get the old limit and display it
        getMonthlyLimit()

        // get the old total amount and display it
        getTotalAmount()

        // onclick change limit
        binding.saveSetFinancials.setOnClickListener {

            // get the new total amount and limit from the InputText
            val newLimit = binding.newMonthlyLimitTextInputEditText.text.toString()
            val newTotalAmount = binding.newTotalAmountTextInputEditText.text.toString()

            // store the new total amount in the data class
            val newTotalAmountData = NewTotalAmountData(newTotalAmount)
            // store the new limit in the data class
            val newLimitData = NewLimitData(newLimit)

            // set the new total amount in Firebase Database
            database.child(userID.toString()).child("limit").setValue(newLimitData)
                .addOnSuccessListener {

                    // set the monthly limit in Firebase Database
                    database.child(userID.toString()).child("amount").setValue(newTotalAmountData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "new financials updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Error! while updating financials",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }.addOnFailureListener {
                    Toast.makeText(this, "Error! while updating financials", Toast.LENGTH_SHORT)
                        .show()
                }

        }
    }

    private fun getMonthlyLimit() {

        database.child(userID.toString()).child("limit").get().addOnSuccessListener {
            // check the user has amount data if not then send to TotalAmountScreen to set amount
            if (it.exists()) {
                // get the amount form database
                val oldLimit = it.child("limit").value.toString()
                // set the limit in EditText
                binding.newMonthlyLimitTextInputEditText.setText(oldLimit)

            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error! While fetching monthly limit", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getTotalAmount() {

        database.child(userID.toString()).child("amount").get().addOnSuccessListener {
            // check the user has amount data if not then send to TotalAmountScreen to set amount
            if (it.exists()) {
                // get the amount form database
                val oldTotalAmount = it.child("amount").value.toString()
                // set the limit in EditText
                binding.newTotalAmountTextInputEditText.setText(oldTotalAmount)

            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error! While fetching amount", Toast.LENGTH_SHORT).show()
        }

    }
}