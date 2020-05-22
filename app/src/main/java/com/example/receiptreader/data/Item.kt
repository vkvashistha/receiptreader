package com.example.receiptreader.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "items")
data class Item(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "item_name") var itemName : String, var qty:Double,
    @ColumnInfo(name = "qtyUnit") var qtyUnit:String?,
    @ColumnInfo(name = "price") var price:Double,
    @ColumnInfo(name = "purchase_date") var date:String,
    @ColumnInfo(name = "category") var category:String?,
    @ColumnInfo(name = "merchant") val merchant:String?) : Serializable
