@file:OptIn(DelicateCoroutinesApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.FAQCustomAdapter
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import com.cory.hourcalculator.classes.FollowSystemVersion
import com.cory.hourcalculator.classes.Vibrate
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class FAQFragment : Fragment() {

    private val client = OkHttpClient()
    private val dataList = ArrayList<HashMap<String, String>>()
    private val selectedItems = ArrayList<HashMap<String, String>>()

    lateinit var faqCustomAdapter : FAQCustomAdapter

    var themeSelection = false

    private lateinit var recyclerViewState: Parcelable

    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        faqCustomAdapter = FAQCustomAdapter(requireContext(), dataList)
    }

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
                        activity?.setTheme(AccentColor(requireContext()).followSystemTheme(requireContext()))
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
                }
                else {
                    if (themeSelection) {
                        activity?.theme?.applyStyle(R.style.system_accent_google, true)
                    }
                    else {
                        activity?.theme?.applyStyle(R.style.system_accent_google_light, true)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_f_a_q, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.faqRecyclerView)

        recyclerView.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            //recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()!!
        }

        val search = view.findViewById<TextInputEditText>(R.id.search)

        search?.setOnKeyListener(View.OnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_DOWN) {
                search.clearFocus()
                hideKeyboard()
                return@OnKeyListener true
            }
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                hideKeyboard()
                search.clearFocus()
                return@OnKeyListener true
            }
            false
        })

        search?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                selectedItems.clear()
                try {
                    if (s.toString() != "") {
                        for (i in 0 until dataList.count()) {
                            if (dataList[i]["question"]!!.contains(s.toString())) {
                                selectedItems.add(dataList[i])

                                 faqCustomAdapter = FAQCustomAdapter(requireContext(), selectedItems)

                                recyclerView.adapter = faqCustomAdapter
                                recyclerView.invalidate()
                            }
                        }
                    }
                    else {
                        faqCustomAdapter = FAQCustomAdapter(requireContext(), dataList)

                        recyclerView.adapter = faqCustomAdapter
                        recyclerView.invalidate()
                        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                selectedItems.clear()
                try {
                    if (s.toString() != "") {
                        for (i in 0 until dataList.count()) {
                            if (dataList[i]["question"]!!.contains(s.toString())) {
                                selectedItems.add(dataList[i])

                                faqCustomAdapter = FAQCustomAdapter(requireContext(), selectedItems)

                                recyclerView.adapter = faqCustomAdapter
                                recyclerView.invalidate()
                            }
                        }
                    }
                    else {
                        faqCustomAdapter = FAQCustomAdapter(requireContext(), dataList)

                        recyclerView.adapter = faqCustomAdapter
                        recyclerView.invalidate()
                        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedItems.clear()
                try {
                    if (s.toString() != "") {
                        for (i in 0 until dataList.count()) {
                            if (dataList[i]["question"]!!.contains(s.toString())) {
                                selectedItems.add(dataList[i])

                                faqCustomAdapter = FAQCustomAdapter(requireContext(), selectedItems)

                                recyclerView.adapter = faqCustomAdapter
                                recyclerView.invalidate()
                            }
                        }
                    }
                    else {
                        faqCustomAdapter = FAQCustomAdapter(requireContext(), dataList)

                        recyclerView.adapter = faqCustomAdapter
                        recyclerView.invalidate()
                        //recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        val dialog = MaterialAlertDialogBuilder(
            requireContext(),
            AccentColor(requireContext()).alertTheme())
            val progressBar =
                ProgressBar(requireContext(), null, android.R.attr.progressBarStyle)

            dialog.setTitle(getString(R.string.fetching_frequently_asked_questions))
            dialog.setView(progressBar)
            dialog.setNegativeButton(getString(R.string.cancel)) { d, _ ->
                d.dismiss()
            }
            val d = dialog.create()
            d.show()


        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarFAQ)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            runFAQ()
        }, 100)

        Handler(Looper.getMainLooper()).postDelayed({
            d.dismiss()
        }, 1000)

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.supportFragmentManager?.popBackStack()
                }
            })
    }

    private fun runFAQ() {
        val request = Request.Builder()
            .url("https://raw.githubusercontent.com/corylowry12/faq_json/main/faq.json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                GlobalScope.launch(Dispatchers.Main) {
                    val alert = MaterialAlertDialogBuilder(
                        requireContext(),
                        AccentColor(requireContext()).alertTheme()
                    )
                    alert.setTitle("Error")
                    alert.setMessage("There was an error fetching Frequently Asked Questions. Check your data connection.")
                    alert.setPositiveButton("OK") { _, _ ->
                        activity?.supportFragmentManager?.popBackStack()
                    }
                    alert.show()
                }
            }

            override fun onResponse(call: Call, response: Response) {

                val strResponse = response.body()!!.string()

                val jsonContact = JSONObject(strResponse)
                //creating json array
                val jsonArrayInfo: JSONArray = jsonContact.getJSONArray("faq")

                val size:Int = jsonArrayInfo.length()

                for (i in 0 until size) {
                    val jsonObjectDetail: JSONObject =jsonArrayInfo.getJSONObject(i)

                    val arrayListDetails = HashMap<String, String>()
                    arrayListDetails["question"] = (jsonObjectDetail.get("question").toString())
                    arrayListDetails["answer"] = (jsonObjectDetail.get("answer").toString())
                    dataList.add(arrayListDetails)

                }

                GlobalScope.launch(Dispatchers.Main) {
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = faqCustomAdapter

                }
            }
        })
    }

    private fun hideKeyboard() {
        val inputManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = activity?.currentFocus

        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(
                focusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}