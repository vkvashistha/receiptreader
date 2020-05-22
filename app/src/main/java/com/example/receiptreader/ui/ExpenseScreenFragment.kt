package com.example.receiptreader.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptreader.R
import com.example.receiptreader.adapter.ExpenseFilteredListAdapter
import com.example.receiptreader.data.Filters
import com.example.receiptreader.data.Item
import com.example.receiptreader.viewmodel.ExpenseViewModel
import kotlinx.android.synthetic.main.fragment_expense_screen.*

class ExpenseScreenFragment: Fragment() {



    private val expenseViewModel: ExpenseViewModel by lazy {
        getViewModelProvider(this, null).get(
            ExpenseViewModel::class.java
        )
    }

    private fun getViewModelProvider(
        fragment: Fragment,
        factory: ViewModelProvider.Factory?
    ): ViewModelProvider {
        return ViewModelProviders.of(fragment, factory)
    }

    lateinit var filteredListView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return activity?.layoutInflater?.inflate(R.layout.fragment_expense_screen, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filteredListView = view.findViewById(R.id.filtered_list)


        btn_apply_filter.setOnClickListener {
            val fromDate = et_from_date.text.toString()
            val toDate = et_to_date.text.toString()
            val category = et_category.text.toString()
            val merchant = et_merchant.text.toString()
          expenseViewModel.applyFilter(Filters(fromDate,toDate,category,merchant))

        }

        btn_show_stats.setOnClickListener {
            findNavController().navigate(R.id.action_show_stats)
        }

        expenseViewModel.itemsLiveData.observe(viewLifecycleOwner, Observer {
            if(it.size>0){
                filteredListView.apply {
                    adapter = ExpenseFilteredListAdapter(it)
                    (adapter as ExpenseFilteredListAdapter).notifyDataSetChanged()
                    layoutManager = LinearLayoutManager(context)
                    tv_total_amount.text = getTotalAmount(it).toString()
                }
            }

        })




    }


    private fun getTotalAmount(itemList:ArrayList<Item>):Double {
        var totalAmount = 0.0
        var i = 0
        while (itemList.size>0 && itemList.size>i) {
            totalAmount += itemList[i].price
            i++
        }
        return totalAmount
    }


}