package com.cory.hourcalculator.fragments

import android.R.color
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.TypedValue
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.intents.MainActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.CornerFamily
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import java.math.RoundingMode


@DelicateCoroutinesApi
class HistoryFragment : Fragment() {

    private var output: String = ""
    private var outputColon: String = ""
    private var outputWages: String = ""
    private val dataList = ArrayList<HashMap<String, String>>()

    private lateinit var linearLayoutManager: LinearLayoutManager

    private var containsColon = false

    var themeSelection = false

    private lateinit var customAdapter: CustomAdapter

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
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 1
            //(activity as MainActivity).setActiveTab(1)
        }

        MainActivity().runOnUiThread(runnable)

        customAdapter = CustomAdapter(requireContext(), dataList)

        linearLayoutManager = LinearLayoutManager(requireContext())

        activity?.window?.setBackgroundDrawable(null)

        val listView = activity?.findViewById<RecyclerView>(R.id.listView)
        val animation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.listview_animation)
        listView?.layoutAnimation = animation

        val inputManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)

        val accentColor = AccentColor(requireContext())
        val floatingActionButtonHistory =
            activity?.findViewById<FloatingActionButton>(R.id.floatingActionButtonHistory)

        floatingActionButtonHistory?.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateBottomNavBackgroundColor()))
        floatingActionButtonHistory?.imageTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()))

        val sortData = SortData(requireContext())
        val dialog = BottomSheetDialog(requireContext())

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)

        val collapsingToolbarLayout =
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutHistory)

            collapsingToolbarLayout?.setContentScrimColor(
                Color.parseColor(
                    CustomColorGenerator(
                        requireContext()
                    ).generateTopAppBarColor()
                )
            )
            collapsingToolbarLayout?.setStatusBarScrimColor(
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


        val infoDrawable = topAppBar?.menu?.findItem(R.id.info)?.icon
        infoDrawable?.mutate()
        val sortDrawable = topAppBar?.menu?.findItem(R.id.mnuSort)?.icon
        sortDrawable?.mutate()
        val optionsDrawable = topAppBar?.menu?.findItem(R.id.options)?.icon
        optionsDrawable?.mutate()

        val navigationDrawable = topAppBar?.navigationIcon
        navigationDrawable?.mutate()

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

        topAppBar?.setOnMenuItemClickListener { menuItem ->
            val dbHandler = DBHelper(requireContext(), null)
            when (menuItem.itemId) {
                R.id.options -> {
                    Vibrate().vibration(requireContext())
                    val historyOptionsDialog = BottomSheetDialog(requireContext())
                    val historyOptions =
                        layoutInflater.inflate(R.layout.history_options_bottom_sheet, null)

                    historyOptionsDialog.setContentView(historyOptions)
                    //val exportSelected = historyOptions.findViewById<ConstraintLayout>(R.id.exportSelectedConstraint)
                    //val deleteSelected = historyOptions.findViewById<ConstraintLayout>(R.id.deleteSelectedConstraint)

                    val exportCardView =
                        historyOptions.findViewById<MaterialCardView>(R.id.exportCardView)
                    val deleteSelectedCardView =
                        historyOptions.findViewById<MaterialCardView>(R.id.deleteSelectedCardView)

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

                    exportCardView.setOnClickListener {
                        Vibrate().vibration(requireContext())
                        //Toast.makeText(requireContext(), "Cant do this right now", Toast.LENGTH_SHORT).show()
                        if (HistoryToggleData(requireContext()).loadHistoryState()) {
                            topAppBar.navigationIcon = null
                            customAdapter.snackbarDeleteSelected.dismiss()
                            customAdapter.snackbarDismissCheckBox.dismiss()
                            customAdapter.exportSelected()
                            historyOptionsDialog.dismiss()
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
                    val wagesData = WagesData(requireContext())

                    if (dbHandler.getCount() > 0) {
                        val infoLayout =
                            layoutInflater.inflate(R.layout.info_bottom_sheet, null)
                        dialog.setContentView(infoLayout)
                        dialog.setCancelable(true)
                        dialog.window?.navigationBarColor =
                            ContextCompat.getColor(requireContext(), R.color.black)

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
                                val wages =
                                    outputWages.toDouble() * wagesData.loadWageAmount().toString()
                                        .toDouble()
                                val wagesRounded = String.format("%.2f", wages)
                                totalHours.text = "Total Hours: $output/$outputColon"
                                numberOfEntries.text = "Number of Entries: ${dbHandler.getCount()}"
                                wagesTextView.text = "Wages: $$wagesRounded"
                            } catch (e: NumberFormatException) {
                                e.printStackTrace()
                                totalHours.text = "Total Hours: $output/$outputColon"
                                numberOfEntries.text = "Number of Entries: ${dbHandler.getCount()}"
                                wagesTextView.text = "Wages: Error"
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
                            dialog.dismiss()
                        }
                        dialog.show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.cant_show_info_history_empty),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    true
                }
                R.id.mnuSort -> {
                    Vibrate().vibration(requireContext())
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

                        /* val listItems: Array<String> = if (containsColon) {
                            if ((sortData.loadSortState() == getString(R.string.total_desc) || sortData.loadSortState() == getString(
                                    R.string.total_asc
                                )) && containsColon
                            ) {
                                selectedItem = 0
                                sortData.setSortState(getString(R.string.day_desc))
                            }
                            arrayOf(
                                getString(R.string.sort_by_last_entered),
                                getString(R.string.sort_by_first_entered)
                            )
                        } else {
                            arrayOf(
                                getString(R.string.sort_by_last_entered),
                                getString(R.string.sort_by_first_entered),
                                getString(R.string.sort_by_most_entered),
                                getString(R.string.sort_by_least_entered)
                            )
                        }

                        val collapsingToolbarLayout =
                            requireView().findViewById<AppBarLayout>(R.id.appBarLayoutHistory)

                        val alert = MaterialAlertDialogBuilder(
                            requireContext(),
                            AccentColor(requireContext()).alertTheme()
                        )
                        alert.setTitle(getString(R.string.sorting_method))
                        alert.setSingleChoiceItems(
                            listItems,
                            selectedItem
                        ) { dialog, i ->
                            Vibrate().vibration(requireContext())
                            when (i) {
                                0 -> {
                                    listView?.scrollToPosition(0)
                                    collapsingToolbarLayout.setExpanded(true, true)
                                    sortData.setSortState(getString(R.string.day_desc))
                                    changeSortMethod()
                                    customAdapter.checkBoxVisible = false
                                    try {
                                        topAppBar.navigationIcon = null
                                        customAdapter.snackbarDeleteSelected.dismiss()
                                        customAdapter.snackbarDismissCheckBox.dismiss()
                                    } catch (e: UninitializedPropertyAccessException) {
                                        e.printStackTrace()
                                    }
                                    view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                                        0,
                                        dataList.size
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.changed_sort_mode_last_entered),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                1 -> {
                                    listView?.scrollToPosition(0)
                                    collapsingToolbarLayout.setExpanded(true, true)
                                    sortData.setSortState(getString(R.string.day_asc))
                                    changeSortMethod()
                                    customAdapter.checkBoxVisible = false
                                    try {
                                        topAppBar.navigationIcon = null
                                        customAdapter.snackbarDeleteSelected.dismiss()
                                        customAdapter.snackbarDismissCheckBox.dismiss()
                                    } catch (e: UninitializedPropertyAccessException) {
                                        e.printStackTrace()
                                    }
                                    view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                                        0,
                                        dataList.size
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.changed_sort_mode_first_entered),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                2 -> {
                                    listView?.scrollToPosition(0)
                                    collapsingToolbarLayout.setExpanded(true, true)
                                    sortData.setSortState(getString(R.string.total_desc))
                                    changeSortMethod()
                                    customAdapter.checkBoxVisible = false
                                    try {
                                        topAppBar.navigationIcon = null
                                        customAdapter.snackbarDeleteSelected.dismiss()
                                        customAdapter.snackbarDismissCheckBox.dismiss()
                                    } catch (e: UninitializedPropertyAccessException) {
                                        e.printStackTrace()
                                    }
                                    view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                                        0,
                                        dataList.size
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.changed_sort_mode_most_entered),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                3 -> {
                                    listView?.scrollToPosition(0)
                                    collapsingToolbarLayout.setExpanded(true, true)
                                    sortData.setSortState(getString(R.string.total_asc))
                                    changeSortMethod()
                                    customAdapter.checkBoxVisible = false
                                    try {
                                        topAppBar.navigationIcon = null
                                        customAdapter.snackbarDeleteSelected.dismiss()
                                        customAdapter.snackbarDismissCheckBox.dismiss()
                                    } catch (e: UninitializedPropertyAccessException) {
                                        e.printStackTrace()
                                    }
                                    view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                                        0,
                                        dataList.size
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.changed_sort_mode_least_entered),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            dialog.dismiss()
                        }
                        alert.show()
                    }*/
                        val sortingLayout =
                            layoutInflater.inflate(R.layout.sorting_bottom_sheet, null)
                        dialog.setContentView(sortingLayout)
                        dialog.setCancelable(true)

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

                        dialog.window?.navigationBarColor =
                            ContextCompat.getColor(requireContext(), R.color.black)
                        /*if (resources.getBoolean(R.bool.isTablet)) {
                        val bottomSheet =
                            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheetBehavior.skipCollapsed = true
                        bottomSheetBehavior.isHideable = false
                        bottomSheetBehavior.isDraggable = false
                    }*/

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

                        if (selectedItem == 0) {
                            dateDescending.isChecked = true
                        } else if (selectedItem == 1) {
                            dateAscending.isChecked = true
                        } else if (selectedItem == 2) {
                            totalDescending.isChecked = true
                        } else if (selectedItem == 3) {
                            totalAscending.isChecked = true
                        }

                        val collapsingToolbarLayout =
                            requireView().findViewById<AppBarLayout>(R.id.appBarLayoutHistory)

                        dateDescending.setOnClickListener {
                            Vibrate().vibration(requireContext())
                            dialog.dismiss()
                            listView?.scrollToPosition(0)
                            collapsingToolbarLayout.setExpanded(true, true)
                            sortData.setSortState(getString(R.string.day_desc))
                            changeSortMethod()
                            customAdapter.checkBoxVisible = false
                            try {
                                topAppBar.navigationIcon = null
                                customAdapter.snackbarDeleteSelected.dismiss()
                                customAdapter.snackbarDismissCheckBox.dismiss()
                            } catch (e: UninitializedPropertyAccessException) {
                                e.printStackTrace()
                            }
                            view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                                0,
                                dataList.size
                            )
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.changed_sort_mode_last_entered),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        dateAscending.setOnClickListener {
                            Vibrate().vibration(requireContext())
                            dialog.dismiss()
                            listView?.scrollToPosition(0)
                            collapsingToolbarLayout.setExpanded(true, true)
                            sortData.setSortState(getString(R.string.day_asc))
                            changeSortMethod()
                            customAdapter.checkBoxVisible = false
                            try {
                                topAppBar.navigationIcon = null
                                customAdapter.snackbarDeleteSelected.dismiss()
                                customAdapter.snackbarDismissCheckBox.dismiss()
                            } catch (e: UninitializedPropertyAccessException) {
                                e.printStackTrace()
                            }
                            view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                                0,
                                dataList.size
                            )
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.changed_sort_mode_first_entered),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        totalDescending.setOnClickListener {
                            Vibrate().vibration(requireContext())
                            dialog.dismiss()
                            listView?.scrollToPosition(0)
                            collapsingToolbarLayout.setExpanded(true, true)
                            sortData.setSortState(getString(R.string.total_desc))
                            changeSortMethod()
                            customAdapter.checkBoxVisible = false
                            try {
                                topAppBar.navigationIcon = null
                                customAdapter.snackbarDeleteSelected.dismiss()
                                customAdapter.snackbarDismissCheckBox.dismiss()
                            } catch (e: UninitializedPropertyAccessException) {
                                e.printStackTrace()
                            }
                            view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                                0,
                                dataList.size
                            )
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.changed_sort_mode_most_entered),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        totalAscending.setOnClickListener {
                            Vibrate().vibration(requireContext())
                            dialog.dismiss()
                            listView?.scrollToPosition(0)
                            collapsingToolbarLayout.setExpanded(true, true)
                            sortData.setSortState(getString(R.string.total_asc))
                            changeSortMethod()
                            customAdapter.checkBoxVisible = false
                            try {
                                topAppBar.navigationIcon = null
                                customAdapter.snackbarDeleteSelected.dismiss()
                                customAdapter.snackbarDismissCheckBox.dismiss()
                            } catch (e: UninitializedPropertyAccessException) {
                                e.printStackTrace()
                            }
                            view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                                0,
                                dataList.size
                            )
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.changed_sort_mode_least_entered),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        dialog.show()
                    }
                    true
                }
                else -> false
            }
        }

        loadIntoList()

        if (customAdapter.isCheckBoxVisible()) {
            topAppBar?.navigationIcon = ResourcesCompat.getDrawable(
                requireContext().resources,
                R.drawable.ic_baseline_close_24,
                requireContext().theme
            )
            val xIconDrawable = topAppBar?.navigationIcon
            xIconDrawable?.mutate()

            if (MenuTintData(requireContext()).loadMenuTint()) {
                    xIconDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                        BlendMode.SRC_ATOP
                    )
            } else {
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                val id = typedValue.resourceId
                xIconDrawable?.colorFilter = BlendModeColorFilter(
                    ContextCompat.getColor(requireContext(), id),
                    BlendMode.SRC_ATOP
                )
            }
        } else {
            topAppBar?.navigationIcon = null
        }

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

    @SuppressLint("Range")
    private fun loadIntoList() {

        val dbHandler = DBHelper(requireActivity().applicationContext, null)

        if (dbHandler.getCount() > 0) {
            val noHoursStoredTextView = activity?.findViewById<TextView>(R.id.noHoursStoredTextView)
            noHoursStoredTextView?.visibility = View.GONE
        } else {
            val noHoursStoredTextView = activity?.findViewById<TextView>(R.id.noHoursStoredTextView)
            noHoursStoredTextView?.visibility = View.VISIBLE

        }

        var y = 0.0

        dataList.clear()
        val cursor = dbHandler.getAllRow(requireContext())
        cursor!!.moveToFirst()

        val containsColonArray = arrayListOf<String>()

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
            map["inTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
            map["outTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
            map["breakTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
            map["totalHours"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
            map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))
            dataList.add(map)

            val array = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)).toString()
            containsColonArray.add(
                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)).toString()
            )

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
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_calculating_hours),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                containsColon = true

            } else {
                y += array.toDouble()
            }



            output = String.format("%.2f", y)
            outputWages = output

            cursor.moveToNext()

        }

        if (dataList.isNotEmpty()) {
            for (i in 0 until dataList.count()) {
                val (wholeNumber, decimal) = output.split(".")
                var minute = (".$decimal".toDouble() * 60).toInt().toString()
                if (minute.length == 1) {
                    minute = "0$minute"
                }

                outputColon = "$wholeNumber:$minute"
            }
        }

        val listView = activity?.findViewById<RecyclerView>(R.id.listView)
        listView?.layoutManager = linearLayoutManager
        listView?.adapter = customAdapter

    }

    @SuppressLint("Range")
    private fun changeSortMethod() {

        val dbHandler = DBHelper(requireActivity().applicationContext, null)

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
    }

    @SuppressLint("Range")
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

        listView?.adapter?.notifyItemInserted(ItemPosition(requireContext()).loadPosition())
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

        val dbHandler = DBHelper(requireContext(), null)

        var y = 0.0

        val cursor = dbHandler.getAllRow(requireContext())
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {

            val array = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)).toString()

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

            output = String.format("%.2f", y)
            cursor.moveToNext()

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

        saveState()

    }

    fun checkBoxVisible(visible: Boolean) {

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)

        if (visible) {
            topAppBar?.navigationIcon = ResourcesCompat.getDrawable(
                requireContext().resources,
                R.drawable.ic_baseline_close_24,
                requireContext().theme
            )
            val xIconDrawable = topAppBar?.navigationIcon
            xIconDrawable?.mutate()

            if (MenuTintData(requireContext()).loadMenuTint()) {
                xIconDrawable?.colorFilter = BlendModeColorFilter(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                    BlendMode.SRC_ATOP
                )
            } else {
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                val id = typedValue.resourceId
                xIconDrawable?.colorFilter = BlendModeColorFilter(
                    ContextCompat.getColor(requireContext(), id),
                    BlendMode.SRC_ATOP
                )
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
                Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
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