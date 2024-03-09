package com.cory.hourcalculator.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.TypedValue
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.android.material.shape.CornerFamily
import com.google.android.material.snackbar.Snackbar
import java.math.RoundingMode


@Suppress("DEPRECATION")
class HistoryFragment : Fragment() {

    private var isLoaded = false

    private var output: String = ""
    private var outputColon: String = ""
    private var outputWages: String = ""
    private val dataList = ArrayList<HashMap<String, String>>()

    private lateinit var infoDialog: BottomSheetDialog
    private lateinit var sortDialog: BottomSheetDialog

    private lateinit var linearLayoutManager: GridLayoutManager

    private var containsColon = false

    private lateinit var customAdapter: CustomAdapter

    fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateCustomTheme()
        customAdapter.updateCardColor()

        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                //activity?.setTheme(R.style.Theme_DarkTheme)
            }

            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
            }

            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
            }

            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                    }

                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(
                            R.style.Theme_AMOLED
                        )
                    }

                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        try {
            if (infoDialog.isShowing) {
                infoDialog.dismiss()
                info()
            }
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }
        try {
            if (sortDialog.isShowing) {
                sortDialog.dismiss()
                sort()
            }
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }
    }

    fun info() {
        val wagesData = WagesData(requireContext())
        val dbHandler = DBHelper(requireContext(), null)

        if (dbHandler.getCount() > 0) {
        if (customAdapter.checkBoxVisible && ShowInfoForOnlySelectedItemsData(requireContext()).loadShowInfoForOnlySelectedItems()) {
            val cursor = dbHandler.getAllRow(requireContext())
            cursor!!.moveToFirst()

            output = ""
            var total = 0.0

                while (!cursor.isAfterLast) {
                    if (customAdapter.getSelectedItems().contains(cursor.position)) {
                        val map = HashMap<String, String>()
                        map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                        map["inTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                        map["outTime"] =
                            cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                        map["breakTime"] =
                            cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                        map["totalHours"] =
                            cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                        map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

                        total += map["totalHours"]!!.toDouble()

                    }
                    cursor.moveToNext()
            }
            output = total.toString()

        }

            calculateWages()
            infoDialog = BottomSheetDialog(requireContext())
            val infoLayout =
                layoutInflater.inflate(R.layout.info_bottom_sheet, null)
            infoDialog.setContentView(infoLayout)
            infoDialog.setCancelable(true)

            if (resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    infoDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }

            val totalHours = infoLayout.findViewById<TextView>(R.id.bodyTextView)
            val numberOfEntries = infoLayout.findViewById<TextView>(R.id.body1TextView)
            val wagesTextView = infoLayout.findViewById<TextView>(R.id.body2TextView)
            val okButton = infoLayout.findViewById<Button>(R.id.okButton)

            val totalHoursCardView =
                infoLayout.findViewById<MaterialCardView>(R.id.totalHoursCardView)
            val totalNumberOfEntriesCardView =
                infoLayout.findViewById<MaterialCardView>(R.id.totalNumberOfEntriesCardView)
            val wagesCardView =
                infoLayout.findViewById<MaterialCardView>(R.id.wagesCardView)

            totalHoursCardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            totalNumberOfEntriesCardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            wagesCardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(
                        CustomColorGenerator(requireContext()).generateCardColor()
                    )
                )
            )
            okButton.setBackgroundColor(
                Color.parseColor(
                    CustomColorGenerator(
                        requireContext()
                    ).generateCustomColorPrimary()
                )
            )
            totalHoursCardView.shapeAppearanceModel =
                totalHoursCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
            totalNumberOfEntriesCardView.shapeAppearanceModel =
                totalNumberOfEntriesCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
            wagesCardView.shapeAppearanceModel = wagesCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(28f)
                .setBottomLeftCornerSize(28f)
                .build()

            if (wagesData.loadWageAmount() != "") {
                try {
                    if (customAdapter.checkBoxVisible && ShowInfoForOnlySelectedItemsData(requireContext()).loadShowInfoForOnlySelectedItems()) {
                        totalHours.text = "Total Hours for Selected Items: $output/$outputColon"
                    }
                    else {
                        totalHours.text = "Total Hours: $output/$outputColon"
                    }
                    if (customAdapter.checkBoxVisible && ShowInfoForOnlySelectedItemsData(requireContext()).loadShowInfoForOnlySelectedItems()) {
                        numberOfEntries.text = "Number of Entries Selected: ${customAdapter.getSelectedCount()}"
                    }
                    else {
                        numberOfEntries.text = "Number of Entries: ${dbHandler.getCount()}"
                    }
                    if (CalculateOvertimeInHistoryData(requireContext()).loadCalculateOvertimeInHistoryState() && outputWages.toDouble() > 40) {
                        val overFourty = outputWages.toDouble() - 40
                        val timeHalf = overFourty * (wagesData.loadWageAmount().toString()
                            .toDouble() * OvertimeData(requireContext()).loadOverTimeAmount())
                        val wages =
                            (40 * wagesData.loadWageAmount().toString()
                                .toDouble()) + timeHalf
                        val wagesRounded = String.format("%,.2f", wages)
                        if (customAdapter.checkBoxVisible && ShowInfoForOnlySelectedItemsData(requireContext()).loadShowInfoForOnlySelectedItems()) {
                            wagesTextView.text = "Wages for Selected Items: $$wagesRounded"
                        }
                        else {
                            wagesTextView.text = "Wages: $$wagesRounded"
                        }
                    } else {
                        val wages =
                            outputWages.toDouble() * wagesData.loadWageAmount().toString()
                                .toDouble()
                        val wagesRounded = String.format("%,.2f", wages)
                        if (customAdapter.checkBoxVisible && ShowInfoForOnlySelectedItemsData(requireContext()).loadShowInfoForOnlySelectedItems()) {
                            wagesTextView.text = "Wages for Selected Items: $$wagesRounded"
                        }
                        else {
                            wagesTextView.text = "Wages: $$wagesRounded"
                        }
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    if (customAdapter.checkBoxVisible && ShowInfoForOnlySelectedItemsData(requireContext()).loadShowInfoForOnlySelectedItems()) {
                        totalHours.text = "Total Hours for Selected Items: $output/$outputColon"
                        numberOfEntries.text = "Number of Entries Selected: ${dbHandler.getCount()}"
                        wagesTextView.text = "Wages Selected: Error"
                    }
                    else {
                        totalHours.text = "Total Hours: $output/$outputColon"
                        numberOfEntries.text = "Number of Entries: ${dbHandler.getCount()}"
                        wagesTextView.text = "Wages: Error"
                    }
                }
            } else {
                totalHours.text = "Total Hours: $output/$outputColon"
                numberOfEntries.text = "Number of Entries: ${dbHandler.getCount()}"
                wagesCardView.visibility = View.GONE
                totalNumberOfEntriesCardView.shapeAppearanceModel =
                    totalNumberOfEntriesCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                        .setBottomRightCornerSize(28f)
                        .setBottomLeftCornerSize(28f)
                        .build()
            }
            /*if (resources.getBoolean(R.bool.isTablet)) {
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.isDraggable = false
        }*/
            okButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                infoDialog.dismiss()
            }
            infoDialog.show()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.cant_show_info_history_empty),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                //activity?.setTheme(R.style.Theme_DarkTheme)
            }

            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
            }

            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
            }

            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                    }

                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(
                            R.style.Theme_AMOLED
                        )
                    }

                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 1
            (activity as MainActivity).setActiveTab(1)
        }

        MainActivity().runOnUiThread(runnable)

        customAdapter = CustomAdapter(requireContext(), dataList)

        linearLayoutManager = if (resources.getBoolean(R.bool.isTablet)) {
            GridLayoutManager(requireContext(), 2)
        } else {
            GridLayoutManager(requireContext(), 1)
        }

        val listView = activity?.findViewById<RecyclerView>(R.id.listView)
        if (AnimationData(requireContext()).loadHistoryRecyclerViewLoadingAnimation()) {
            val animation =
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.listview_animation)
            listView?.layoutAnimation = animation
        }

        updateCustomTheme()

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)

        topAppBar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.options -> {
                    Vibrate().vibration(requireContext())
                    val historyOptionsDialog = BottomSheetDialog(requireContext())
                    val historyOptions =
                        layoutInflater.inflate(R.layout.history_options_bottom_sheet, null)
                    historyOptionsDialog.setContentView(historyOptions)

                    if (resources.getBoolean(R.bool.isTablet)) {
                        val bottomSheet =
                            historyOptionsDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheetBehavior.skipCollapsed = true
                        bottomSheetBehavior.isHideable = false
                        bottomSheetBehavior.isDraggable = false
                    }

                    val exportCardView =
                        historyOptions.findViewById<MaterialCardView>(R.id.exportCardView)
                    val deleteSelectedCardView =
                        historyOptions.findViewById<MaterialCardView>(R.id.deleteSelectedCardView)

                    if (TimeCardsToggleData(requireContext()).loadTimeCardsState()) {
                        exportCardView.setCardBackgroundColor(
                            ColorStateList.valueOf(
                                Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                            )
                        )
                        deleteSelectedCardView.setCardBackgroundColor(
                            ColorStateList.valueOf(
                                Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                            )
                        )

                        exportCardView.shapeAppearanceModel = exportCardView.shapeAppearanceModel
                            .toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                            .setBottomRightCornerSize(0f)
                            .setBottomLeftCornerSize(0f)
                            .build()
                        deleteSelectedCardView.shapeAppearanceModel =
                            deleteSelectedCardView.shapeAppearanceModel
                                .toBuilder()
                                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                .setBottomRightCornerSize(28f)
                                .setBottomLeftCornerSize(28f)
                                .build()
                    }
                    else {
                        exportCardView.visibility = View.GONE

                        val param = (deleteSelectedCardView.layoutParams as ViewGroup.MarginLayoutParams).apply {
                            setMargins(0, dpToPx(requireContext(), 15).toInt(),0,0)
                        }
                        deleteSelectedCardView.layoutParams = param

                        deleteSelectedCardView.setCardBackgroundColor(
                            ColorStateList.valueOf(
                                Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                            )
                        )

                        deleteSelectedCardView.shapeAppearanceModel =
                            deleteSelectedCardView.shapeAppearanceModel
                                .toBuilder()
                                .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                                .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                                .setBottomRightCornerSize(28f)
                                .setBottomLeftCornerSize(28f)
                                .build()
                    }

                    exportCardView.setOnClickListener {
                        Vibrate().vibration(requireContext())

                        if (customAdapter.getSelectedCount() <= 50) {
                            topAppBar.navigationIcon = null
                            customAdapter.snackbarDeleteSelected.dismiss()
                            customAdapter.snackbarDismissCheckBox.dismiss()
                            customAdapter.exportSelected()
                            historyOptionsDialog.dismiss()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Can't export more than 50 entries at a time",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    deleteSelectedCardView.setOnClickListener {
                        Vibrate().vibration(requireContext())
                        customAdapter.checkBoxVisible = false
                        try {
                            topAppBar.navigationIcon = null
                            customAdapter.snackbarDeleteSelected.dismiss()
                            customAdapter.snackbarDismissCheckBox.dismiss()
                            customAdapter.deleteSelected(requireView())
                            historyOptionsDialog.dismiss()
                        } catch (e: UninitializedPropertyAccessException) {
                            e.printStackTrace()
                        }
                    }
                    if (customAdapter.getSelectedCount() > 0) {
                        historyOptionsDialog.show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "There is nothing selected",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    true
                }

                R.id.info -> {
                    Vibrate().vibration(requireContext())
                    info()
                    true
                }

                R.id.mnuSort -> {
                    Vibrate().vibration(requireContext())
                    sort()
                    true
                }

                else -> false
            }
        }

        loadIntoList()

        topAppBar?.navigationIcon = null

        val floatingActionButtonHistory =
            requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButtonHistory)

        val params = CoordinatorLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(30, 0, 30, 30)

        if (HistoryFABPositioning(requireContext()).loadFABPosition() == 0) {
            params.gravity = Gravity.BOTTOM or Gravity.START
        } else if (HistoryFABPositioning(requireContext()).loadFABPosition() == 1) {
            params.gravity = Gravity.BOTTOM or Gravity.CENTER
        } else if (HistoryFABPositioning(requireContext()).loadFABPosition() == 2) {
            params.gravity = Gravity.BOTTOM or Gravity.END
        }

        floatingActionButtonHistory.layoutParams = params

        listView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()

                if (pastVisibleItems > 0) {
                    floatingActionButtonHistory?.show()
                } else {
                    floatingActionButtonHistory?.hide()
                }
            }
        })

        floatingActionButtonHistory?.setOnClickListener {
            scrollToTop()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.supportFragmentManager?.popBackStack()
                }
            })
    }

    private fun sort() {
        val dbHandler = DBHelper(requireContext(), null)
        val sortData = SortData(requireContext())
        val topAppBar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarHistory)
        val listView = requireActivity().findViewById<RecyclerView>(R.id.listView)
        if (dbHandler.getCount() == 0) {
            Toast.makeText(
                requireContext(),
                getString(R.string.cant_sort_history_is_empty),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            var selectedItem = -1

            when {
                sortData.loadSortState() == getString(R.string.day_desc) -> {
                    selectedItem = 0
                }

                sortData.loadSortState() == getString(R.string.day_asc) -> {
                    selectedItem = 1
                }

                sortData.loadSortState() == getString(R.string.total_desc) -> {
                    selectedItem = 2
                }

                sortData.loadSortState() == getString(R.string.total_asc) -> {
                    selectedItem = 3
                }
            }

            sortDialog = BottomSheetDialog(requireContext())
            val sortingLayout =
                layoutInflater.inflate(R.layout.sorting_bottom_sheet, null)
            sortDialog.setContentView(sortingLayout)
            sortDialog.setCancelable(true)

            if (resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    sortDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }

            val dateDescendingCardView =
                sortingLayout.findViewById<MaterialCardView>(R.id.dateDescendingCardView)
            val dateAscendingCardView =
                sortingLayout.findViewById<MaterialCardView>(R.id.dateAscendingCardView)
            val totalDescendingCardView =
                sortingLayout.findViewById<MaterialCardView>(R.id.totalDescendingCardView)
            val totalAscendingCardView =
                sortingLayout.findViewById<MaterialCardView>(R.id.totalAscendingCardView)

            totalAscendingCardView.visibility = View.VISIBLE
            totalDescendingCardView.visibility = View.VISIBLE
            dateDescendingCardView.shapeAppearanceModel =
                dateDescendingCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
            dateAscendingCardView.shapeAppearanceModel =
                dateAscendingCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
            totalDescendingCardView.shapeAppearanceModel =
                totalDescendingCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
            totalAscendingCardView.shapeAppearanceModel =
                totalAscendingCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(28f)
                    .setBottomLeftCornerSize(28f)
                    .build()

            val dateDescending =
                sortingLayout.findViewById<RadioButton>(R.id.dateDescending)
            val dateAscending =
                sortingLayout.findViewById<RadioButton>(R.id.dateAscending)
            val totalDescending =
                sortingLayout.findViewById<RadioButton>(R.id.totalDescending)
            val totalAscending =
                sortingLayout.findViewById<RadioButton>(R.id.totalAscending)

            dateDescendingCardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            dateAscendingCardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            totalDescendingCardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            totalAscendingCardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            val states = arrayOf(
                intArrayOf(-android.R.attr.state_checked), // unchecked
                intArrayOf(android.R.attr.state_checked)  // checked
            )

            val colors = intArrayOf(
                Color.parseColor("#000000"),
                Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
            )

            dateDescending.buttonTintList = ColorStateList(states, colors)
            dateAscending.buttonTintList = ColorStateList(states, colors)
            totalDescending.buttonTintList = ColorStateList(states, colors)
            totalAscending.buttonTintList = ColorStateList(states, colors)

            when (selectedItem) {
                0 -> {
                    dateDescending.isChecked = true
                }

                1 -> {
                    dateAscending.isChecked = true
                }

                2 -> {
                    totalDescending.isChecked = true
                }

                3 -> {
                    totalAscending.isChecked = true
                }
            }

            val collapsingToolbarLayout =
                requireView().findViewById<AppBarLayout>(R.id.appBarLayoutHistory)

            dateDescendingCardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                sortDialog.dismiss()
                listView?.scrollToPosition(0)
                collapsingToolbarLayout.setExpanded(true, true)
                sortData.setSortState(getString(R.string.day_desc))
                loadIntoList()
                customAdapter.checkBoxVisible = false
                try {
                    topAppBar.navigationIcon = null
                    hideOptionsIcon()
                    customAdapter.snackbarDeleteSelected.dismiss()
                    customAdapter.snackbarDismissCheckBox.dismiss()
                } catch (e: UninitializedPropertyAccessException) {
                    e.printStackTrace()
                }
                requireActivity().findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                    0,
                    dataList.size
                )
                Toast.makeText(
                    requireContext(),
                    getString(R.string.changed_sort_mode_last_entered),
                    Toast.LENGTH_SHORT
                ).show()
            }

            dateAscendingCardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                sortDialog.dismiss()
                listView?.scrollToPosition(0)
                collapsingToolbarLayout.setExpanded(true, true)
                sortData.setSortState(getString(R.string.day_asc))
                loadIntoList()
                customAdapter.checkBoxVisible = false
                try {
                    topAppBar.navigationIcon = null
                    hideOptionsIcon()
                    customAdapter.snackbarDeleteSelected.dismiss()
                    customAdapter.snackbarDismissCheckBox.dismiss()
                } catch (e: UninitializedPropertyAccessException) {
                    e.printStackTrace()
                }
                requireActivity().findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                    0,
                    dataList.size
                )
                Toast.makeText(
                    requireContext(),
                    getString(R.string.changed_sort_mode_first_entered),
                    Toast.LENGTH_SHORT
                ).show()
            }

            totalDescendingCardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                sortDialog.dismiss()
                listView?.scrollToPosition(0)
                collapsingToolbarLayout.setExpanded(true, true)
                sortData.setSortState(getString(R.string.total_desc))
                loadIntoList()
                customAdapter.checkBoxVisible = false
                try {
                    topAppBar.navigationIcon = null
                    hideOptionsIcon()
                    customAdapter.snackbarDeleteSelected.dismiss()
                    customAdapter.snackbarDismissCheckBox.dismiss()
                } catch (e: UninitializedPropertyAccessException) {
                    e.printStackTrace()
                }
                requireActivity().findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                    0,
                    dataList.size
                )
                Toast.makeText(
                    requireContext(),
                    getString(R.string.changed_sort_mode_most_entered),
                    Toast.LENGTH_SHORT
                ).show()
            }

            totalAscendingCardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                sortDialog.dismiss()
                listView?.scrollToPosition(0)
                collapsingToolbarLayout.setExpanded(true, true)
                sortData.setSortState(getString(R.string.total_asc))
                loadIntoList()
                customAdapter.checkBoxVisible = false
                try {
                    topAppBar.navigationIcon = null
                    hideOptionsIcon()
                    customAdapter.snackbarDeleteSelected.dismiss()
                    customAdapter.snackbarDismissCheckBox.dismiss()
                } catch (e: UninitializedPropertyAccessException) {
                    e.printStackTrace()
                }
                requireActivity().findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                    0,
                    dataList.size
                )
                Toast.makeText(
                    requireContext(),
                    getString(R.string.changed_sort_mode_least_entered),
                    Toast.LENGTH_SHORT
                ).show()
            }
            sortDialog.show()
        }
    }

    private fun updateCustomTheme() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.historyCoordinatorLayout)
            .setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val floatingActionButtonHistory =
            activity?.findViewById<FloatingActionButton>(R.id.floatingActionButtonHistory)

        floatingActionButtonHistory?.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateBottomNavBackgroundColor()))
        floatingActionButtonHistory?.imageTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()))

        val collapsingToolbarLayout =
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutHistory)

        collapsingToolbarLayout?.setExpandedTitleColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTitleBarExpandedTextColor()
            )
        )

        collapsingToolbarLayout?.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutHistory)
                ?.setCollapsedTitleTextColor(
                    Color.parseColor(
                        CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()
                    )
                )
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutHistory)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)

        val infoDrawable = topAppBar?.menu?.findItem(R.id.info)?.icon
        infoDrawable?.mutate()
        val sortDrawable = topAppBar?.menu?.findItem(R.id.mnuSort)?.icon
        sortDrawable?.mutate()
        val optionsDrawable = topAppBar?.menu?.findItem(R.id.options)?.icon
        optionsDrawable?.mutate()

        val navigationDrawable = topAppBar?.navigationIcon
        navigationDrawable?.mutate()

        if (Build.VERSION.SDK_INT >= 29) {
            try {
                if (MenuTintData(requireContext()).loadMenuTint()) {
                    infoDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                        BlendMode.SRC_ATOP
                    )
                    sortDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                        BlendMode.SRC_ATOP
                    )
                    optionsDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                        BlendMode.SRC_ATOP
                    )
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                        BlendMode.SRC_ATOP
                    )
                } else {
                    val typedValue = TypedValue()
                    activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                    val id = typedValue.resourceId
                    infoDrawable?.colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), id),
                        BlendMode.SRC_ATOP
                    )
                    sortDrawable?.colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), id),
                        BlendMode.SRC_ATOP
                    )
                    optionsDrawable?.colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), id),
                        BlendMode.SRC_ATOP
                    )
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), id),
                        BlendMode.SRC_ATOP
                    )
                }
            } catch (e: NoClassDefFoundError) {
                e.printStackTrace()
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                val id = typedValue.resourceId
                infoDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
                sortDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
                navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
                optionsDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            }
        } else {

            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            infoDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            sortDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            optionsDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun loadIntoList() {

        try {
            val dbHandler = DBHelper(requireContext(), null)

            textViewVisibility()

            dataList.clear()
            val cursor = dbHandler.getAllRow(requireContext())
            cursor!!.moveToFirst()

            while (!cursor.isAfterLast) {
                val map = HashMap<String, String>()
                map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                map["inTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                map["outTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                map["breakTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                map["totalHours"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))
                dataList.add(map)

                cursor.moveToNext()

            }

            calculateWages()

            val listView = activity?.findViewById<RecyclerView>(R.id.listView)
            listView?.layoutManager = linearLayoutManager
            listView?.adapter = customAdapter

        } catch (e: Exception) {
            e.printStackTrace()
            val snackBar =
                Snackbar.make(requireView(), "Error loading history", Snackbar.LENGTH_LONG)
            snackBar.duration = Snackbar.LENGTH_INDEFINITE

            snackBar.setAction("Delete") {
                Vibrate().vibration(requireContext())
                snackBar.dismiss()
                DBHelper(requireActivity().applicationContext, null).deleteAll()
                loadIntoList()
            }

            snackBar.setActionTextColor(
                Color.parseColor(CustomColorGenerator(requireContext()).generateSnackbarActionTextColor())
            )
            snackBar.apply {
                snackBar.view.background = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.snackbar_corners,
                    context.theme
                )
            }
            snackBar.show()
        }
    }

    fun undo() {
        val listView = activity?.findViewById<RecyclerView>(R.id.listView)

        containsColon = false

        val dbHandler = DBHelper(requireContext(), null)
        val undoHoursData = UndoHoursData(requireContext())

        dbHandler.insertRow(
            undoHoursData.loadInTime(),
            undoHoursData.loadOutTime(),
            undoHoursData.loadTotalHours(),
            undoHoursData.loadDate(),
            undoHoursData.loadBreakTime()
        )

        dataList.clear()
        val cursor2 = dbHandler.getAllRow(requireContext())
        cursor2!!.moveToFirst()

        while (!cursor2.isAfterLast) {
            val map2 = HashMap<String, String>()
            map2["id"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_ID))
            map2["inTime"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_IN))
            map2["outTime"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_OUT))
            map2["breakTime"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_BREAK))
            map2["totalHours"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_TOTAL))
            map2["date"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_DAY))
            dataList.add(map2)

            cursor2.moveToNext()

        }

        listView?.adapter?.notifyItemInserted(ItemPositionData(requireContext()).loadPosition())
        listView?.adapter?.notifyItemRangeChanged(0, dataList.count())
        //customAdapter.updateCornerRadius()
    }

    private lateinit var recyclerViewState: Parcelable

    fun saveState() {

        val recyclerView = view?.findViewById<RecyclerView>(R.id.listView)

        recyclerViewState = recyclerView?.layoutManager?.onSaveInstanceState()!!

        textViewVisibility()

        calculateWages()
    }

    @SuppressLint("Range")
    private fun calculateWages() {
        outputWages = ""
        val dbHandler = DBHelper(requireContext(), null)

        var y = 0.0

        val cursor = dbHandler.getAllRow(requireContext())
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {

            if (customAdapter.checkBoxVisible && ShowInfoForOnlySelectedItemsData(requireContext()).loadShowInfoForOnlySelectedItems()) {
                if (customAdapter.getSelectedItems().contains(cursor.position)) {
                    val array =
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)).toString()

                    var decimalTime: Double
                    if (array.contains(":")) {
                        val (hours, minutes) = array.split(":")
                        val decimal =
                            (minutes.toDouble() / 60).toBigDecimal()
                                .setScale(2, RoundingMode.HALF_EVEN)
                                .toString().drop(2)
                        try {
                            decimalTime = "$hours.$decimal".toDouble()
                            y += decimalTime

                        } catch (e: java.lang.NumberFormatException) {
                            e.printStackTrace()
                        }

                        containsColon = true

                    } else {
                        y += array.toDouble()
                    }
                }
            }
            else {
                val array =
                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)).toString()

                var decimalTime: Double
                if (array.contains(":")) {
                    val (hours, minutes) = array.split(":")
                    val decimal =
                        (minutes.toDouble() / 60).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
                            .toString().drop(2)
                    try {
                        decimalTime = "$hours.$decimal".toDouble()
                        y += decimalTime

                    } catch (e: java.lang.NumberFormatException) {
                        e.printStackTrace()
                    }

                    containsColon = true

                } else {
                    y += array.toDouble()
                }
            }

                output = String.format("%.2f", y)
                outputWages = output
            cursor.moveToNext()

        }

        if (dataList.isNotEmpty()) {
            val (wholeNumber, decimal) = output.split(".")
            var minute = (".$decimal".toDouble() * 60).toInt().toString()
            if (minute.length == 1) {
                minute = "0$minute"
            }

            outputColon = "$wholeNumber:$minute"
        }
    }

    private fun textViewVisibility() {
        val dbHandler = DBHelper(requireActivity().applicationContext, null)

        if (dbHandler.getCount() > 0) {
            val noHoursStoredTextView = activity?.findViewById<TextView>(R.id.noHoursStoredTextView)
            noHoursStoredTextView?.visibility = View.GONE
        } else {
            val noHoursStoredTextView = activity?.findViewById<TextView>(R.id.noHoursStoredTextView)
            noHoursStoredTextView?.visibility = View.VISIBLE
        }
    }

    fun restoreState() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.listView)
        recyclerView?.layoutManager?.onRestoreInstanceState(recyclerViewState)

        textViewVisibility()

        calculateWages()
    }

    fun deleteAll() {
        val animation = AlphaAnimation(1f, 0f)
        animation.duration = 500
        val listView = view?.findViewById<RecyclerView>(R.id.listView)
        val floatingActionButtonHistory =
            view?.findViewById<FloatingActionButton>(R.id.floatingActionButtonHistory)

        floatingActionButtonHistory?.visibility = View.INVISIBLE

        listView?.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            loadIntoList()
        }, 500)

        //requireActivity().findViewById<AppBarLayout>(R.id.appBarLayoutHistory).setExpanded(true, true)

        saveState()

        hideNavigationIcon()
        hideOptionsIcon()

    }

    fun checkBoxVisible(visible: Boolean) {

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)

        if (visible) {
            topAppBar?.navigationIcon = ResourcesCompat.getDrawable(
                requireContext().resources,
                R.drawable.ic_baseline_close_24,
                requireContext().theme
            )

            val navigationDrawable = topAppBar?.navigationIcon
            navigationDrawable?.mutate()

            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    if (MenuTintData(requireContext()).loadMenuTint()) {
                        navigationDrawable?.colorFilter = BlendModeColorFilter(
                            Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                            BlendMode.SRC_ATOP
                        )
                    } else {
                        val typedValue = TypedValue()
                        activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                        val id = typedValue.resourceId
                        navigationDrawable?.colorFilter = BlendModeColorFilter(
                            ContextCompat.getColor(requireContext(), id),
                            BlendMode.SRC_ATOP
                        )
                    }
                } catch (e: NoClassDefFoundError) {
                    e.printStackTrace()
                    val typedValue = TypedValue()
                    activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                    val id = typedValue.resourceId
                    navigationDrawable?.setColorFilter(
                        ContextCompat.getColor(requireContext(), id),
                        PorterDuff.Mode.SRC_ATOP
                    )
                }
            }
            showOptionsIcon()
        } else {
            topAppBar?.navigationIcon = null
            hideOptionsIcon()
        }

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            customAdapter.checkboxVisible()
            topAppBar.navigationIcon = null
            hideOptionsIcon()
        }
    }

    fun hideNavigationIcon() {
        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)
        topAppBar?.navigationIcon = null
        hideOptionsIcon()
    }

    private fun showOptionsIcon() {
        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)
        topAppBar?.menu?.findItem(R.id.options)?.isVisible = true
    }

    private fun hideOptionsIcon() {
        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)
        topAppBar?.menu?.findItem(R.id.options)?.isVisible = false
    }

    fun scrollToTop() {
        val listView = view?.findViewById<RecyclerView>(R.id.listView)
        Vibrate().vibration(requireContext())

        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 0) {

            val savedState = listView?.layoutManager?.onSaveInstanceState()
            listView?.scrollToPosition(0)
            val collapsingToolbarLayout =
                requireView().findViewById<AppBarLayout>(R.id.appBarLayoutHistory)
            collapsingToolbarLayout.setExpanded(true, true)
            val snackbar =
                Snackbar.make(
                    requireView(),
                    getString(R.string.restore_position),
                    Snackbar.LENGTH_LONG
                )
                    .setDuration(5000)

            snackbar.setAction(getString(R.string.restore)) {
                Vibrate().vibration(requireContext())

                listView?.layoutManager?.onRestoreInstanceState(savedState)
                collapsingToolbarLayout.setExpanded(false, false)

            }

            snackbar.setActionTextColor(
                Color.parseColor(CustomColorGenerator(requireContext()).generateSnackbarActionTextColor())
            )

            snackbar.apply {
                snackbar.view.background = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.snackbar_corners,
                    context.theme
                )
            }
            if (!customAdapter.isCheckBoxVisible()) {
                snackbar.show()
            }
        }
    }

    fun undoDeleteAll() {
        val animation = AlphaAnimation(0f, 1f)
        animation.duration = 500
        val listView = view?.findViewById<RecyclerView>(R.id.listView)

        listView?.alpha = 0f
        loadIntoList()
        listView?.startAnimation(animation)
        listView?.alpha = 1f

        restoreState()

    }
}