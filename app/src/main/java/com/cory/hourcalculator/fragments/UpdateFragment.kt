package com.cory.hourcalculator.fragments

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.KnownIssuesAdapter
import com.cory.hourcalculator.adapters.UpdateAdapter
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.Vibrate
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException

class UpdateFragment : Fragment() {

    val client = OkHttpClient()
    private val dataList = ArrayList<HashMap<String, String>>()

    private val dataListKnownIssues = ArrayList<HashMap<String, String>>()

    private val dataListRoadMap = ArrayList<HashMap<String, String>>()

    private lateinit var alert : MaterialAlertDialogBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBar = view.findViewById<MaterialToolbar>(R.id.materialToolBarUpdate)

        appBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val layout = requireView().findViewById<LinearLayout>(R.id.linearLayoutAppNews)
        val layoutTransition = layout.layoutTransition
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        val dialog = MaterialAlertDialogBuilder(
            requireContext(),
            AccentColor(requireContext()).alertTheme())
        val progressBar =
            ProgressBar(requireContext(), null, android.R.attr.progressBarStyleLarge)

        dialog.setTitle("Fetching The Latest App News...")
        dialog.setView(progressBar)
        dialog.setNegativeButton("Cancel") { d, _ ->
            d.dismiss()
            activity?.supportFragmentManager?.popBackStack()
        }
        val d = dialog.create()
        d.show()


        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarUpdate)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.closeAll -> {

                    val updateRecyclerView = requireView().findViewById<RecyclerView>(R.id.updateRecyclerView)
                    val knownIssuesRecyclerView = requireView().findViewById<RecyclerView>(R.id.knownIssuesRecyclerView)
                    val upcomingFeaturesRecyclerView = requireView().findViewById<RecyclerView>(R.id.upcomingRecyclerView)

                    if (updateRecyclerView.visibility == View.VISIBLE) {
                        updateRecyclerView.visibility = View.GONE
                    }
                    if (knownIssuesRecyclerView.visibility == View.VISIBLE) {
                        knownIssuesRecyclerView.visibility = View.GONE
                    }

                    if (upcomingFeaturesRecyclerView.visibility == View.VISIBLE) {
                        upcomingFeaturesRecyclerView.visibility = View.GONE
                    }
                    true
                }
                else -> true
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            run("https://raw.githubusercontent.com/corylowry12/update_json/main/update_json.json")
            //runKnownIssues("https://raw.githubusercontent.com/corylowry12/update_json/main/update_json.json")
            //runRoadmap("https://raw.githubusercontent.com/corylowry12/update_json/main/update_json.json")
        }, 100)

        Handler(Looper.getMainLooper()).postDelayed({
            d.dismiss()
        }, 1000)

        val updatesConstraint = requireView().findViewById<ConstraintLayout>(R.id.updatesConstraint)

        updatesConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val updatesChevron = requireView().findViewById<ImageView>(R.id.updatesChevronImage)
            val recyclerViewUpdates = requireView().findViewById<RecyclerView>(R.id.updateRecyclerView)

            if (recyclerViewUpdates.visibility == View.VISIBLE) {
                recyclerViewUpdates.visibility = View.GONE
                updatesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
            else {
                recyclerViewUpdates.visibility = View.VISIBLE
                updatesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
        }

        val knownIssuesConstraint = requireView().findViewById<ConstraintLayout>(R.id.knownIssuesConstraint)

        knownIssuesConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val knownIssuesChevron = requireView().findViewById<ImageView>(R.id.knownIssuesChevronImage)
            val recyclerViewKnownIssues = requireView().findViewById<RecyclerView>(R.id.knownIssuesRecyclerView)

            if (recyclerViewKnownIssues.visibility == View.VISIBLE) {
                recyclerViewKnownIssues.visibility = View.GONE
                knownIssuesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
            else {
                recyclerViewKnownIssues.visibility = View.VISIBLE
                knownIssuesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }

        }
        val upcomingConstraint = requireView().findViewById<ConstraintLayout>(R.id.upcomingConstraint)

        upcomingConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val upcomingChevron = requireView().findViewById<ImageView>(R.id.upcomingChevronImage)
            val recyclerViewRoadMap = requireView().findViewById<RecyclerView>(R.id.upcomingRecyclerView)

                if (recyclerViewRoadMap.visibility == View.VISIBLE) {
                    recyclerViewRoadMap.visibility = View.GONE
                    upcomingChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                }
                else {
                    recyclerViewRoadMap.visibility = View.VISIBLE
                    upcomingChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                }
        }
    }

    @DelicateCoroutinesApi
    fun run(url : String) {
        dataList.clear()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                GlobalScope.launch(Dispatchers.Main) {
                    alert = MaterialAlertDialogBuilder(
                        requireContext(),
                        AccentColor(requireContext()).alertTheme()
                    )
                    alert.setTitle("Error")
                    alert.setMessage("There was an error fetching the latest news. Check your data connection.")
                    alert.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        activity?.supportFragmentManager?.popBackStack()
                    }
                    if (!alert.create().isShowing) {
                        alert.show()
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val str_response = response.body()!!.string()
                //creating json object
                val json_contact:JSONObject = JSONObject(str_response)
                //creating json array
                val jsonarray_info: JSONArray = json_contact.getJSONArray("update")

                val size:Int = jsonarray_info.length()

                for (i in 0 until size) {
                    val json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)

                    val arrayList_details = HashMap<String, String>()
                    arrayList_details["date"] = json_objectdetail.get("date").toString()
                    arrayList_details["title"] = (json_objectdetail.get("title").toString())
                    arrayList_details["body"] = (json_objectdetail.get("body").toString())
                    dataList.add(arrayList_details)
                }

                val jsonarray_info_known_issues: JSONArray = json_contact.getJSONArray("known issues")

                val size_known_issues:Int = jsonarray_info.length()

                for (i in 0 until size_known_issues) {
                    val json_objectdetail:JSONObject=jsonarray_info_known_issues.getJSONObject(i)

                    val arrayList_details = HashMap<String, String>()
                    arrayList_details["title"] = (json_objectdetail.get("title").toString())
                    dataListKnownIssues.add(arrayList_details)

                }

                val recyclerViewKnownIssues = requireView().findViewById<RecyclerView>(R.id.knownIssuesRecyclerView)

                GlobalScope.launch(Dispatchers.Main) {
                    recyclerViewKnownIssues?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerViewKnownIssues?.adapter = KnownIssuesAdapter(requireContext(), dataListKnownIssues)
                }

                val recyclerViewUpdate = requireView().findViewById<RecyclerView>(R.id.updateRecyclerView)

                GlobalScope.launch(Dispatchers.Main) {
                    recyclerViewUpdate?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerViewUpdate?.adapter = UpdateAdapter(requireContext(), dataList)
                }

                val jsonarray_info_road_map: JSONArray = json_contact.getJSONArray("roadmap")

                val size_road_map:Int = jsonarray_info.length()

                for (i in 0 until size_road_map) {
                    val json_objectdetail:JSONObject=jsonarray_info_road_map.getJSONObject(i)

                    val arrayList_details = HashMap<String, String>()
                    arrayList_details["title"] = (json_objectdetail.get("title").toString())
                    dataListRoadMap.add(arrayList_details)
                }

                val recyclerViewRoadMap = requireView().findViewById<RecyclerView>(R.id.upcomingRecyclerView)

                GlobalScope.launch(Dispatchers.Main) {
                    recyclerViewRoadMap?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerViewRoadMap?.adapter = KnownIssuesAdapter(requireContext(), dataListRoadMap)

                }

            }
        })
    }
}