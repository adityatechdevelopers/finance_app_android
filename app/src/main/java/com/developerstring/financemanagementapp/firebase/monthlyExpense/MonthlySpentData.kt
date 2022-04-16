package com.developerstring.financemanagementapp.firebase.monthlyExpense

import androidx.annotation.Keep

@Keep data class MonthlySpentData(
    var spent: String? = null,
    var month: String? = null
) {

}
