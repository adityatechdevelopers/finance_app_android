package com.developerstring.financemanagementapp.firebase.financialreport

import androidx.annotation.Keep

@Keep data class FinancialReportData(
    var spent: String? = null,
    var month: String? = null
)
