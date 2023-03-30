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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.adapters.PatchNotesAdapter
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.sharedprefs.ColoredTitleBarTextData
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.cory.hourcalculator.sharedprefs.MenuTintData
import com.cory.hourcalculator.sharedprefs.VersionData
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip

class PatchNotesFragment : Fragment() {

    private lateinit var bugFixesAdapter: PatchNotesAdapter
    private lateinit var newFeaturesAdapter: PatchNotesAdapter
    private lateinit var enhancementsAdapter: PatchNotesAdapter

    private var bugFixesArray = arrayOf("Fixed issue where when resetting \'App Settings\' to default it would uncheck the Vibration switch even though vibration is enabled after resetting", "Fixed issue with there being no vibration when long clicking items in history view",
                                        "Fixed issue with there being no vibration when clicking \"Hide\" to hide checkboxes", "Fixed issue with there being no vibration when clicking \"Report A Bug\" in the about app view", "Fixed issue with the shape of the floating action button in the history view",
                                        "Fixed crashing when trying to leave the edit hour view if there were pending changes and you clicked yes to save them", "Fixed issue with title and menu buttons not being centered properly in title bars",
                                        "Fixed issue with github button in about view still using built in web view instead of custom tab", "Fixed issue when hitting back button and the bottom nav bar wouldn't change selected tab",
                                        "Fixed issue where if you went to edit an entry and the in time or out time hours were equal to 12, it would change it to 11", "Fixed issue with deleting selected not working properly if you deleted multiple entries in quick succession one at a time",
                                        "Fixed some issues with weird splash screen logos on certain devices", "Fixed issue with there being not vibration when clicking the view repo button in the about app view", "Fixed issue where the date would be December 31st, 1969 when undoing hour deletion in the history settings view")

    private var newFeaturesArray = arrayOf("Added the ability to click on the history tab when the history view is active and scroll to the top", "Added the ability to calculate in time or decimal format depending on which is enabled by long pressing the calculate button (eg. if decimal format is enabled, long pressing will calculate in time format)",
                                            "Added the ability to click on an hour entry and open it to edit it (disabled by default, to enable go to Settings->App Settings->Open hour for editing on history item click)", "Added a toggle to change menu item tint to match the theme")

    private var enhancementsArray = arrayOf("Enabling and disabling history will now use a switch instead of radio buttons", "The switches in the settings will now have an icon based on their value", "Converted dialogs throughout the app to be Bottom Sheets",
                                            "Made the entire view clickable when selecting an app icon instead of just the check box", "Made the entire view clickable when selecting multiple hours to delete, it will function the same way as the checkbox (clicking will select and long clicking will select all)",
                                            "App will now show \"Error\" if there was a problem calculating wages", "App will now calculate hours via the info dialog in the history view to show HOURS:MINUTES if there is no decimal time stored",
                                            "Optimizations when launching the app", "Optimizations in switching tabs", "Improved the quality of the app icon logos in the Appearance Settings view", "Status bar will now match the color of the theme and will change color based on scroll position",
                                            "App icon radio buttons will no be checked if you click the icon but not the radio button", "App will no longer let you go to another tab when edit view is visible to prevent accidentally leaving and losing edited data",
                                            "Adjusted left and right margins for the output text view on the home view", "App will now only use custom tabs instead of the previous built in web view", "Custom tabs will now match the accent color of the app", "Changed red 1 logo when there was a new update in patch notes setting item",
                                            "Updated themed icon to match the other regular icons", "Changed the chip color in the patch notes and time cards view to match the theming better", "Delete menu item in the edit view is now an icon instead of a drop down menu",
                                            "Removed auto icon theming, you will now just have to manually pick an app icon, this way the app doesn't have to restart every time you change a theme", "Major refactor of code to optimize the history view", "Added a toast message when entry is automatically deleted")

    private var bugFixesArrayInternal = arrayOf("Fixed issue where if you enabled material you theming it would update the custom color circle image", "Fixed issue where hint text color in the rename item bottom sheet was purple", "Fixed issue where wages wouldn't update if you viewed info and then deleted some items",
                                                "Fixed issue where if you saved colors and restarted the app, the colors would be gone")

    private var newFeaturesArrayInternal = arrayOf("Added the ability to rename a time card entry without entering the time card info view and changing the text box", "Added ability to change the badge color on the bottom navigation view (the icon that shows the number of items in history and time cards) to match the accent color", "Added the ability to remove a photo without viewing the photo first",
                                                    "Added the ability to manage a photo in the time card view just by long pressing on it", "Added ability to make background be more colorful, instead of being white/black, it'll be a light or dark shade of whatever accent color is chosen")

    private var enhancementsArrayInternal = arrayOf("Removed ability to save color by long pressing the pick a custom color card", "App will now generate a random color every time the app is launched whether its enabled or not, it just won't set the color if its disabled", "Accent color custom hex code text view will now show if a color is saved and if you have named it," +
                                                    " it will show the name")

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        updateCustomTheme()

        bugFixesAdapter.updateCardColor()
        enhancementsAdapter.updateCardColor()
        newFeaturesAdapter.updateCardColor()

        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                activity?.setTheme(R.style.Theme_DarkTheme)
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
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                activity?.setTheme(R.style.Theme_DarkTheme)
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
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
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

        if (BuildConfig.FLAVOR == "Internal") {
            topAppBar?.title = "Patch Notes (Internal)"
        }

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }
        if (BuildConfig.FLAVOR == "Internal") {
            bugFixesAdapter = PatchNotesAdapter(requireContext(), bugFixesArrayInternal)
            newFeaturesAdapter = PatchNotesAdapter(requireContext(), newFeaturesArrayInternal)
            enhancementsAdapter = PatchNotesAdapter(requireContext(), enhancementsArrayInternal)
        }
        else {
            bugFixesAdapter = PatchNotesAdapter(requireContext(), bugFixesArray)
            newFeaturesAdapter = PatchNotesAdapter(requireContext(), newFeaturesArray)
            enhancementsAdapter = PatchNotesAdapter(requireContext(), enhancementsArray)
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
                    bugFixesAdapter
            }
            else {
                bugFixesRecyclerView.adapter =
                    bugFixesAdapter
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
                    newFeaturesAdapter
            }
            else {
                newFeaturesRecyclerView.adapter =
                    newFeaturesAdapter
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
                    enhancementsAdapter
            }
            else {
                enhancementsRecyclerView.adapter =
                    enhancementsAdapter
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

        VersionData(requireContext()).setVersion(BuildConfig.VERSION_NAME)

        val runnable = Runnable {
            (context as MainActivity).changeSettingsBadge()
        }
        MainActivity().runOnUiThread(runnable)
    }

    private fun updateCustomTheme(): MaterialToolbar? {
        requireActivity().findViewById<CoordinatorLayout>(R.id.patchNotesCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarPatchNotes)

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

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutPatchNotes)
            ?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

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