@file:OptIn(DelicateCoroutinesApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.cory.hourcalculator.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

                val recyclerView = requireView().findViewById<RecyclerView>(R.id.faqRecyclerView)

                GlobalScope.launch(Dispatchers.Main) {
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = FAQCustomAdapter(requireContext(), dataList)

                }
            }
        })
    }
}