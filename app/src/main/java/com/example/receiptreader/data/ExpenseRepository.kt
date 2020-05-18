package com.example.receiptreader.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

object ExpenseRepository {
    private lateinit var itemDao: ItemDao
    private val itemsLiveData = MutableLiveData<ArrayList<Item>>()


    fun initializeRepository(application: Application) {
        itemDao = ItemsRoomDatabase.getDatabase(application).itemDao()
    }

    fun getExpenses(filter: Filters): LiveData<ArrayList<Item>> {
        GlobalScope.launch {
            val items = itemDao.getAllItems()
            val filterdItems = ArrayList<Item>()
            for (item in items) {
                if (!filter.startDate.isNullOrBlank() && compareDate(filter.startDate!!, item.date) > 0) {
                    continue
                }

                if (!filter.endDate.isNullOrBlank() && compareDate(filter.endDate!!, item.date) < 0) {
                    continue
                }

                if(!filter.category.isNullOrBlank() && filter.category != item.category) {
                    continue
                }

                if(!filter.merchant.isNullOrBlank() && filter.merchant != item.merchant) {
                    continue
                }

                filterdItems.add(item)
            }
            itemsLiveData.postValue(items)
        }
        return itemsLiveData
    }

    fun compareDate(left: String, right: String): Int {
        val format = SimpleDateFormat("yyyy-MM-dd")

        val date1: Date = format.parse(left)
        val date2: Date = format.parse(right)

        if (date1.compareTo(date2) < 0) {
            return -1
        } else if (date1.compareTo(date2) > 0) {
            return 1
        } else {
            return 0
        }
    }

    fun saveExpenses(items: ArrayList<Item>) {
        GlobalScope.launch(Dispatchers.IO) {
            for (item in items) {
                itemDao.insert(item)
            }
        }
    }
}