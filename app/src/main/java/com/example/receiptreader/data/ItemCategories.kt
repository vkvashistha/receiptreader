package com.example.receiptreader.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object ItemCategories {
    val categoryMap = mapOf(
        "Parle" to "Snacks",
        "Kurkure" to "Snacks",
        "Shaktibhog Ata" to "Grocery",
        "Coca Cola" to "Soft Drinks"
    )
    fun autoCategorize(items : List<Item>) : LiveData<List<Item>> {
        val response = MutableLiveData<List<Item>>()
        for(item in items) {
            item.category = categoryMap[item.itemName]
        }

        response.postValue(items)
        return response
    }
}