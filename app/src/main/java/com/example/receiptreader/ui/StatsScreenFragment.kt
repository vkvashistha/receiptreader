package com.example.receiptreader.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.receiptreader.R
import com.example.receiptreader.data.Filters
import com.example.receiptreader.data.Item
import com.example.receiptreader.viewmodel.StatsViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_stats_screen.*
import java.util.*


class StatsScreenFragment : Fragment() {

    private lateinit var statsViewModel: StatsViewModel
    private lateinit var layouts: IntArray
    private var mAdapter: ViewsSliderAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        statsViewModel = getViewModelProvider(this, null).get(StatsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_stats_screen, container, false)
    }

    fun getViewModelProvider(
        fragment: Fragment,
        factory: ViewModelProvider.Factory?
    ): ViewModelProvider {
        return ViewModelProviders.of(fragment, factory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fromDate = et_from_date.text.toString()
        val toDate = et_to_date.text.toString()
        val category = et_category.text.toString()
        val merchant = et_merchant.text.toString()

        statsViewModel.getExpenses(Filters(fromDate, toDate, category, merchant))

        statsViewModel.itemsLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it.size > 0) {
                init(view, it)
            }
        })
    }

    private fun init(
        view: View,
        itemList: ArrayList<Item>
    ) {
        layouts = intArrayOf(
            R.layout.fragment_barchart,
            R.layout.fragment_piechart
        )
        mAdapter = ViewsSliderAdapter(layouts, itemList)

        view.findViewById<ViewPager2>(R.id.view_pager).adapter = mAdapter

        view.findViewById<ViewPager2>(R.id.view_pager)
            .registerOnPageChangeCallback(pageChangeCallback)
    }

    /*
     * ViewPager page change listener
     */
    var pageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }

    open class ViewsSliderAdapter(
        private var layouts: IntArray,
        private var itemList: ArrayList<Item>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
            return SliderViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int
        ) {
            if (position == 0) {
                setBarChart(holder.itemView)
            } else {
                setPieChart(holder.itemView)
            }
        }

        private fun setPieChart(
            view: View
        ) {
            val hashMap = HashMap<String, Float>()
            for (i in 0 until itemList.size) {
                hashMap[itemList[i].category.toString()] = itemList[i].price.toFloat()
            }

            val piechart = view.findViewById<PieChart>(R.id.piechart)
            val pieChartEntries: ArrayList<Entry> = ArrayList()
            for (i in 0 until itemList.size) {
                pieChartEntries.add(
                    Entry(
                        getExpenseForGivenCategory(
                            itemList[i].category.toString(),
                            hashMap
                        ), 0
                    )
                )
            }
            val piedataset = PieDataSet(pieChartEntries, "Cells")
            val label = ArrayList<String>()
            for (i in 0 until itemList.size) {
                label.add(itemList[i].category.toString())
            }
            val piedata = PieData(label, piedataset)
            piechart?.data = piedata // set the data and list of labels into chart
            piechart?.setDescription("Set Bar Chart Description Here") // set the description
            piedataset.setColors(ColorTemplate.COLORFUL_COLORS)
            piechart?.animateY(5000)
        }

        private fun setBarChart(itemView: View) {
            val barChart = itemView.findViewById<BarChart>(R.id.barchart)
            val hashMap = HashMap<String, Float>()
            for (i in 0 until itemList.size) {
                hashMap[itemList[i].category.toString()] = itemList[i].price.toFloat()
            }
            val entries: ArrayList<BarEntry> = ArrayList()
            for (i in 0 until itemList.size) {
                entries.add(
                    BarEntry(
                        getExpenseForGivenCategory(
                            itemList[i].category.toString(),
                            hashMap
                        ), 0
                    )
                )
            }
            val bardataset = BarDataSet(entries, "Cells")
            val labels = ArrayList<String>()
            for (i in 0 until itemList.size) {
                labels.add(itemList[i].category.toString())
            }
            val data = BarData(labels, bardataset)
            barChart.data = data // set the data and list of labels into chart
            barChart.setDescription("Set Bar Chart Description Here") // set the description
            bardataset.setColors(ColorTemplate.COLORFUL_COLORS)
            barChart.animateY(5000)
        }

        private fun getExpenseForGivenCategory(
            category: String,
            hashMap: HashMap<String, Float>
        ): Float {
            return hashMap[category]!!
        }

        override fun getItemViewType(position: Int): Int {
            return layouts[position]
        }

        override fun getItemCount(): Int {
            return layouts.size
        }

        inner class SliderViewHolder(view: View?) :
            RecyclerView.ViewHolder(view!!) {
        }
    }
}