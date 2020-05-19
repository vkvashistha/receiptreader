package com.example.receiptreader.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.receiptreader.R
import kotlinx.android.synthetic.main.fragment_home_screen.*

class HomeScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btn_add_bill.setOnClickListener {
            findNavController().navigate(R.id.action_add_new_receipt)
        }


        btn_show_expenses.setOnClickListener {
            findNavController().navigate(R.id.action_show_expenses)
        }
    }

}