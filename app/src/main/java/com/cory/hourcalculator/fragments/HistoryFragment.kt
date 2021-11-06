package com.cory.hourcalculator.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ListView
import android.widget.Toast
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HistoryFragment : Fragment() {

    private var output : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)

        topAppBar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.info -> {
                    // Handle edit text press

                    val alert = MaterialAlertDialogBuilder(requireActivity())
                    alert.setTitle("Info")
                    alert.setMessage("Total Hours: $output")
                    alert.setPositiveButton("OK", null)
                    alert.show()
                    true
                }

                else -> false
            }
        }

        loadIntoList()
    }

    private fun loadIntoList() {

        val dbHandler = DBHelper(requireContext(), null)
        val dataList = ArrayList<HashMap<String, String>>()

        var y = 0.0

        //Toast.makeText(requireContext(), dbHandler.getCount().toString(), Toast.LENGTH_LONG).show()

        dataList.clear()
        val cursor = dbHandler.getAllRow(requireActivity())
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
            map["intime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
            map["out"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
            map["break"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
            map["total"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
            map["day"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))
            dataList.add(map)

            val array = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)).toString()

            y += array.toDouble()

            output = String.format("%.2f", y)
            //textViewTotalHours.text = getString(R.string.total_hours_history, output)

           /* if (wagesData.loadWageAmount() != "") {
                try {
                    val wages = output.toDouble() * wagesData.loadWageAmount().toString().toDouble()
                    val wagesrounded = String.format("%.2f", wages)
                    textViewWages.text = getString(R.string.total_wages, wagesrounded)
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    textViewWages.text = getString(R.string.there_is_a_problem_calculating_wages)
                }
            }*/
            cursor.moveToNext()

        }
        //textViewSize.text = getString(R.string.amount_of_hours_saved, dbHandler.getCount())
        val listView = activity?.findViewById<ListView>(R.id.listView)
        listView?.adapter = CustomAdapter(requireContext(), dataList, HistoryFragment())

    }

    fun update() {
        val listView = activity?.findViewById<ListView>(R.id.listView)
        val index = listView?.firstVisiblePosition
        val v = listView?.getChildAt(0)
        val top = if (v == null) 0 else v.top - listView.paddingTop

        loadIntoList()
        listView?.setSelectionFromTop(index!!, top)

    }
}