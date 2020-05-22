package com.example.receiptreader.ui

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receiptreader.R
import com.example.receiptreader.adapter.RecieptItemListAdapter
import com.example.receiptreader.data.Item
import com.example.receiptreader.data.ItemCategories
import com.example.receiptreader.viewmodel.ReceiptViewModel
import kotlinx.android.synthetic.main.fragment_add_receipt_screen.*

class ReceiptScreenFragment : Fragment() {
    private val receiptViewModel: ReceiptViewModel by lazy {
        getViewModelProvider(this, null).get(
            ReceiptViewModel::class.java
        )
    }

    private fun getViewModelProvider(
        fragment: Fragment,
        factory: ViewModelProvider.Factory?
    ): ViewModelProvider {
        return ViewModelProviders.of(fragment, factory)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_receipt_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getSerializable("data")
        val list = data as ArrayList<Item>
        val date = data[0].date
        val merchant = data[0].merchant

        et_date.setText(date)
        et_merchant.setText(merchant)
        ItemCategories.autoCategorize(list).observe(viewLifecycleOwner, Observer {
            filtered_list.apply {
                adapter = RecieptItemListAdapter(it)
                layoutManager = LinearLayoutManager(context)
            }
        })


        tv_total_amount.text = receiptViewModel.totalAmount(list).toString()
        btn_save_receipt.setOnClickListener {
            receiptViewModel.saveReceipt(list)
            Toast.makeText(context, "Receipt Saved", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

    }
}