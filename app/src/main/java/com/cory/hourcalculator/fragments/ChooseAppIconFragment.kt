package com.cory.hourcalculator.fragments

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.AppIconAdapter
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.MenuTintData
import com.cory.hourcalculator.classes.Vibrate
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar

class ChooseAppIconFragment : Fragment() {

    private val appIconDataList = ArrayList<HashMap<String, String>>()

    private lateinit var gridLayoutManager: GridLayoutManager

    private lateinit var appIconAdapter: AppIconAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_app_icon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appIconToolBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarAppIconsFragment)

        appIconToolBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val collapsingToolbarLayout = activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAppIconFragment)
        if (AccentColor(requireContext()).loadAccent() == 5) {
            collapsingToolbarLayout?.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
            collapsingToolbarLayout?.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        }

        val navigationDrawable = appIconToolBar?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            if (AccentColor(requireContext()).loadAccent() == 5) {
                navigationDrawable?.colorFilter = BlendModeColorFilter(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                    BlendMode.SRC_ATOP
                )
            }
            else {
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
                val id = typedValue.resourceId
                navigationDrawable?.colorFilter = BlendModeColorFilter(
                    ContextCompat.getColor(requireContext(), id),
                    BlendMode.SRC_ATOP
                )
            }
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }

        gridLayoutManager = GridLayoutManager(requireContext(), 1)

        val appIconRecyclerView = activity?.findViewById<RecyclerView>(R.id.appIconRecyclerView)
        appIconRecyclerView?.layoutManager = gridLayoutManager
        val iconArray = arrayOf(R.drawable.hourcalclogoteal.toString(), R.drawable.hourcalclogopink.toString(),
                                R.drawable.hourcalclogoorange.toString(), R.drawable.hourcalclogored.toString(), R.drawable.hourcalclogoblue.toString(),
                                R.drawable.hourcalculatorlogoyellowgradient.toString(), R.drawable.hourcalclogo_christmas.toString(), R.drawable.hourcalclogoteal.toString())
        val iconNameArray = arrayOf("Teal", "Pink", "Orange", "Red", "Blue", "OG", "Snow Falling", "Material You")
        for (i in 0 until iconArray.count()) {
            val map = HashMap<String, String>()
            map["icon"] = iconArray.elementAt(i)
            map["name"] = iconNameArray.elementAt(i)
            appIconDataList.add(map)
        }
        appIconAdapter = AppIconAdapter(requireContext(), appIconDataList)

        appIconRecyclerView?.adapter = appIconAdapter
    }
}