package com.cory.hourcalculator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.adapters.TimeCardCustomAdapter
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.database.TimeCardDBHelper
import java.math.RoundingMode

class TimeCardsFragment : Fragment() {

    private val dataList = ArrayList<HashMap<String, String>>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var timeCardCustomAdapter: TimeCardCustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeCardCustomAdapter = TimeCardCustomAdapter(requireContext(), dataList)
        linearLayoutManager = LinearLayoutManager(requireContext())

        activity?.window?.setBackgroundDrawable(null)

        loadIntoList()
    }

    private fun loadIntoList() {

        val dbHandler = TimeCardDBHelper(requireActivity().applicationContext, null)

        dataList.clear()
        val cursor = dbHandler.getAllRow(requireContext())
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_ID))
            map["totalHours"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_TOTAL))
            map["week"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_WEEK))
            dataList.add(map)

            cursor.moveToNext()
        }


        val timeCardRecyclerView = activity?.findViewById<RecyclerView>(R.id.timeCardsRecyclerView)
        timeCardRecyclerView?.layoutManager = linearLayoutManager
        timeCardRecyclerView?.adapter = timeCardCustomAdapter

    }
}