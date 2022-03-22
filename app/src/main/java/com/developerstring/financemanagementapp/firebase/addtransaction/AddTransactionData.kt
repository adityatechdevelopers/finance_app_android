package com.developerstring.financemanagementapp.firebase.addtransaction

data class AddTransactionData(
    var amount: Int? = null,
    var reason: String? = null,
    var date: String? = null,
    var time: String? = null,
    var id: String? = null,
    var cal: String? = null
)
