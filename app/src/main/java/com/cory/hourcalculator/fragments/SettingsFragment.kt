package com.cory.hourcalculator.fragments

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.CornerFamily

class SettingsFragment : Fragment() {

    private var scrollPosition = 0

    private val iconDisableArray = arrayListOf<String>()
    private var iconEnableID = ""

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

        try {
            val runnable = Runnable {
                (activity as MainActivity).currentTab = 3
                (activity as MainActivity).setActiveTab(3)
            }

            MainActivity().runOnUiThread(runnable)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        updateCustomColor()

            main()
    }

    private fun main() {

        val appearanceCardView = requireActivity().findViewById<MaterialCardView>(R.id.themeCardView)
        val appSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.appSettingsCardView)
        val historySettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.historySettingsCardView)
        val timeCardSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.timeCardSettingsCardView)
        val wageSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.wageSettingsCardView)
        val animationSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.animationSettingsCardView)
        val tabletSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.tabletSettingsCardView)
        val timeCardGalleryCardView = requireActivity().findViewById<MaterialCardView>(R.id.galleryCardView)
        val backupRestoreCardView = requireActivity().findViewById<MaterialCardView>(R.id.backupCardView)
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
        wageSettingsCardView.shapeAppearanceModel = wageSettingsCardView.shapeAppearanceModel
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
        if (resources.getBoolean(R.bool.isTablet)) {
            tabletSettingsCardView.shapeAppearanceModel =
                tabletSettingsCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
        }
        else {
            tabletSettingsCardView.visibility = View.GONE
        }
        timeCardGalleryCardView.shapeAppearanceModel = timeCardGalleryCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        backupRestoreCardView.shapeAppearanceModel = backupRestoreCardView.shapeAppearanceModel
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

        wageSettingsCardView.setOnClickListener {
            openFragment(WageSettingsFragment())
        }

        animationSettingsCardView.setOnClickListener {
            openFragment(AnimationSettingsFragment())
        }

        tabletSettingsCardView.setOnClickListener {
            openFragment(TabletSettingsFragment())
        }

        timeCardGalleryCardView.setOnClickListener {
            openFragment(GalleryFragment())
        }

        backupRestoreCardView.setOnClickListener {
            openFragment(BackupRestoreFragment())
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

        val dialog = BottomSheetDialog(requireContext())
        val deleteAllLayout = layoutInflater.inflate(R.layout.delete_all_app_data_bottom_sheet, null)
        dialog.setContentView(deleteAllLayout)

        if (resources.getBoolean(R.bool.isTablet)) {
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.isDraggable = false
        }
        val infoCardView =
            deleteAllLayout.findViewById<MaterialCardView>(R.id.infoCardView)
        val yesButton = deleteAllLayout.findViewById<Button>(R.id.yesButton)
        val noButton = deleteAllLayout.findViewById<Button>(R.id.noButton)

        infoCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(requireContext()).generateCardColor()
            )
        )
        yesButton.setBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCustomColorPrimary()
            )
        )
        noButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

        yesButton.setOnClickListener {
            Vibrate().vibration(requireContext())
            val dbHandler = DBHelper(requireContext(), null)
            requireContext().getSharedPreferences("file", 0).edit().clear().apply()
            requireContext().cacheDir.deleteRecursively()
            dbHandler.deleteAll()
            TimeCardDBHelper(requireContext(), null).deleteAll()
            TimeCardsItemDBHelper(requireContext(), null).deleteAll()

            iconDisableArray.clear()
            iconEnableID = "com.cory.hourcalculator.SplashScreenNoIcon"
            iconDisableArray.add("com.cory.hourcalculator.SplashPink")
            iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
            iconDisableArray.add("com.cory.hourcalculator.SplashRed")
            iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
            iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
            iconDisableArray.add("com.cory.hourcalculator.SplashOG")
            iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
            changeIcons()
            ChosenAppIconData(requireContext()).setChosenAppIcon(getString(R.string.teal))
            restartApplication()
            dialog.dismiss()
        }
        noButton.setOnClickListener {
            Vibrate().vibration(requireContext())
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.settingsCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val appearanceCardView = requireActivity().findViewById<MaterialCardView>(R.id.themeCardView)
        val appSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.appSettingsCardView)
        val historySettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.historySettingsCardView)
        val timeCardSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.timeCardSettingsCardView)
        val wageSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.wageSettingsCardView)
        val animationSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.animationSettingsCardView)
        val tabletSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.tabletSettingsCardView)
        val timeCardGalleryCardView = requireActivity().findViewById<MaterialCardView>(R.id.galleryCardView)
        val backupRestoreCardView = requireActivity().findViewById<MaterialCardView>(R.id.backupCardView)
        val patchNotesCardView = requireActivity().findViewById<MaterialCardView>(R.id.patchNotesCardView)
        val aboutCardView = requireActivity().findViewById<MaterialCardView>(R.id.aboutAppCardView)
        val deleteAppDataCardView = requireActivity().findViewById<MaterialCardView>(R.id.deleteAppDataCardView)

        appearanceCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        appSettingsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        historySettingsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        timeCardSettingsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        wageSettingsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        animationSettingsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        tabletSettingsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        timeCardGalleryCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        backupRestoreCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
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

    private fun changeIcons() {
        requireContext().packageManager?.setComponentEnabledSetting(
            ComponentName(
                BuildConfig.APPLICATION_ID,
                iconEnableID
            ),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        for (i in 0 until iconDisableArray.count()) {
            requireContext().packageManager?.setComponentEnabledSetting(
                ComponentName(
                    BuildConfig.APPLICATION_ID,
                    iconDisableArray.elementAt(i)
                ),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }

    private fun restartApplication() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent =
                requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            requireContext().startActivity(intent)
            (context as Activity).finish()
        }, 1000)
    }
}
