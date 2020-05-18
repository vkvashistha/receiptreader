package com.example.receiptreader.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object ExpenseRepository {
    fun getExpenses(filter : Filters) : LiveData<ArrayList<Item>> {
        val result = MutableLiveData<ArrayList<Item>>()
        return result;
    }

    fun saveExpenses(items : ArrayList<Item>) {

    }
}