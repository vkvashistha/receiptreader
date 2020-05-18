package com.example.receiptreader.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.receiptreader.data.ExpenseRepository
import com.example.receiptreader.data.Filters
import com.example.receiptreader.data.Item
import java.util.ArrayList

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {


    fun getFilteredList(filters: Filters):ArrayList<Item> {
       return ExpenseRepository.getExpenses(filters).value!!
    }

}