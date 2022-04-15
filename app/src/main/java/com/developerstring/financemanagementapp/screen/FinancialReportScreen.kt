package com.developerstring.financemanagementapp.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.developerstring.financemanagementapp.R
import com.developerstring.financemanagementapp.adapter.financialreport.FinancialReportAdapter
import com.developerstring.financemanagementapp.adapter.transaction.TransactionAdapter
import com.developerstring.financemanagementapp.databinding.ActivityFinancialReportScreenBinding
import com.developerstring.financemanagementapp.databinding.ActivityTransactionScreenBinding
import com.developerstring.financemanagementapp.firebase.financialreport.FinancialReportData
import com.developerstring.financemanagementapp.firebase.transactions.TransactionsData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FinancialReportScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityFinancialReportScreenBinding
    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth
    // get Current User ID
    private var userID: String? = null
    // firebase database reference
    private lateinit var database: DatabaseReference
    // transaction ArrayList
    private lateinit var financialReportArrayList: ArrayList<FinancialReportData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityFinancialReportScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()

        // get Current User ID
        userID = mAuth.currentUser?.uid.toString()

        // onclick close img
        binding.closeImgFinancialReportScreen.setOnClickListener {
            finish()
        }

        // set the layout and reverse items for recyclerView
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        // transaction RecyclerView
        binding.recyclerViewFinancialReportMain.layoutManager = layoutManager
        binding.recyclerViewFinancialReportMain.setHasFixedSize(true)
        binding.recyclerViewFinancialReportMain.itemAnimator = DefaultItemAnimator()

        // set user array list
        financialReportArrayList = arrayListOf<FinancialReportData>()

        // get the data from firebase database
        getTransactionData()
    }

    private fun getTransactionData() {

        // firebase Database Instance and reference(main path)
        database = FirebaseDatabase.getInstance()
            .getReference("user data")
            .child("amount")
            .child(userID.toString())
            .child("spent")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val reportData = userSnapshot.getValue(FinancialReportData::class.java)
                        financialReportArrayList.add(reportData!!)
                    }
                    binding.recyclerViewFinancialReportMain.adapter = FinancialReportAdapter(financialReportArrayList)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FinancialReportScreen,"Error while loading the Transactions", Toast.LENGTH_SHORT).show()
            }

        })

    }
}