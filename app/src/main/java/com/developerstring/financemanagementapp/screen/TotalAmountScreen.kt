package com.developerstring.financemanagementapp.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import com.developerstring.financemanagementapp.databinding.ActivityTotalAmountScreenBinding
import com.developerstring.financemanagementapp.firebase.totalamount.TotalAmountData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TotalAmountScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityTotalAmountScreenBinding
    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth
    // set Firebase Database
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityTotalAmountScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()
        // get Current User ID
        val userID = mAuth.currentUser?.uid

        // firebase Database Instance and reference(main path)
        database = FirebaseDatabase.getInstance().getReference("user data")

        binding.doneTotalAmountBtn.setOnClickListener {
            // get the new limit from the InputText
            val totalAmountText = binding.totalAmountExpenseTextInputEditText.text.toString()

            // store the new limit in the data class
            val totalAmount = TotalAmountData(totalAmountText)

            // set the data in Firebase Database
            database.child("amount").child(userID.toString()).child("amount").setValue(totalAmount).addOnSuccessListener {

                Toast.makeText(this,"Amount set successfully", Toast.LENGTH_SHORT).show()

                Handler().postDelayed({
                    // send user to MainActivity
                    val mainActivityIntent = Intent(this,MainActivity::class.java)
                    startActivity(mainActivityIntent)
                    finish()
                },3000)


            }.addOnFailureListener {
                Toast.makeText(this,"failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}