package com.example.receiptreader.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.receiptreader.data.Item
import com.google.gson.Gson


class ReceiptViewModel(application: Application) : AndroidViewModel(application) {

    fun parseReciept(jsonString : String) : ArrayList<Item> {
        return ArrayList()
    }
    fun saveReceipt() {

    }
}