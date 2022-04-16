package com.developerstring.financemanagementapp.screen

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.developerstring.financemanagementapp.R
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

    // for checking fields are not null
    private var data = false

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

        // if amount text is null
        binding.totalAmountTextInputEditText.doOnTextChanged { text, start, before, count ->
            if (text?.length == null) {
                data = false
                binding.totalAmountTextInputLayout.error = "Please enter total amount you have"
            } else {
                binding.totalAmountTextInputLayout.error = null
                binding.amountTextSetFinancials.visibility = View.INVISIBLE
            }
        }

        // if monthly amount text is null
        binding.monthlyLimitTextInputEditText.doOnTextChanged { text, start, before, count ->
            if (text?.length == null) {
                data = false
                binding.monthlyLimitTextInputLayout.error = "Please enter monthly limit"
            } else {
                binding.monthlyLimitTextInputLayout.error = null
                binding.limitTextSetFinancials.visibility = View.INVISIBLE
            }
        }

        // get the old limit and display it
        getMonthlyLimit()

        // get the old total amount and display it
        getTotalAmount()

        // onclick change limit
        binding.saveSetFinancials.setOnClickListener {

            // get the new total amount and limit from the InputText
            val newLimit = binding.monthlyLimitTextInputEditText.text.toString()
            val newTotalAmount = binding.totalAmountTextInputEditText.text.toString()

            // store the new total amount in the data class
            val newTotalAmountData = NewTotalAmountData(newTotalAmount)
            // store the new limit in the data class
            val newLimitData = NewLimitData(newLimit)

            checkEditTextData()

            if (!TextUtils.isEmpty(binding.monthlyLimitTextInputEditText.text) &&
                !TextUtils.isEmpty(binding.totalAmountTextInputEditText.text)
            ) {
                // set the new total amount in Firebase Database
                database.child(userID.toString()).child("limit").setValue(newLimitData)
                    .addOnSuccessListener {

                        // set the monthly limit in Firebase Database
                        database.child(userID.toString()).child("amount")
                            .setValue(newTotalAmountData)
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

            } else {
                Toast.makeText(
                    this@SetFinancialsScreen,
                    "Please enter all details",
                    Toast.LENGTH_SHORT
                ).show()
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
                binding.monthlyLimitTextInputEditText.setText(oldLimit)

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
                binding.totalAmountTextInputEditText.setText(oldTotalAmount)

            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error! While fetching amount", Toast.LENGTH_SHORT).show()
        }

    }
    private fun checkEditTextData() {
        // check amount
        if (!TextUtils.isEmpty(binding.totalAmountTextInputEditText.text)) {
            // check limit
            if (!TextUtils.isEmpty(binding.monthlyLimitTextInputEditText.text)) {
                data = true
            } else {
                binding.monthlyLimitTextInputLayout.error
                binding.limitTextSetFinancials.visibility = View.VISIBLE
            }
        } else {
            binding.totalAmountTextInputLayout.error
            binding.amountTextSetFinancials.visibility = View.VISIBLE
            // check limit
            if (TextUtils.isEmpty(binding.monthlyLimitTextInputEditText.text)) {
                binding.monthlyLimitTextInputLayout.error
                binding.limitTextSetFinancials.visibility = View.VISIBLE
            }
        }
    }
}