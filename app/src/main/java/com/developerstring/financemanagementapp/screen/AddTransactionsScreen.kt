package com.developerstring.financemanagementapp.screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.developerstring.financemanagementapp.databinding.ActivityAddTransactionsScreenBinding
import com.developerstring.financemanagementapp.firebase.addtransaction.AddTransactionData
import com.developerstring.financemanagementapp.firebase.totalamount.TotalAmountData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionsScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityAddTransactionsScreenBinding

    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth

    // get Current User ID
    private var userID: String? = null

    // set Firebase Database
    private lateinit var database: DatabaseReference

    // set data class
    private val addTransactionData = AddTransactionData()

    // set chip value on withdraw
    private val chipSpent = "-"

    // set chip value on add fund
    private val chipAddFund = "+"

    // set chip value
    private var chipValue: String? = null

    // create a variable for amount
    private var amount: Int? = null

    // create a variable for spent on
    private var reason: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityAddTransactionsScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()

        // get Current User ID
        userID = mAuth.currentUser?.uid.toString()

        // firebase Database Instance and reference(main path)
        database = FirebaseDatabase.getInstance().getReference("user data")

        // onclick close img
        binding.closeImgAddTransactionsScreen.setOnClickListener {
            finish()
        }

        // if amount text is null
        binding.amountAddTransactionTextInputEditText.doOnTextChanged { text, start, before, count ->
            if (text?.length == null) {
                binding.amountAddTransactionTextInputLayout.error = "Please Enter Amount"
            } else {
                binding.amountAddTransactionTextInputLayout.error = null
            }
        }

        // chip value on withdraw selected
        binding.withdrawAddTransactionChip.setOnClickListener {
            chipValue = chipSpent
        }

        // chip value on add fund selected
        binding.addFundAddTransactionChip.setOnClickListener {
            chipValue = chipAddFund
        }

        // on done button clicked
        binding.doneAddTransactionButton.setOnClickListener {

            // checking the user has selected the chip or not
            if (chipValue != null) {

                // checking is the amount is null or not
                if (!TextUtils.isEmpty(binding.amountAddTransactionTextInputEditText.text.toString())) {

                    // getting the amount
                    amount = binding.amountAddTransactionTextInputEditText.text.toString().toInt()
                    reason = "not specified"

                    // checking user has entered text in spent on or not
                    if (!TextUtils.isEmpty(binding.spentOnAddTransactionTextInputEditText.text.toString())) {
                        // get the spent on text only when it is not null
                        reason = binding.spentOnAddTransactionTextInputEditText.text.toString()
                    }

                    addTransactionToDatabase()

                } else {
                    // show error for not entering amount
                    binding.amountAddTransactionTextInputLayout.error = "Please Enter Amount"
                    Toast.makeText(this, "Please enter Amount", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "please fill the details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTransactionToDatabase() {

        // show the progressbar
        binding.progressBarAddTransactions.visibility = View.VISIBLE

        // disable the button
        binding.doneAddTransactionButton.isEnabled = false

        // disable error of amount text if its there
        binding.amountAddTransactionTextInputLayout.error = null

        // to get the current time and date
        val currentTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentMonth = SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(Date())
        val databaseKey = database.push().key.toString()

        // save the data to the member class
        addTransactionData.amount = amount
        addTransactionData.reason = reason
        addTransactionData.date = currentDate
        addTransactionData.time = currentTime
        addTransactionData.id = databaseKey
        addTransactionData.cal = chipValue

        // upload the data to firebase database with month
        database.child("transaction").child("term").child(userID.toString()).child(currentMonth)
            .child(databaseKey).setValue(addTransactionData).addOnSuccessListener {
                // update the value in total amount
                updateTotalAmount()
            }.addOnFailureListener {
                // on failure
                binding.progressBarAddTransactions.visibility = View.INVISIBLE
                Toast.makeText(this, "Unable to upload data", Toast.LENGTH_SHORT).show()
                binding.doneAddTransactionButton.isEnabled = true
            }

        // upload the data to firebase database
        database.child("transaction").child(userID.toString()).child(databaseKey)
            .setValue(addTransactionData).addOnFailureListener {
                // on failure
                binding.progressBarAddTransactions.visibility = View.INVISIBLE
                Toast.makeText(this, "Unable to upload data", Toast.LENGTH_SHORT).show()
                binding.doneAddTransactionButton.isEnabled = true
            }

    }

    private fun updateTotalAmount() {

        database.child("amount").child(userID.toString()).child("amount").get()
            .addOnSuccessListener {
                // check the user has amount data if not then send to TotalAmountScreen to set amount
                if (it.exists()) {
                    // get the amount form database
                    val previousAmount = it.child("amount").value.toString().toInt()
                    // value of amount remained in account
                    var remainedAmount = previousAmount
                    // check we have to add or minus amount
                    if (chipValue == chipAddFund) {
                        remainedAmount = (previousAmount + amount!!)
                    } else if (chipValue == chipSpent) {
                        remainedAmount = (previousAmount - amount!!)
                    }

                    // store the new limit in the data class
                    val totalAmount = TotalAmountData(remainedAmount.toString())

                    // set the data in Firebase Database
                    database.child("amount").child(userID.toString()).child("amount")
                        .setValue(totalAmount).addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Transaction has been added successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            // a delay after adding the data
                            Handler().postDelayed({
                                // invisible the progressbar
                                binding.progressBarAddTransactions.visibility = View.INVISIBLE
                                // send user to main activity
                                this.finish()
                            }, 2000)

                        }.addOnFailureListener {
                            Toast.makeText(this, "failed to add transaction", Toast.LENGTH_SHORT)
                                .show()
                        }

                } else {
                    val totalAmountIntent = Intent(this, TotalAmountScreen::class.java)
                    startActivity(totalAmountIntent)
                    finish()
                }
            }.addOnFailureListener {
                // on failure
                Toast.makeText(this, "Error! While getting total amount", Toast.LENGTH_SHORT).show()
            }

    }

}