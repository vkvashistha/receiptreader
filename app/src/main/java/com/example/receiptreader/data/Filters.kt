package com.example.receiptreader.data

data class Filters(
    var startDate : String? = null,
    var endDate : String? = null,
    var category : String? = null,
    var merchant : String? = null
)