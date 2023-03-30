package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.AccentColorData
import com.cory.hourcalculator.sharedprefs.ColoredTitleBarTextData
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.cory.hourcalculator.sharedprefs.VersionData
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.CornerFamily

class SettingsFragment : Fragment() {

    private var scrollPosition = 0

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateCustomColor()
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
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            //(activity as MainActivity).setActiveTab(3)
        }

        MainActivity().runOnUiThread(runnable)

        activity?.window?.setBackgroundDrawable(null)

            updateCustomColor()

        val inputManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)

        main()

        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nestedScrollViewSettings)

        nestedScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->

            scrollPosition = scrollX
        }
    }

    private fun main() {

        val appearanceCardView = requireActivity().findViewById<MaterialCardView>(R.id.themeCardView)
        val appSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.appSettingsCardView)
        val historySettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.historySettingsCardView)
        val timeCardSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.timeCardSettingsCardView)
        val animationSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.animationSettingsCardView)
        val timeCardGalleryCardView = requireActivity().findViewById<MaterialCardView>(R.id.galleryCardView)
        val patchNotesCardView = requireActivity().findViewById<MaterialCardView>(R.id.patchNotesCardView)
        val aboutCardView = requireActivity().findViewById<MaterialCardView>(R.id.aboutAppCardView)
        val deleteAppDataCardView = requireActivity().findViewById<MaterialCardView>(R.id.deleteAppDataCardView)

        appearanceCardView.shapeAppearanceModel = appearanceCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        appSettingsCardView.shapeAppearanceModel = appSettingsCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        historySettingsCardView.shapeAppearanceModel = historySettingsCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        timeCardSettingsCardView.shapeAppearanceModel = timeCardSettingsCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        animationSettingsCardView.shapeAppearanceModel = animationSettingsCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        timeCardGalleryCardView.shapeAppearanceModel = timeCardGalleryCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        patchNotesCardView.shapeAppearanceModel = patchNotesCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        aboutCardView.shapeAppearanceModel = aboutCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        deleteAppDataCardView.shapeAppearanceModel = deleteAppDataCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        appearanceCardView?.setOnClickListener {
            (context as MainActivity).currentSettingsItem = 0
            openFragment(AppearanceFragment())
        }

        appSettingsCardView?.setOnClickListener {
            (context as MainActivity).currentSettingsItem = 1
            openFragment(AppSettingsFragment())
        }

        historySettingsCardView?.setOnClickListener {
            (context as MainActivity).currentSettingsItem = 2
            openFragment(HistorySettingsFragment())
        }

        timeCardSettingsCardView.setOnClickListener {
            openFragment(TimeCardSettingsFragment())
        }

        timeCardGalleryCardView.setOnClickListener {
            openFragment(GalleryFragment())
        }

        val patchNotesChevron = activity?.findViewById<ImageView>(R.id.patchNotesChevron)

        patchNotesCardView?.setOnClickListener {
            (context as MainActivity).currentSettingsItem = 3
            openFragment(PatchNotesFragment())
        }

        if (VersionData(requireContext()).loadVersion() != BuildConfig.VERSION_NAME) {
            patchNotesChevron?.setImageResource(R.drawable.baseline_priority_high_24)
            patchNotesChevron?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.redAccent
                )
            )
        }

        aboutCardView?.setOnClickListener {
            (context as MainActivity).currentSettingsItem = 6
            openFragment(AboutAppFragment())
        }

        deleteAppDataCardView?.setOnClickListener {
            showDeleteDataAlert()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.supportFragmentManager?.popBackStack()
                }
            })
    }

    private fun openFragment(fragment: Fragment) {
        Vibrate().vibration(requireContext())

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        transaction?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)
        transaction?.commit()
    }

    private fun showDeleteDataAlert() {
        Vibrate().vibration(requireContext())

        val alert =
            MaterialAlertDialogBuilder(requireContext(), AccentColorData(requireContext()).alertTheme())
        alert.setTitle(getString(R.string.warning))
        alert.setMessage(getString(R.string.would_you_like_to_delete_all_app_data))
        alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
            Vibrate().vibration(requireContext())
            val dbHandler = DBHelper(requireContext(), null)
            requireContext().getSharedPreferences("file", 0).edit().clear().apply()
            requireContext().cacheDir.deleteRecursively()
            dbHandler.deleteAll()
            Toast.makeText(requireContext(), getString(R.string.app_data_cleared), Toast.LENGTH_LONG).show()
            Handler(Looper.getMainLooper()).postDelayed({
                val intent =
                    requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
                intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                activity?.finish()
            }, 1500)
        }
        alert.setNegativeButton(getString(R.string.no)) { _, _ ->
            Vibrate().vibration(requireContext())
        }
        alert.show()
    }

    private fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.settingsCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val appearanceCardView = requireActivity().findViewById<MaterialCardView>(R.id.themeCardView)
        val appSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.appSettingsCardView)
        val historySettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.historySettingsCardView)
        val timeCardSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.timeCardSettingsCardView)
        val animationSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.animationSettingsCardView)
        val timeCardGalleryCardView = requireActivity().findViewById<MaterialCardView>(R.id.galleryCardView)
        val patchNotesCardView = requireActivity().findViewById<MaterialCardView>(R.id.patchNotesCardView)
        val aboutCardView = requireActivity().findViewById<MaterialCardView>(R.id.aboutAppCardView)
        val deleteAppDataCardView = requireActivity().findViewById<MaterialCardView>(R.id.deleteAppDataCardView)

        appearanceCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        appSettingsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        historySettingsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        timeCardSettingsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        animationSettingsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        timeCardGalleryCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        patchNotesCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        aboutCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        deleteAppDataCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val collapsingToolbarLayout = requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutSettings)
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        collapsingToolbarLayout.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutSettings)?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutSettings)?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }
    }
}