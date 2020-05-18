package com.example.receiptreader.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.receiptreader.data.ExpenseRepository
import com.example.receiptreader.data.Filters
import com.example.receiptreader.data.Item

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    fun getExpenses(filters: Filters) : ArrayList<Item> {
        return ExpenseRepository.getExpenses(filters).value!!
    }
}