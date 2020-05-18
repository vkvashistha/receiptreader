package com.example.receiptreader.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptreader.R
import com.example.receiptreader.adapter.ExpenseFilteredListAdapter
import com.example.receiptreader.data.Filters
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        return inflater.inflate(R.layout.fragment_expense_screen, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filteredListView = view.findViewById(R.id.filtered_list)
        val fromDate = et_from_date.text.toString()
        val toDate = et_to_date.text.toString()
        val category = et_category.text.toString()
        val merchant = et_merchant.text.toString()

        btn_apply_filter.setOnClickListener {
         val filteredList =   expenseViewModel.getFilteredList(Filters(fromDate,toDate,category,merchant))

            filteredListView.apply {
                adapter = ExpenseFilteredListAdapter(filteredList)
                layoutManager = LinearLayoutManager(context)
            }
        }



    }


}