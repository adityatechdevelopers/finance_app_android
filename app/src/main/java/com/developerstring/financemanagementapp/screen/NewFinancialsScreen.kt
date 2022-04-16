package com.developerstring.financemanagementapp.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.developerstring.financemanagementapp.R
import com.developerstring.financemanagementapp.databinding.ActivityNewFinancialsScreenBinding
import com.developerstring.financemanagementapp.databinding.ActivitySetFinancialsScreenBinding
import com.developerstring.financemanagementapp.firebase.monthlyExpense.NewLimitData
import com.developerstring.financemanagementapp.firebase.monthlyExpense.NewTotalAmountData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NewFinancialsScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityNewFinancialsScreenBinding

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
        binding = ActivityNewFinancialsScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()
        // get Current User ID
        userID = mAuth.currentUser?.uid

        // firebase Database Instance and reference(main path)
        database = FirebaseDatabase.getInstance().getReference("user data").child("amount")

        // if amount text is null
        binding.newTotalAmountTextInputEditText.doOnTextChanged { text, start, before, count ->
            if (text?.length == null) {
                data = false
                binding.newTotalAmountTextInputLayout.error = "Please enter total amount you have"
            } else {
                binding.newTotalAmountTextInputLayout.error = null
                binding.amountTextNewFinancials.setTextColor(resources.getColor(R.color.black))
            }
        }

        // if monthly amount text is null
        binding.newMonthlyLimitTextInputEditText.doOnTextChanged { text, start, before, count ->
            if (text?.length == null) {
                data = false
                binding.newMonthlyLimitTextInputLayout.error = "Please enter monthly limit"
            } else {
                binding.newMonthlyLimitTextInputLayout.error = null
                binding.limitTextNewFinancials.setTextColor(resources.getColor(R.color.black))
            }
        }

        // onclick change limit
        binding.saveNewFinancials.setOnClickListener {

            // get the new total amount and limit from the InputText
            val newLimit = binding.newMonthlyLimitTextInputEditText.text.toString()
            val newTotalAmount = binding.newTotalAmountTextInputEditText.text.toString()

            checkEditTextData()

            // check all fields are filled
            if (!TextUtils.isEmpty(binding.newTotalAmountTextInputEditText.text) &&
                !TextUtils.isEmpty(binding.newMonthlyLimitTextInputEditText.text)
            ) {
                // store the new total amount in the data class
                val newTotalAmountData = NewTotalAmountData(newTotalAmount)
                // store the new limit in the data class
                val newLimitData = NewLimitData(newLimit)

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

                                // send user to MainActivity
                                val intent = Intent(this@NewFinancialsScreen, MainActivity::class.java)
                                startActivity(intent)
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

            else {
                Toast.makeText(this@NewFinancialsScreen,"please fill all the details",Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun checkEditTextData() {
        // check amount
        if (!TextUtils.isEmpty(binding.newTotalAmountTextInputEditText.text)) {
            // check limit
            if (!TextUtils.isEmpty(binding.newMonthlyLimitTextInputEditText.text)) {
                data = true
            } else {
                binding.newMonthlyLimitTextInputLayout.error
                binding.limitTextNewFinancials.setTextColor(resources.getColor(R.color.red))
            }
        } else {
            binding.newTotalAmountTextInputLayout.error
            binding.amountTextNewFinancials.setTextColor(resources.getColor(R.color.red))
            // check limit
            if (TextUtils.isEmpty(binding.newMonthlyLimitTextInputEditText.text)) {
                binding.newMonthlyLimitTextInputLayout.error
                binding.limitTextNewFinancials.setTextColor(resources.getColor(R.color.red))
            }
        }
    }
}