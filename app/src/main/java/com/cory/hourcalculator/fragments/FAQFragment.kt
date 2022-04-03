package com.cory.hourcalculator.fragments

import android.animation.LayoutTransition
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class FAQFragment : Fragment() {

    val client = OkHttpClient()
    private val dataList = ArrayList<HashMap<String, String>>()
    private lateinit var dialog: MaterialAlertDialogBuilder

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

            dialog.setTitle("Fetching Frequently Asked Questions...")
            dialog.setView(progressBar)
            dialog.setNegativeButton("Cancel") { d, _ ->
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
            runFAQ("https://raw.githubusercontent.com/corylowry12/faq_json/main/faq.json")
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

    fun runFAQ(url : String) {
        val request = Request.Builder()
            .url(url)
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

                val str_response = response.body()!!.string()
                //creating json object
                val json_contact:JSONObject = JSONObject(str_response)
                //creating json array
                val jsonarray_info: JSONArray = json_contact.getJSONArray("faq")

                val size:Int = jsonarray_info.length()

                for (i in 0 until size) {
                    val json_objectdetail: JSONObject =jsonarray_info.getJSONObject(i)

                    val arrayList_details = HashMap<String, String>()
                    arrayList_details["question"] = (json_objectdetail.get("question").toString())
                    arrayList_details["answer"] = (json_objectdetail.get("answer").toString())
                    dataList.add(arrayList_details)

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