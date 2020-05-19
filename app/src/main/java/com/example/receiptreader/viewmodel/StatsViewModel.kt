package com.example.receiptreader.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.receiptreader.data.ExpenseRepository
import com.example.receiptreader.data.Filters
import com.example.receiptreader.data.Item

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    val itemsLiveData: LiveData<java.util.ArrayList<Item>> = ExpenseRepository.getExpenses(Filters())

    fun getExpenses(filters: Filters) : LiveData<ArrayList<Item>> {
        return ExpenseRepository.getExpenses(filters)
    }
}