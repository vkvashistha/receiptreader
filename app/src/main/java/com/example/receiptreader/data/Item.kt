package com.example.receiptreader.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item (
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "item_name") val itemName : String, val qty:Double,
    @ColumnInfo(name = "qtyUnit") val qtyUnit:String?,
    @ColumnInfo(name = "price") val price:Double,
    @ColumnInfo(name = "purchase_date") val date:String,
    @ColumnInfo(name = "category") val category:String?,
    @ColumnInfo(name = "merchant") val merchant:String?)
