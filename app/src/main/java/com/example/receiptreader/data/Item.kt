package com.example.receiptreader.data

data class Item (val itemName : String, val qty:Double,
                 val qtyUnit:String?, val price:Double,
                 val date:String, val category:String?)