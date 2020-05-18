package com.example.receiptreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptreader.R
import com.example.receiptreader.data.Item
import kotlinx.android.synthetic.main.custom_row_expense_screen.view.*
import java.util.ArrayList

class ExpenseFilteredListAdapter(filteredList: ArrayList<Item>) :
    RecyclerView.Adapter<ExpenseFilteredListAdapter.ExpenseListItemViewHolder>() {

    private val filteredList = filteredList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseListItemViewHolder {
        val filteredListView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_row_expense_screen, parent, false)
        return ExpenseListItemViewHolder(filteredListView)
    }

    override fun getItemCount(): Int {
            return filteredList.size

    }

    override fun onBindViewHolder(holder: ExpenseListItemViewHolder, position: Int) {
        holder.onBindView(position)
    }


    inner class ExpenseListItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBindView(position: Int) {
            itemView.tv_category.text = filteredList[position].category
            itemView.tv_item.text = filteredList[position].itemName
            itemView.tv_quantity.text = filteredList[position].qtyUnit
            itemView.tv_price.text = filteredList[position].price.toString()

        }
    }
}