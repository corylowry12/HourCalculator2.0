package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.adapters.TimeCardCustomAdapter
import com.cory.hourcalculator.adapters.TimeCardItemCustomAdapter
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import com.cory.hourcalculator.classes.FollowSystemVersion
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import java.math.RoundingMode

class TimeCardItemInfoFragment : Fragment() {

    private val timeCardItemInfoDataList = ArrayList<HashMap<String, String>>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var timeCardItemCustomAdapter: TimeCardItemCustomAdapter

    private lateinit var editable : Editable

    var themeSelection = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                activity?.setTheme(R.style.Theme_DarkTheme)
                themeSelection = true
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
                themeSelection = false
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
                themeSelection = true
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                        themeSelection = false
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(
                            AccentColor(requireContext()).followSystemTheme(
                                requireContext()
                            )
                        )
                        themeSelection = true
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                        themeSelection = true
                    }
                }
            }
        }

        val accentColor = AccentColor(requireContext())
        val followSystemVersion = FollowSystemVersion(requireContext())

        when {
            accentColor.loadAccent() == 0 -> {
                activity?.theme?.applyStyle(R.style.teal_accent, true)
            }
            accentColor.loadAccent() == 1 -> {
                activity?.theme?.applyStyle(R.style.pink_accent, true)
            }
            accentColor.loadAccent() == 2 -> {
                activity?.theme?.applyStyle(R.style.orange_accent, true)
            }
            accentColor.loadAccent() == 3 -> {
                activity?.theme?.applyStyle(R.style.red_accent, true)
            }
            accentColor.loadAccent() == 4 -> {
                if (!followSystemVersion.loadSystemColor()) {
                    activity?.theme?.applyStyle(R.style.system_accent, true)
                } else {
                    if (themeSelection) {
                        activity?.theme?.applyStyle(R.style.system_accent_google, true)
                    } else {
                        activity?.theme?.applyStyle(R.style.system_accent_google_light, true)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_time_card_item_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBarTimeCardItem = view.findViewById<MaterialToolbar>(R.id.materialToolBarTimeCardItemInfo)
        topAppBarTimeCardItem?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        topAppBarTimeCardItem.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add -> {
                    Vibrate().vibration(requireContext())
                    return@setOnMenuItemClickListener true
                }
                else -> false
            }
        }

        timeCardItemCustomAdapter = TimeCardItemCustomAdapter(requireContext(), timeCardItemInfoDataList)
        linearLayoutManager = LinearLayoutManager(requireContext())

        activity?.window?.setBackgroundDrawable(null)

        val id = arguments?.getString("id")
        val name = arguments?.getString("name")

        loadIntoList(id!!)

        val timeCardInfoNameTextInput = activity?.findViewById<TextInputEditText>(R.id.timeCardInfoNameTextInput)

        editable = if (name != null) {
            Editable.Factory.getInstance().newEditable(name)
        } else {
            Editable.Factory.getInstance().newEditable("")
        }
        timeCardInfoNameTextInput?.text = editable

        timeCardInfoNameTextInput?.setOnKeyListener(View.OnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard(timeCardInfoNameTextInput)
                return@OnKeyListener true
            }
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                TimeCardDBHelper(requireContext(), null).updateName(timeCardInfoNameTextInput.text.toString(), id)
                hideKeyboard(timeCardInfoNameTextInput)
                return@OnKeyListener true
            }
            false
        })

        timeCardInfoNameTextInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                    TimeCardDBHelper(requireContext(), null).updateName(s.toString(), id)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TimeCardDBHelper(requireContext(), null).updateName(s.toString(), id)
            }
        })
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

    private fun hideKeyboard(nameEditText: TextInputEditText) {
        val inputManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = activity?.currentFocus

        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(
                focusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            if (nameEditText.hasFocus()) {
                nameEditText.clearFocus()
            }
        }
    }
}