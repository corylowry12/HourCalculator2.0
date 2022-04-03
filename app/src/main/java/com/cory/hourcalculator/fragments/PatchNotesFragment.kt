package com.cory.hourcalculator.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.PatchNotesBugFixesAdapter
import com.cory.hourcalculator.adapters.PatchNotesEnhancementsAdapter
import com.cory.hourcalculator.adapters.PatchNotesNewFeaturesAdapter
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class PatchNotesFragment : Fragment() {

    private var bugFixesArray = arrayOf("Fixed crashing in the appearance fragment when checking if you are on android 12 or later if your version was 8.0.0", "Fixed crashing when checking for updates if you had no internet connection", "Fixed issue with settings not remembering scroll position if you left view and came back to it",
                                        "Fixed issue with appearance settings not remembering scroll position if you changed the theme and the view restarted", "Fixed issues with there being no haptic feedback for most dialogs in the appearance settings view",
                                        "Fixed issues with some radio buttons not providing haptic feedback when clicked in the Appearance Settings view", "Fixed issue with the back arrow not being the same color as title in webview when light theme was active",
                                        "Fixed issue with Calculate button having no space between it and the break text box", "Fixed issue with app vibrating twice if you selected gray theme", "Fixed issue with Snackbar message showing up in a different location after you clicked retry if it failed to check for updates")
    private var newFeaturesArray = arrayOf("Added option to match googles apps when using the follow system theme option in the appearance settings (not available on devices not on android 12 or later)", "Added the ability to undo the deletion and disable automatic deletion if you enable it and it prompts you to delete and you click yes. It will display in a snackbar message with an undo button")
    private var enhancementsArray = arrayOf("Updated Dependencies", "Changed icons in the bottom nav bar to be outlined", "Changed History Tab Icon",
                                            "Icons in the bottom nav bar will now be shaded if for each active tab", "Improved performance when setting icons in the Appearance Settings",
                                            "Redesigned title bar in the History View", "Title bar in History will now collapse on scroll", "Floating action button will now slide up when snackbar message shows up in history",
                                            "Added rounded corners to menu in history", "Icons in the title bar in History will now be whatever color the chosen accent color is", "Redesigned the reset to defaults menus throughout the app",
                                            "Redesigned the menu in the webview", "The \"Follow System\" option in the Appearance Settings has been renamed to \"Material You\"", "Improved haptic feedback on devices Android 12+",
                                            "Improved animations when scrolling back to the top after clicking FAB in the History View", "Text Color in bottom nav bar will now be the same color as the icons",
                                            "Tweaked red accent colors", "Tweaked break text box hint color to make it more legible when the gray theme is enabled", "Adjusted the corner radius of the break text box to make it match with the rest of the app",
                                            "Tweaked badge color that contains the number of hours stored", "Improved the speed of animations when switching tabs", "Snackbar undo message after hour deletion will now disappear when you leave the history tab",
                                            "Performance improvements when deleting entries automatically via Automatic Deletion in the Settings", "Reordered some items in the settings", "Tweaked some colors throughout the UI",
                                            "Rewrote the FAQ fragment, it can now be updated and changed remotely (No longer requires an app update to change the questions)", "History will now automatically scroll back to the top when you change the sorting method",
                                            "Reworded the toast messages that pop up when you change automatic deletion settings", "Made haptic feedback more pleasant when changing the time", "Appearance Settings has been renamed to Appearance",
                                            "App will now default to Material You theming if you are on Android 12 or later", "Added a badge to let you know how many items are in each section in the patch notes view", "Added a 500 millisecond delay to the \"failed to check for updates\" snackbar message so it will no longer show up as soon as the app opens",
                                            "Tapping the copy menu item in the webview will now say \"URL copied to clipboard\" instead of \"Text copied to clipboard\"")

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
        return inflater.inflate(R.layout.fragment_patch_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarPatchNotes)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val bugFixesCounterTextView = requireView().findViewById<TextView>(R.id.bugFixesCounterTextView)
        bugFixesCounterTextView.text = bugFixesArray.count().toString()

        val newFeaturesCounterTextView = requireView().findViewById<TextView>(R.id.newFeaturesCounterTextView)
        newFeaturesCounterTextView.text = newFeaturesArray.count().toString()

        val enhancementsCounterTextView = requireView().findViewById<TextView>(R.id.enhancementsCounterTextView)
        enhancementsCounterTextView.text = enhancementsArray.count().toString()

        val bugFixesConstraint = requireView().findViewById<ConstraintLayout>(R.id.bugFixesConstraint)

        bugFixesConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val bugFixesRecyclerView = requireView().findViewById<RecyclerView>(R.id.bugFixesRecyclerView)
            bugFixesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            bugFixesRecyclerView.adapter = PatchNotesBugFixesAdapter(requireContext(), bugFixesArray)
            val bugFixesChevron = requireView().findViewById<ImageView>(R.id.bugFixesChevronImage)

            if (bugFixesRecyclerView.visibility == View.GONE) {
                bugFixesRecyclerView.visibility = View.VISIBLE
                bugFixesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                bugFixesRecyclerView.visibility = View.GONE
                bugFixesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        val newFeaturesConstraint = requireView().findViewById<ConstraintLayout>(R.id.newFeaturesConstraint)

        newFeaturesConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val newFeaturesRecyclerView = requireView().findViewById<RecyclerView>(R.id.newFeaturesRecyclerView)
            newFeaturesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            newFeaturesRecyclerView.adapter = PatchNotesNewFeaturesAdapter(requireContext(), newFeaturesArray)
            val newFeaturesChevron = requireView().findViewById<ImageView>(R.id.newFeaturesChevronImage)

            if (newFeaturesRecyclerView.visibility == View.GONE) {
                newFeaturesRecyclerView.visibility = View.VISIBLE
                newFeaturesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                newFeaturesRecyclerView.visibility = View.GONE
                newFeaturesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        val enhancementsConstraint = requireView().findViewById<ConstraintLayout>(R.id.enhancementsConstraint)

        enhancementsConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val enhancementsRecyclerView = requireView().findViewById<RecyclerView>(R.id.enhancementsRecyclerView)
            enhancementsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            enhancementsRecyclerView.adapter = PatchNotesEnhancementsAdapter(requireContext(), enhancementsArray)
            val enhancementsChevron = requireView().findViewById<ImageView>(R.id.enhancementsChevronImage)

            if (enhancementsRecyclerView.visibility == View.GONE) {
                enhancementsRecyclerView.visibility = View.VISIBLE
                enhancementsChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                enhancementsRecyclerView.visibility = View.GONE
                enhancementsChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.closeAll -> {

                    val bugFixesRecyclerView = requireView().findViewById<RecyclerView>(R.id.bugFixesRecyclerView)
                    val newFeaturesRecyclerView = requireView().findViewById<RecyclerView>(R.id.newFeaturesRecyclerView)
                    val enhancementsRecyclerView = requireView().findViewById<RecyclerView>(R.id.enhancementsRecyclerView)

                    val bugFixesChevron = requireView().findViewById<ImageView>(R.id.bugFixesChevronImage)
                    val newFeaturesChevron = requireView().findViewById<ImageView>(R.id.newFeaturesChevronImage)
                    val enhancementsChevron = requireView().findViewById<ImageView>(R.id.enhancementsChevronImage)

                    if (bugFixesRecyclerView.visibility == View.VISIBLE) {
                        bugFixesRecyclerView.visibility = View.GONE
                        bugFixesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }
                    if (newFeaturesRecyclerView.visibility == View.VISIBLE) {
                        newFeaturesRecyclerView.visibility = View.GONE
                        newFeaturesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }

                    if (enhancementsRecyclerView.visibility == View.VISIBLE) {
                        enhancementsRecyclerView.visibility = View.GONE
                        enhancementsChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }
                    true
                }
                else -> true
            }
        }

        Version(requireContext()).setVersion(getString(R.string.version_number))

        val runnable = Runnable {
            (context as MainActivity).changeSettingsBadge()
        }
        MainActivity().runOnUiThread(runnable)
    }
}