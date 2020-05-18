package com.example.receiptreader.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.receiptreader.R
import com.example.receiptreader.data.Filters
import com.example.receiptreader.data.Item
import com.example.receiptreader.viewmodel.StatsViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate

class StatsScreenFragment : Fragment() {

    private val statsViewModel: StatsViewModel by lazy {
        getViewModelProvider(this, null).get(
            StatsViewModel::class.java
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
        setHasOptionsMenu(false)
        return inflater.inflate(R.layout.fragment_stats_screen, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filters = Filters("2020/05/10", "2020/05/15", "Grocery", "Big Bazaar")
        val itemList: java.util.ArrayList<Item>
        itemList = statsViewModel.getExpenses(filters)
        setBarChart(view, itemList)
        setPieChart(view, itemList)
    }

    private fun setBarChart(
        view: View,
        itemList: ArrayList<Item>
    ) {
        val barChart = view.findViewById<BarChart>(R.id.barchart)
        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until itemList.size) {
            entries.add(BarEntry(itemList[i].price.toFloat(), 0))
        }
        val bardataset = BarDataSet(entries, "Cells")
        val labels = ArrayList<String>()
        for (i in 0 until itemList.size){
            labels.add(itemList[i].category.toString())
        }
        val data = BarData(labels, bardataset)
        barChart.data = data // set the data and list of labels into chart
        barChart.setDescription("Set Bar Chart Description Here") // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS)
        barChart.animateY(5000)
    }

    private fun setPieChart(
        view: View,
        itemList: ArrayList<Item>
    ) {
        val piechart = view.findViewById<PieChart>(R.id.piechart)
        val pieChartEntries: ArrayList<Entry> = ArrayList()
        for (i in 0 until itemList.size) {
            pieChartEntries.add(Entry(itemList[i].price.toFloat(), 0))
        }
        val piedataset = PieDataSet(pieChartEntries, "Cells")
        val label = ArrayList<String>()
        for (i in 0 until itemList.size){
            label.add(itemList[i].category.toString())
        }
        val piedata = PieData(label, piedataset)
        piechart.data = piedata // set the data and list of labels into chart
        piechart.setDescription("Set Bar Chart Description Here") // set the description
        piedataset.setColors(ColorTemplate.COLORFUL_COLORS)
        piechart.animateY(5000)
    }

}