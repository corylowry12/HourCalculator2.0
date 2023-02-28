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
import com.cory.hourcalculator.adapters.TimeCardItemCustomAdapter
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import java.math.RoundingMode

class TimeCardItemInfoFragment : Fragment() {

    private val timeCardItemInfoDataList = ArrayList<HashMap<String, String>>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var timeCardItemCustomAdapter: TimeCardItemCustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_card_item_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeCardItemCustomAdapter = TimeCardItemCustomAdapter(requireContext(), timeCardItemInfoDataList)
        linearLayoutManager = LinearLayoutManager(requireContext())

        activity?.window?.setBackgroundDrawable(null)

        val id = arguments?.getString("id")
        Toast.makeText(requireContext(), id.toString(), Toast.LENGTH_SHORT).show()

        loadIntoList(id!!)
    }

    private fun loadIntoList(id : String) {

        val dbHandler = TimeCardsItemDBHelper(requireActivity().applicationContext, null)

        timeCardItemInfoDataList.clear()
        val cursor = dbHandler.getAllRow(requireContext(), id)
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(TimeCardsItemDBHelper.COLUMN_ID))
            map["inTime"] = cursor.getString(cursor.getColumnIndex(TimeCardsItemDBHelper.COLUMN_IN))
            map["outTime"] = cursor.getString(cursor.getColumnIndex(TimeCardsItemDBHelper.COLUMN_OUT))
            map["totalHours"] = cursor.getString(cursor.getColumnIndex(TimeCardsItemDBHelper.COLUMN_TOTAL))
            map["breakTime"] = cursor.getString(cursor.getColumnIndex(TimeCardsItemDBHelper.COLUMN_BREAK))
            map["day"] = cursor.getString(cursor.getColumnIndex(TimeCardsItemDBHelper.COLUMN_DAY))
            timeCardItemInfoDataList.add(map)

            cursor.moveToNext()
        }


        val timeCardRecyclerView = activity?.findViewById<RecyclerView>(R.id.timeCardItemInfoRecyclerView)
        timeCardRecyclerView?.layoutManager = linearLayoutManager
        timeCardRecyclerView?.adapter = timeCardItemCustomAdapter

    }
}