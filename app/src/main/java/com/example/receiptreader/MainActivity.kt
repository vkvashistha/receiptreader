package com.example.receiptreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.receiptreader.data.ExpenseRepository
import com.example.receiptreader.data.Filters
import com.example.receiptreader.data.Item
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_text.setOnClickListener {
            val listData = ExpenseRepository.getExpenses(Filters())
            listData.observe(this@MainActivity, Observer { items->
                var text = ""

                for(item in items) {
                    text += item.itemName
                }

                tv_text.text = text
            })
        }
        ExpenseRepository.initializeRepository(application)
//        loadMockData()

    }

    private fun loadMockData() {
        val mockItems = ArrayList<Item>()
        mockItems.add(Item(0,"Masaala Oats", 1.0, "kg", 238.0, "2020-05-19","Daily Meals", "City Market"))
        mockItems.add(Item(0,"Maggy", 400.0, "g", 80.0, "2020-05-19","Snacks", "City Market"))
        mockItems.add(Item(0,"Coalgate", 100.0, "g", 80.0, "2020-05-19","Bathroom", "City Market"))
        mockItems.add(Item(0,"Lux Soap", 4.0, null, 120.0, "2020-05-19","Bathroom", "City Market"))
        mockItems.add(Item(0,"Razer", 10.0, null, 100.0, "2020-05-19","Self Care", "City Market"))
        mockItems.add(Item(0,"Bread", 400.0, "g", 100.0, "2020-05-19","Breakfast", "City Market"))
        mockItems.add(Item(0,"Haldiram Peanuts", 400.0, "g", 80.0, "2020-05-19","Snacks", "City Market"))
        mockItems.add(Item(0,"Coca Cola", 2.0, "ltr", 95.0, "2020-05-19","Drinks", "City Market"))


        mockItems.add(Item(0,"100 pipers", 6.0, "ltr", 16314.0, "2020-05-23","Alcohol", "Vanilla Spirit Zone"))
        mockItems.add(Item(0,"Maggy", 400.0, "g", 80.0, "2020-05-20","Snacks", "City Market"))
        mockItems.add(Item(0,"Coalgate", 100.0, "g", 80.0, "2020-05-20","Bathroom", "City Market"))
        mockItems.add(Item(0,"Lux Soap", 4.0, null, 120.0, "2020-05-20","Bathroom", "City Market"))
//        mockItems.add(Item(0,"Razer", 10.0, null, 100.0, "2020-05-20","Self Care", "City Market"))
//        mockItems.add(Item(0,"Bread", 400.0, "g", 100.0, "2020-05-20","Breakfast", "City Market"))
//        mockItems.add(Item(0,"Haldiram Peanuts", 400.0, "g", 80.0, "2020-05-20","Snacks", "City Market"))
//        mockItems.add(Item(0,"Coca Cola", 2.0, "ltr", 95.0, "2020-05-20","Drinks", "City Market"))

        ExpenseRepository.saveExpenses(mockItems)

    }
}
