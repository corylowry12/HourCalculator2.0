package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.PatchNotesBugFixesAdapter
import com.cory.hourcalculator.adapters.PatchNotesEnhancementsAdapter
import com.cory.hourcalculator.adapters.PatchNotesNewFeaturesAdapter
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class PatchNotesFragment : Fragment() {

    private var bugFixesArray = arrayOf("Fixed issue where when resetting \'App Settings\' to default it would uncheck the Vibration switch even though vibration is enabled after resetting", "Fixed issue with there being no vibration when long clicking items in history view",
                                        "Fixed issue with there being no vibration when clicking \"Hide\" to hide checkboxes", "Fixed issue with there being no vibration when clicking \"Report A Bug\" in the about app view", "Fixed issue with the shape of the floating action button in the history view",
                                        "Fixed crashing when trying to leave the edit hour view if there were pending changes and you clicked yes to save them", "Fixed issue with title and menu buttons not being centered properly in title bars",
                                        "Fixed issue with github button in about view still using built in web view instead of custom tab", "Fixed issue when hitting back button and the bottom nav bar wouldn't change selected tab",
                                        "Fixed issue where if you went to edit an entry and the in time or out time hours were equal to 12, it would change it to 11", "Fixed issue with deleting selected not working properly if you deleted multiple entries in quick succession one at a time",
                                        "Fixed some issues with weird splash screen logos on certain devices", "Fixed issue with there being not vibration when clicking the view repo button in the about app view")

    private var newFeaturesArray = arrayOf("Added the ability to click on the history tab when the history view is active and scroll to the top", "Added the ability to calculate in time or decimal format depending on which is enabled by long pressing the calculate button (eg. if decimal format is enabled, long pressing will calculate in time format)",
                                            "Added the ability to click on an hour entry and open it to edit it (disabled by default, to enable go to Settings->App Settings->Open hour for editing on history item click)", "Added a toggle to change menu item tint to match the theme")

    private var enhancementsArray = arrayOf("Enabling and disabling history will now use a switch instead of radio buttons", "The switches in the settings will now have an icon based on their value", "Converted dialogs throughout the app to be Bottom Sheets",
                                            "Made the entire view clickable when selecting an app icon instead of just the check box", "Made the entire view clickable when selecting multiple hours to delete, it will function the same way as the checkbox (clicking will select and long clicking will select all)",
                                            "App will now show \"Error\" if there was a problem calculating wages", "App will now calculate hours via the info dialog in the history view to show HOURS:MINUTES if there is no decimal time stored",
                                            "Optimizations when launching the app", "Optimizations in switching tabs", "Improved the quality of the app icon logos in the Appearance Settings view", "Status bar will now match the color of the theme and will change color based on scroll position",
                                            "App icon radio buttons will no be checked if you click the icon but not the radio button", "App will no longer let you go to another tab when edit view is visible to prevent accidentally leaving and losing edited data",
                                            "Adjusted left and right margins for the output text view on the home view", "App will now only use custom tabs instead of the previous built in web view", "Custom tabs will now match the accent color of the app", "Changed red 1 logo when there was a new update in patch notes setting item",
                                            "Updated themed icon to match the other regular icons", "Changed the chip color in the patch notes and time cards view to match the theming better", "Delete menu item in the edit view is now an icon instead of a drop down menu",
                                            "Removed auto icon theming, you will now just have to manually pick an app icon, this way the app doesn't have to restart every time you change a theme", "Major refactor of code to optimize the history view")

    private var bugFixesArrayInternal = arrayOf("Fixed issue where if you went to time card info view and scrolled and clicked the time card tab it would make the app crash", "Fixed crashing when opening the version info view", "Fixed issue with reset settings bottom sheet not matching the current theme",
                                                "Fixed issue with there being no vibration when clicking the reset button in the number of days view", "Fixed issue with there being not vibration when clicking the view repo button in the about app view", "Fixed issue with layout not being positioned properly in the edit view",
                                                "Fixed issue with there being no vibration when clicking the generate a random color button", "Fixed issue with crashing when deleting an entry in the edit view", "Fixed issue with cards in the history view not being curved properly when undoing the deletion of an hour in the edit view",
                                                "Fixed issue with there being no vibration when clicking the delete or delete all buttons in the time cards view", "Fixed some issues with font size in the number of days view", "Fixed issue with the one entry button being offset in the number of days view")

    private var newFeaturesArrayInternal = arrayOf("No new features")

    private var enhancementsArrayInternal = arrayOf("Tweaked the design of the history item options menu (menu that contains edit, delete, and delete all)")

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
        return inflater.inflate(R.layout.fragment_patch_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateCustomTheme()
        val topAppBar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarPatchNotes)

        val navigationDrawable = topAppBar?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
                navigationDrawable?.colorFilter = BlendModeColorFilter(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                    BlendMode.SRC_ATOP
                )
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }

        if (BuildConfig.FLAVOR == "Internal") {
            topAppBar?.title = "Patch Notes (Internal)"
        }

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val bugFixesChip = requireView().findViewById<Chip>(R.id.bugFixesChip)
        if (BuildConfig.FLAVOR == "Internal") {
            bugFixesChip.text = bugFixesArrayInternal.count().toString()
        }
        else {
            bugFixesChip.text = bugFixesArray.count().toString()
        }

        val newFeaturesChip = requireView().findViewById<Chip>(R.id.newFeaturesChip)
        if (BuildConfig.FLAVOR == "Internal") {
            newFeaturesChip.text = newFeaturesArrayInternal.count().toString()
        }
        else {
            newFeaturesChip.text = newFeaturesArray.count().toString()
        }

        val enhancementsChip = requireView().findViewById<Chip>(R.id.enhancementsChip)
        if (BuildConfig.FLAVOR == "Internal") {
            enhancementsChip.text = enhancementsArrayInternal.count().toString()
        }
        else {
            enhancementsChip.text = enhancementsArray.count().toString()
        }

        val bugFixesConstraint = requireView().findViewById<ConstraintLayout>(R.id.bugFixesConstraint)

        bugFixesConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val bugFixesRecyclerView = requireView().findViewById<RecyclerView>(R.id.bugFixesRecyclerView)
            bugFixesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            if (BuildConfig.FLAVOR == "Internal") {
                bugFixesRecyclerView.adapter =
                    PatchNotesBugFixesAdapter(requireContext(), bugFixesArrayInternal)
            }
            else {
                bugFixesRecyclerView.adapter =
                    PatchNotesBugFixesAdapter(requireContext(), bugFixesArray)
            }
            if (bugFixesRecyclerView.visibility == View.GONE) {
                bugFixesRecyclerView.visibility = View.VISIBLE
                bugFixesChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                bugFixesRecyclerView.visibility = View.GONE
                bugFixesChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        val newFeaturesConstraint = requireView().findViewById<ConstraintLayout>(R.id.newFeaturesConstraint)

        newFeaturesConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val newFeaturesRecyclerView = requireView().findViewById<RecyclerView>(R.id.newFeaturesRecyclerView)
            newFeaturesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            if (BuildConfig.FLAVOR == "Internal") {
                newFeaturesRecyclerView.adapter =
                    PatchNotesNewFeaturesAdapter(requireContext(), newFeaturesArrayInternal)
            }
            else {
                newFeaturesRecyclerView.adapter =
                    PatchNotesNewFeaturesAdapter(requireContext(), newFeaturesArray)
            }

            if (newFeaturesRecyclerView.visibility == View.GONE) {
                newFeaturesRecyclerView.visibility = View.VISIBLE
                newFeaturesChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                newFeaturesRecyclerView.visibility = View.GONE
                newFeaturesChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        val enhancementsConstraint = requireView().findViewById<ConstraintLayout>(R.id.enhancementsConstraint)

        enhancementsConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val enhancementsRecyclerView = requireView().findViewById<RecyclerView>(R.id.enhancementsRecyclerView)
            enhancementsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            if (BuildConfig.FLAVOR == "Internal") {
                enhancementsRecyclerView.adapter =
                    PatchNotesEnhancementsAdapter(requireContext(), enhancementsArrayInternal)
            }
            else {
                enhancementsRecyclerView.adapter =
                    PatchNotesEnhancementsAdapter(requireContext(), enhancementsArray)
            }

            if (enhancementsRecyclerView.visibility == View.GONE) {
                enhancementsRecyclerView.visibility = View.VISIBLE
                enhancementsChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                enhancementsRecyclerView.visibility = View.GONE
                enhancementsChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.closeAll -> {

                    val bugFixesRecyclerView = requireView().findViewById<RecyclerView>(R.id.bugFixesRecyclerView)
                    val newFeaturesRecyclerView = requireView().findViewById<RecyclerView>(R.id.newFeaturesRecyclerView)
                    val enhancementsRecyclerView = requireView().findViewById<RecyclerView>(R.id.enhancementsRecyclerView)

                    if (bugFixesRecyclerView.visibility == View.VISIBLE) {
                        bugFixesRecyclerView.visibility = View.GONE
                        bugFixesChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }
                    if (newFeaturesRecyclerView.visibility == View.VISIBLE) {
                        newFeaturesRecyclerView.visibility = View.GONE
                        newFeaturesChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }

                    if (enhancementsRecyclerView.visibility == View.VISIBLE) {
                        enhancementsRecyclerView.visibility = View.GONE
                        enhancementsChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }

                    val handler = Handler(Looper.getMainLooper())
                    val runnable = Runnable {
                        val collapsingToolbarLayoutPatchNotes = requireView().findViewById<AppBarLayout>(R.id.appBarLayoutPatchNotes)
                        collapsingToolbarLayoutPatchNotes.setExpanded(true, true)
                    }
                    handler.postDelayed(runnable, 50)
                    true
                }
                else -> true
            }
        }

        Version(requireContext()).setVersion(BuildConfig.VERSION_NAME)

        val runnable = Runnable {
            (context as MainActivity).changeSettingsBadge()
        }
        MainActivity().runOnUiThread(runnable)
    }

    private fun updateCustomTheme(): MaterialToolbar? {
        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarPatchNotes)

        requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutPatchNotes)
            .setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutPatchNotes)
            .setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        requireActivity().findViewById<MaterialCardView>(R.id.bugFixesOuterCardView)
            .setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        requireActivity().findViewById<MaterialCardView>(R.id.newFeaturesOuterCardView)
            .setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        requireActivity().findViewById<MaterialCardView>(R.id.enhancementsOuterCardView)
            .setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        requireActivity().findViewById<Chip>(R.id.bugFixesChip)
            .setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBottomNavTextColor()))
        requireActivity().findViewById<Chip>(R.id.bugFixesChip).closeIconTint =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateBottomNavTextColor()))
        requireActivity().findViewById<Chip>(R.id.bugFixesChip).chipBackgroundColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateChipBackgroundColor()))
        requireActivity().findViewById<Chip>(R.id.newFeaturesChip)
            .setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBottomNavTextColor()))
        requireActivity().findViewById<Chip>(R.id.newFeaturesChip).closeIconTint =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateBottomNavTextColor()))
        requireActivity().findViewById<Chip>(R.id.newFeaturesChip).chipBackgroundColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateChipBackgroundColor()))
        requireActivity().findViewById<Chip>(R.id.enhancementsChip)
            .setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBottomNavTextColor()))
        requireActivity().findViewById<Chip>(R.id.enhancementsChip).closeIconTint =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateBottomNavTextColor()))
        requireActivity().findViewById<Chip>(R.id.enhancementsChip).chipBackgroundColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateChipBackgroundColor()))
        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutPatchNotes)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutPatchNotes)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }
        return topAppBar
    }
}