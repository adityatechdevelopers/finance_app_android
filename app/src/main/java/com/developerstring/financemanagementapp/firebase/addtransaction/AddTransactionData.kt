package com.developerstring.financemanagementapp.firebase.addtransaction

import androidx.annotation.Keep

@Keep data class AddTransactionData(
    var amount: Int? = null,
    var reason: String? = null,
    var date: String? = null,
    var time: String? = null,
    var id: String? = null,
    var cal: String? = null,
    var month: String? = null
)
