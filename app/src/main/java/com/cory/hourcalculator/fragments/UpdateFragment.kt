package com.cory.hourcalculator.fragments

import android.animation.LayoutTransition
import android.os.Bundle
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

      // run("https://raw.githubusercontent.com/corylowry12/update_json/main/update_json.json")

        val updatesConstraint = requireView().findViewById<ConstraintLayout>(R.id.updatesConstraint)

        updatesConstraint.setOnClickListener {
            dataList.clear()

            Toast.makeText(requireContext(), "Fetching Data...", Toast.LENGTH_SHORT).show()

            run("https://raw.githubusercontent.com/corylowry12/update_json/main/update_json.json")
        }

        val knownIssuesConstraint = requireView().findViewById<ConstraintLayout>(R.id.knownIssuesConstraint)

        knownIssuesConstraint.setOnClickListener {
            dataListKnownIssues.clear()

            Toast.makeText(requireContext(), "Fetching Data...", Toast.LENGTH_SHORT).show()

            runKnownIssues("https://raw.githubusercontent.com/corylowry12/update_json/main/update_json.json")
        }
        val upcomingConstraint = requireView().findViewById<ConstraintLayout>(R.id.upcomingConstraint)

        upcomingConstraint.setOnClickListener {
            dataListRoadMap.clear()

            Toast.makeText(requireContext(), "Fetching Data...", Toast.LENGTH_SHORT).show()

            runRoadmap("https://raw.githubusercontent.com/corylowry12/update_json/main/update_json.json")
        }
    }

    @DelicateCoroutinesApi
    fun run(url : String) {
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
                    alert.setMessage("There was an error fetching the latest news. Check your data connection.")
                    alert.setPositiveButton("OK") { _, _ ->

                    }
                    alert.show()
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

                val recyclerView = requireView().findViewById<RecyclerView>(R.id.updateRecyclerView)
                val updateChevron = requireView().findViewById<ImageView>(R.id.updatesChevronImage)

                GlobalScope.launch(Dispatchers.Main) {
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = UpdateAdapter(requireContext(), dataList)

                    if (recyclerView.visibility == View.VISIBLE) {
                        recyclerView.visibility = View.GONE
                        updateChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }
                    else {
                        recyclerView.visibility = View.VISIBLE
                        updateChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                    }
                }

            }
        })
    }

    fun runKnownIssues(url : String) {
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
                    alert.setMessage("There was an error fetching the latest news. Check your data connection.")
                    alert.setPositiveButton("OK") { _, _ ->

                    }
                    alert.show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val str_response = response.body()!!.string()
                //creating json object
                val json_contact:JSONObject = JSONObject(str_response)
                //creating json array
                val jsonarray_info: JSONArray = json_contact.getJSONArray("known issues")

                val size:Int = jsonarray_info.length()

                for (i in 0 until size) {
                    val json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)

                    val arrayList_details = HashMap<String, String>()
                    arrayList_details["title"] = (json_objectdetail.get("title").toString())
                    dataListKnownIssues.add(arrayList_details)

                }

                val recyclerView = requireView().findViewById<RecyclerView>(R.id.knownIssuesRecyclerView)
                val knownIssuesChevron = requireView().findViewById<ImageView>(R.id.knownIssuesChevronImage)

                GlobalScope.launch(Dispatchers.Main) {
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = KnownIssuesAdapter(requireContext(), dataListKnownIssues)

                    if (recyclerView.visibility == View.VISIBLE) {
                        recyclerView.visibility = View.GONE
                        knownIssuesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }
                    else {
                        recyclerView.visibility = View.VISIBLE
                        knownIssuesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                    }
                }

            }
        })
    }

    fun runRoadmap(url : String) {
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
                    alert.setMessage("There was an error fetching the latest news. Check your data connection.")
                    alert.setPositiveButton("OK") { _, _ ->

                    }
                    alert.show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val str_response = response.body()!!.string()
                //creating json object
                val json_contact:JSONObject = JSONObject(str_response)
                //creating json array
                val jsonarray_info: JSONArray = json_contact.getJSONArray("roadmap")

                val size:Int = jsonarray_info.length()

                for (i in 0 until size) {
                    val json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)

                    val arrayList_details = HashMap<String, String>()
                    arrayList_details["title"] = (json_objectdetail.get("title").toString())
                    dataListRoadMap.add(arrayList_details)
                }

                val recyclerView = requireView().findViewById<RecyclerView>(R.id.upcomingRecyclerView)
                val upcomingChevron = requireView().findViewById<ImageView>(R.id.upcomingChevronImage)

                GlobalScope.launch(Dispatchers.Main) {
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = KnownIssuesAdapter(requireContext(), dataListRoadMap)

                    if (recyclerView.visibility == View.VISIBLE) {
                        recyclerView.visibility = View.GONE
                        upcomingChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }
                    else {
                        recyclerView.visibility = View.VISIBLE
                        upcomingChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                    }
                }

            }
        })
    }

    @DelicateCoroutinesApi
    fun runSwipe(url : String) {
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
                    alert.setMessage("There was an error fetching the latest news. Check your data connection.")
                    alert.setPositiveButton("OK") { _, _ ->

                    }
                    alert.show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                dataList.clear()
                var str_response = response.body()!!.string()
                //creating json object
                val json_contact:JSONObject = JSONObject(str_response)
                //creating json array
                var jsonarray_info: JSONArray = json_contact.getJSONArray("update")

                var size:Int = jsonarray_info.length()

                for (i in 0 until size) {
                    var json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)

                    var arrayList_details = HashMap<String, String>()
                    arrayList_details["date"] = json_objectdetail.get("date").toString()
                    arrayList_details["title"] = (json_objectdetail.get("title").toString())
                    arrayList_details["body"] = (json_objectdetail.get("body").toString())
                    dataList.add(arrayList_details)
                }

                val recyclerView = requireView().findViewById<RecyclerView>(R.id.updateRecyclerView)

                GlobalScope.launch(Dispatchers.Main) {
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = UpdateAdapter(requireContext(), dataList)
                }
            }
        })
    }
}