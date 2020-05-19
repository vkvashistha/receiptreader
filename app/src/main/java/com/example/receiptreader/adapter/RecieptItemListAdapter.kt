package com.example.receiptreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptreader.R
import com.example.receiptreader.data.Item
import kotlinx.android.synthetic.main.custom_row_expense_screen.view.*
import java.util.ArrayList

class RecieptItemListAdapter(val items: List<Item>) :
    RecyclerView.Adapter<RecieptItemListAdapter.ExpenseListItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseListItemViewHolder {
        val filteredListView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_add_expense, parent, false)
        return ExpenseListItemViewHolder(filteredListView)
    }

    override fun getItemCount(): Int {
            return items.size

    }

    override fun onBindViewHolder(holder: ExpenseListItemViewHolder, position: Int) {
        holder.onBindView(position)
    }


    inner class ExpenseListItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBindView(position: Int) {
            itemView.tv_category.text = items[position].category
            itemView.tv_item.text = items[position].itemName
            itemView.tv_quantity.text = items[position].qtyUnit
            itemView.tv_price.text = items[position].price.toString()

        }
    }
}