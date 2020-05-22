package com.example.receiptreader.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.receiptreader.data.ExpenseRepository
import com.example.receiptreader.data.Item
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class ReceiptViewModel(application: Application) : AndroidViewModel(application) {

    fun parseReciept(jsonString : String) : List<Item> {
        val gson = GsonBuilder().create()
        val itemListType = object : TypeToken<ArrayList<Item>>(){}.type
        val itemList = gson.fromJson<List<Item>>(jsonString, itemListType)
        return itemList
    }

    fun saveReceipt(itemList : List<Item>) {
        ExpenseRepository.saveExpenses(itemList)
    }

    fun totalAmount(itemList: List<Item>) : Double {
        var total = 0.0
        for(item in itemList) {
            total += item.price
        }

        return total
    }
}