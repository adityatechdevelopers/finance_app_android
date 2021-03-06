package com.developerstring.financemanagementapp.screen

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.developerstring.financemanagementapp.adapter.transaction.TransactionAdapter
import com.developerstring.financemanagementapp.databinding.ActivityMonthFinancialReportScreenBinding
import com.developerstring.financemanagementapp.firebase.transactions.TransactionsData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MonthFinancialReportScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityMonthFinancialReportScreenBinding
    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth
    // get Current User ID
    private var userID: String? = null
    // firebase database reference
    private lateinit var database: DatabaseReference
    // transaction ArrayList
    private lateinit var transactionArrayList: ArrayList<TransactionsData>
    // store data from intent
    private lateinit var month : String
    private lateinit var spent : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityMonthFinancialReportScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // get the data from intent which include month and spent amount
        month = intent.getStringExtra("month").toString()
        spent = intent.getStringExtra("spent").toString()

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()

        // get Current User ID
        userID = mAuth.currentUser?.uid.toString()

        // onclick close img
        binding.closeImgMonthFinancialReportScreen.setOnClickListener {
            finish()
        }

        // setting the month and spent data to textview
        binding.spentTextMonthFinancialReportScreen.text = month
        binding.monthTextMonthFinancialReportScreen.text = spent

        // set the layout and reverse items for recyclerView
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        // transaction RecyclerView
        binding.recyclerViewMonthFinancialReportScreen.layoutManager = layoutManager
        binding.recyclerViewMonthFinancialReportScreen.setHasFixedSize(true)
        binding.recyclerViewMonthFinancialReportScreen.itemAnimator = DefaultItemAnimator()

        // set user array list
        transactionArrayList = arrayListOf<TransactionsData>()

        // get the data from firebase database
        getTransactionData()
    }

    private fun getTransactionData() {

        // firebase Database Instance and reference(main path)
        database = FirebaseDatabase.getInstance()
            .getReference("user data")
            .child("transaction")
            .child("term")
            .child(userID.toString())
            .child(month)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val transaction = userSnapshot.getValue(TransactionsData::class.java)
                        transactionArrayList.add(transaction!!)
                    }
                    binding.recyclerViewMonthFinancialReportScreen.adapter = TransactionAdapter(transactionArrayList)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MonthFinancialReportScreen,"Error while loading the Transactions", Toast.LENGTH_SHORT).show()
            }

        })

    }
}