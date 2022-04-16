package com.developerstring.financemanagementapp.firebase.transactions

import androidx.annotation.Keep

@Keep data class TransactionsData(
    var amount: Int? = null,
    var reason: String? = null,
    var cal: String? = null,
    var date: String? = null,
)
