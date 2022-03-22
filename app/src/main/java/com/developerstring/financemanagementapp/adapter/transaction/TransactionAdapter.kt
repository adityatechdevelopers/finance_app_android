package com.developerstring.financemanagementapp.adapter.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.developerstring.financemanagementapp.R
import com.developerstring.financemanagementapp.firebase.transactions.TransactionsData

class TransactionAdapter(
    private val transactionList: ArrayList<TransactionsData>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.main_transaction_layout,parent,false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentItem = transactionList[position]

        holder.cal.text = currentItem.cal
        holder.amount.text = currentItem.amount.toString()
        holder.reason.text = currentItem.reason
        holder.date.text = currentItem.date
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cal: TextView = itemView.findViewById(R.id.cal_text_main_transaction)
        val amount: TextView = itemView.findViewById(R.id.amount_text_main_transaction)
        val reason: TextView = itemView.findViewById(R.id.reason_text_main_transaction)
        val date: TextView = itemView.findViewById(R.id.date_text_main_transaction)

    }

}