package com.developerstring.financemanagementapp.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.developerstring.financemanagementapp.adapter.transaction.TransactionAdapter
import com.developerstring.financemanagementapp.databinding.ActivityTransactionScreenBinding
import com.developerstring.financemanagementapp.firebase.transactions.TransactionsData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TransactionScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityTransactionScreenBinding
    // set Firebase Auth
    private lateinit var mAuth: FirebaseAuth
    // get Current User ID
    private var userID: String? = null
    // firebase database reference
    private lateinit var database: DatabaseReference
    // transaction ArrayList
    private lateinit var transactionArrayList: ArrayList<TransactionsData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityTransactionScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance()

        // get Current User ID
        userID = mAuth.currentUser?.uid.toString()

        // onclick close img
        binding.closeImgTransactionScreen.setOnClickListener {
            finish()
        }

        // set the layout and reverse items for recyclerView
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        // transaction RecyclerView
        binding.recyclerViewTransactionMain.layoutManager = layoutManager
        binding.recyclerViewTransactionMain.setHasFixedSize(true)
        binding.recyclerViewTransactionMain.itemAnimator = DefaultItemAnimator()

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
            .child("all")
            .child(userID.toString())

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val transaction = userSnapshot.getValue(TransactionsData::class.java)
                        transactionArrayList.add(transaction!!)
                    }
                    binding.recyclerViewTransactionMain.adapter = TransactionAdapter(transactionArrayList)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TransactionScreen,"Error while loading the Transactions", Toast.LENGTH_SHORT).show()
            }

        })

    }
}