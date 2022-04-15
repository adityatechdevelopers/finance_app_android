package com.developerstring.financemanagementapp.adapter.financialreport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.developerstring.financemanagementapp.R
import com.developerstring.financemanagementapp.firebase.financialreport.FinancialReportData

class FinancialReportAdapter(
    private val financialReportList: ArrayList<FinancialReportData>
) : RecyclerView.Adapter<FinancialReportAdapter.FinancialReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinancialReportViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.financial_report_layout,parent,false)
        return FinancialReportViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FinancialReportViewHolder, position: Int) {
        val currentItem = financialReportList[position]

        holder.amount.text = currentItem.spent
        holder.month.text = currentItem.month
    }

    override fun getItemCount(): Int {
        return financialReportList.size
    }

    class FinancialReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val amount: TextView = itemView.findViewById(R.id.amount_financial_report_layout)
        val month: TextView = itemView.findViewById(R.id.month_financial_report_layout)

    }

}