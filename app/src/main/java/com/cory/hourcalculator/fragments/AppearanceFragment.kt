package com.cory.hourcalculator.fragments

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlin.math.abs


@DelicateCoroutinesApi
class AppearanceFragment : Fragment() {

    private lateinit var followSystemImageViewDrawable: Drawable
    private lateinit var materialYouDrawable: Drawable
    var themeSelection = false

    private fun setDrawable(): Drawable {
        /*when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> {
                return ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.whitecircleimageview
                )!!
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                return if (FollowSystemThemeChoice(requireContext()).loadFollowSystemThemePreference() == 0) {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.blackcircleimageview
                    )!!
                } else {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.graycircleimageview
                    )!!
                }
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                return if (FollowSystemThemeChoice(requireContext()).loadFollowSystemThemePreference() == 0) {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.blackcircleimageview
                    )!!
                } else {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.graycircleimageview
                    )!!
                }
            }
        }*/

        if (DarkThemeData(requireContext()).loadDarkModeState() == 0) {
            return ContextCompat.getDrawable(
                requireContext(),
                R.drawable.whitecircleimageview
            )!!
        }
        else if (DarkThemeData(requireContext()).loadDarkModeState() == 2) {
            return ContextCompat.getDrawable(
                requireContext(),
                R.drawable.blackcircleimageview
            )!!
        }
        else if (DarkThemeData(requireContext()).loadDarkModeState() == 3) {
            when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    return ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.whitecircleimageview
                    )!!
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    return if (FollowSystemThemeChoice(requireContext()).loadFollowSystemThemePreference() == 0) {
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.blackcircleimageview
                        )!!
                    } else {
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.graycircleimageview
                        )!!
                    }
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    return if (FollowSystemThemeChoice(requireContext()).loadFollowSystemThemePreference() == 0) {
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.blackcircleimageview
                        )!!
                    } else {
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.graycircleimageview
                        )!!
                    }
                }
            }
        }

        return ContextCompat.getDrawable(requireContext(), R.drawable.blackcircleimageview)!!
    }

    private fun materialYouDrawable() : Drawable {

        val darkThemeData = DarkThemeData(requireContext())
        val followSystemVersion = FollowSystemVersion(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 && followSystemVersion.loadSystemColor() -> {
                return ContextCompat.getDrawable(requireContext(), R.drawable.materialyouimageview)!!
            }
            darkThemeData.loadDarkModeState() == 1 && !followSystemVersion.loadSystemColor() -> {
                return ContextCompat.getDrawable(requireContext(), R.drawable.followsystemcircleimageview)!!
            }
            darkThemeData.loadDarkModeState() == 0 && followSystemVersion.loadSystemColor() -> {
                return ContextCompat.getDrawable(requireContext(), R.drawable.materialyouimageview)!!
            }
            darkThemeData.loadDarkModeState() == 0 && !followSystemVersion.loadSystemColor() -> {
                return ContextCompat.getDrawable(requireContext(), R.drawable.followsystemcircleimageview)!!
            }
            darkThemeData.loadDarkModeState() == 2 && followSystemVersion.loadSystemColor() -> {
                return ContextCompat.getDrawable(requireContext(), R.drawable.materialyouimageview)!!
            }
            darkThemeData.loadDarkModeState() == 2 && !followSystemVersion.loadSystemColor() -> {
                return ContextCompat.getDrawable(requireContext(), R.drawable.followsystemcircleimageview)!!
            }
            darkThemeData.loadDarkModeState() == 3 -> {

                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return if (followSystemVersion.loadSystemColor()) {
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.materialyouimageview
                            )!!
                        } else {
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.followsystemcircleimageview
                            )!!
                        }
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return if (followSystemVersion.loadSystemColor()) {
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.materialyouimageview
                            )!!
                        } else {
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.followsystemcircleimageview
                            )!!
                        }
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return if (followSystemVersion.loadSystemColor()) {
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.materialyouimageview
                            )!!
                        } else {
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.followsystemcircleimageview
                            )!!
                        }
                    }
                }
            }
        }

        return ContextCompat.getDrawable(requireContext(), R.drawable.followsystemcircleimageview)!!
    }

    private fun getCurrentNightTheme(): Boolean {

        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> {
                return false
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                return true
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                return true
            }
        }

        return true
    }

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

        followSystemImageViewDrawable = setDrawable()
        materialYouDrawable = materialYouDrawable()

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
        return inflater.inflate(R.layout.fragment_appearance, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarAppearance)

        setColoredMenuItemTint(topAppBar)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val dialog = BottomSheetDialog(requireContext())

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.reset -> {
                    val resetSettingsLayout = layoutInflater.inflate(R.layout.reset_settings_bottom_sheet, null)
                    dialog.setContentView(resetSettingsLayout)
                    dialog.setCancelable(false)
                    resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text = "Would you like to reset Appearance Settings?"
                    /*if (resources.getBoolean(R.bool.isTablet)) {
                        val bottomSheet =
                            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheetBehavior.skipCollapsed = true
                        bottomSheetBehavior.isHideable = false
                        bottomSheetBehavior.isDraggable = false
                    }*/
                    val yesResetButton = resetSettingsLayout.findViewById<Button>(R.id.yesResetButton)
                    val cancelResetButton = resetSettingsLayout.findViewById<Button>(R.id.cancelResetButton)
                    yesResetButton.setOnClickListener {
                        reset()
                        dialog.dismiss()
                    }
                    cancelResetButton.setOnClickListener {
                        Vibrate().vibration(requireContext())
                        dialog.dismiss()
                    }
                    dialog.show()
                    true
                }
                else -> true
            }
        }

        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nestedScrollViewAppearance)
        val appBar = view.findViewById<AppBarLayout>(R.id.appBarLayoutAppearance)

        nestedScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->

            AppearanceScrollPosition(requireContext()).setScroll(scrollX)

        }

        appBar.addOnOffsetChangedListener { _, verticalOffset ->
            if (abs(verticalOffset) - appBar.totalScrollRange == 0) {
                AppearanceScrollPosition(requireContext()).setCollapsed(false)
            } else {
                AppearanceScrollPosition(requireContext()).setCollapsed(true)
            }
        }

        /*val followSystemImageView = activity?.findViewById<ImageView>(R.id.followSystemImageView)
        followSystemImageView?.setImageDrawable(followSystemImageViewDrawable)*/

       // val materialYouImageView = activity?.findViewById<ImageView>(R.id.systemAccentImageView)
        //materialYouImageView?.setImageDrawable(materialYouDrawable)

        val darkThemeData = DarkThemeData(requireContext())

        val themeCardView = requireActivity().findViewById<MaterialCardView>(R.id.themeCardViewAppearance)
        val followSystemCardView = requireActivity().findViewById<MaterialCardView>(R.id.followSystemChoiceCardView)
        val coloredNavigationBarCardView = requireActivity().findViewById<MaterialCardView>(R.id.navbarCardView)
        val coloredMenuTintCardView = requireActivity().findViewById<MaterialCardView>(R.id.coloredMenuTintCardView)

        //val radius = resources.getDimension(R.dimen.my_corner_radius)
        themeCardView.shapeAppearanceModel = themeCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        followSystemCardView.shapeAppearanceModel = followSystemCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        /*accentCardView.shapeAppearanceModel = accentCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()*/
        coloredNavigationBarCardView.shapeAppearanceModel = coloredNavigationBarCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        coloredMenuTintCardView.shapeAppearanceModel = coloredNavigationBarCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val currentBackgroundColorImageView = activity?.findViewById<ImageView>(R.id.currentSelectedTheme)
        currentBackgroundColorImageView?.setImageDrawable(setDrawable())
        val backgroundThemeConstraint = activity?.findViewById<ConstraintLayout>(R.id.backgroundThemeConstraintLayout)
        backgroundThemeConstraint?.setOnClickListener {
            openFragment(BackgroundColorFragment())
        }

        val accentColorConstraintLayout = activity?.findViewById<ConstraintLayout>(R.id.accentColorConstraintLayout)
        accentColorConstraintLayout?.setOnClickListener {
            openFragment(AccentColorFragment())
        }

        /*val lightThemeButton = activity?.findViewById<RadioButton>(R.id.lightTheme)
        val darkThemeButton = activity?.findViewById<RadioButton>(R.id.darkTheme)
        val amoledThemeButton = activity?.findViewById<RadioButton>(R.id.blackTheme)
        val followSystemThemeButton = activity?.findViewById<RadioButton>(R.id.followSystem)

        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                darkThemeButton?.isChecked = true
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                lightThemeButton?.isChecked = true
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                amoledThemeButton?.isChecked = true
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                followSystemThemeButton?.isChecked = true
            }
        }

        lightThemeButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (darkThemeData.loadDarkModeState() == 0) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.light_theme_is_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (FollowSystemVersion(requireContext()).loadSystemColor() && AccentColor(requireContext()).loadAccent() == 4) {
                    /*val alert =
                        MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                    alert.setTitle(getString(R.string.warning))
                    alert.setMessage(getString(R.string.followSystemGoogleDialogWarning))
                    alert.setPositiveButton(getString(R.string.yes)) { _, _ ->*/

                    val dialog = BottomSheetDialog(requireContext())
                    val warningLayout =
                        layoutInflater.inflate(R.layout.match_google_apps_warning_bottom_sheet, null)
                    dialog.setContentView(warningLayout)
                    dialog.setCancelable(true)
                    val yesButton = warningLayout.findViewById<Button>(R.id.yesButton)
                    val noButton = warningLayout.findViewById<Button>(R.id.noButton)
                    yesButton.setOnClickListener {
                        Vibrate().vibration(requireContext())
                        darkThemeData.setDarkModeState(0)
                        darkThemeButton?.isChecked = false
                        amoledThemeButton?.isChecked = false
                        followSystemThemeButton?.isChecked = false
                        dialog.dismiss()
                        restartApplication()
                    }
                    //alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    noButton.setOnClickListener {
                        Vibrate().vibration(requireContext())
                        when {
                            darkThemeData.loadDarkModeState() == 1 -> {
                                darkThemeButton?.isChecked = true
                            }
                            darkThemeData.loadDarkModeState() == 2 -> {
                                amoledThemeButton?.isChecked = true
                            }
                            darkThemeData.loadDarkModeState() == 3 -> {
                                followSystemThemeButton?.isChecked = true
                            }
                        }
                        lightThemeButton.isChecked = false
                        dialog.dismiss()
                    }
                    //alert.show()
                    dialog.show()
                }
                else {
                    darkThemeData.setDarkModeState(0)
                    darkThemeButton?.isChecked = false
                    amoledThemeButton?.isChecked = false
                    followSystemThemeButton?.isChecked = false
                    restartThemeChange()
                }
            }
        }
        darkThemeButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (darkThemeData.loadDarkModeState() == 1) {
                Toast.makeText(requireContext(), getString(R.string.dark_theme_already_enabled), Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (FollowSystemVersion(requireContext()).loadSystemColor() && AccentColor(requireContext()).loadAccent() == 4) {
                    /*val alert =
                        MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                    alert.setTitle(getString(R.string.warning))
                    alert.setMessage(getString(R.string.followSystemGoogleDialogWarning))
                    alert.setPositiveButton(getString(R.string.yes)) { _, _ ->*/
                    val dialog = BottomSheetDialog(requireContext())
                    val warningLayout =
                        layoutInflater.inflate(R.layout.match_google_apps_warning_bottom_sheet, null)
                    dialog.setContentView(warningLayout)
                    dialog.setCancelable(true)
                    val yesButton = warningLayout.findViewById<Button>(R.id.yesButton)
                    val noButton = warningLayout.findViewById<Button>(R.id.noButton)
                    yesButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                        darkThemeData.setDarkModeState(1)
                        lightThemeButton?.isChecked = false
                        amoledThemeButton?.isChecked = false
                        followSystemThemeButton?.isChecked = false
                        dialog.dismiss()
                        restartApplication()
                    }
                    //alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    noButton.setOnClickListener {
                        Vibrate().vibration(requireContext())
                        when {
                            darkThemeData.loadDarkModeState() == 0 -> {
                                lightThemeButton?.isChecked = true
                            }
                            darkThemeData.loadDarkModeState() == 2 -> {
                                amoledThemeButton?.isChecked = true
                            }
                            darkThemeData.loadDarkModeState() == 3 -> {
                                followSystemThemeButton?.isChecked = true
                            }
                        }
                        darkThemeButton.isChecked = false
                        dialog.dismiss()
                    }
                    dialog.show()
                    //alert.show()
                }
                else {
                    darkThemeData.setDarkModeState(1)
                    lightThemeButton?.isChecked = false
                    amoledThemeButton?.isChecked = false
                    followSystemThemeButton?.isChecked = false
                    restartThemeChange()
                }
            }
        }
        amoledThemeButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (darkThemeData.loadDarkModeState() == 2) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.black_theme_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (FollowSystemVersion(requireContext()).loadSystemColor() && AccentColor(requireContext()).loadAccent() == 4) {
                    /*val alert =
                        MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                    alert.setTitle(getString(R.string.warning))
                    alert.setMessage(getString(R.string.followSystemGoogleDialogWarning))
                    alert.setPositiveButton(getString(R.string.yes)) { _, _ ->*/
                    val dialog = BottomSheetDialog(requireContext())
                    val warningLayout =
                        layoutInflater.inflate(R.layout.match_google_apps_warning_bottom_sheet, null)
                    dialog.setContentView(warningLayout)
                    dialog.setCancelable(true)
                    val yesButton = warningLayout.findViewById<Button>(R.id.yesButton)
                    val noButton = warningLayout.findViewById<Button>(R.id.noButton)
                    yesButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                        darkThemeData.setDarkModeState(2)
                        lightThemeButton?.isChecked = false
                        darkThemeButton?.isChecked = false
                        followSystemThemeButton?.isChecked = false
                        dialog.dismiss()
                        restartApplication()
                    }
                    //alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    noButton.setOnClickListener {
                        Vibrate().vibration(requireContext())
                        when {
                            darkThemeData.loadDarkModeState() == 0 -> {
                                lightThemeButton?.isChecked = true
                            }
                            darkThemeData.loadDarkModeState() == 1 -> {
                                darkThemeButton?.isChecked = true
                            }
                            darkThemeData.loadDarkModeState() == 3 -> {
                                followSystemThemeButton?.isChecked = true
                            }
                        }
                        amoledThemeButton.isChecked = false
                        dialog.dismiss()
                    }
                    //alert.show()
                    dialog.show()
                }
                else {
                    darkThemeData.setDarkModeState(2)
                    lightThemeButton?.isChecked = false
                    darkThemeButton?.isChecked = false
                    followSystemThemeButton?.isChecked = false
                    restartThemeChange()
                }
            }
        }
        followSystemThemeButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (darkThemeData.loadDarkModeState() == 3) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.system_theme_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (FollowSystemVersion(requireContext()).loadSystemColor() && AccentColor(requireContext()).loadAccent() == 4) {
                    /*val alert =
                        MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                    alert.setTitle(getString(R.string.warning))
                    alert.setMessage(getString(R.string.followSystemGoogleDialogWarning))
                    alert.setPositiveButton(getString(R.string.yes)) { _, _ ->*/
                    val dialog = BottomSheetDialog(requireContext())
                    val warningLayout =
                        layoutInflater.inflate(R.layout.match_google_apps_warning_bottom_sheet, null)
                    dialog.setContentView(warningLayout)
                    dialog.setCancelable(true)
                    val yesButton = warningLayout.findViewById<Button>(R.id.yesButton)
                    val noButton = warningLayout.findViewById<Button>(R.id.noButton)
                    yesButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                        darkThemeData.setDarkModeState(3)
                        lightThemeButton?.isChecked = false
                        darkThemeButton?.isChecked = false
                        amoledThemeButton?.isChecked = false
                        dialog.dismiss()
                        restartThemeChange()
                    }
                   // alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    noButton.setOnClickListener {
                        Vibrate().vibration(requireContext())
                        when {
                            darkThemeData.loadDarkModeState() == 0 -> {
                                lightThemeButton?.isChecked = true
                            }
                            darkThemeData.loadDarkModeState() == 1 -> {
                                darkThemeButton?.isChecked = true
                            }
                            darkThemeData.loadDarkModeState() == 2 -> {
                                amoledThemeButton?.isChecked = true
                            }
                        }
                        followSystemThemeButton.isChecked = false
                        dialog.dismiss()
                    }
                    //alert.show()
                    dialog.show()
                }
                else {
                    darkThemeData.setDarkModeState(3)
                    lightThemeButton?.isChecked = false
                    darkThemeButton?.isChecked = false
                    amoledThemeButton?.isChecked = false
                    restartThemeChange()
                }

            }
        }*/

        val followSystemThemeChoice = FollowSystemThemeChoice(requireContext())

       /* val blackTheme = activity?.findViewById<RadioButton>(R.id.amoledSystemTheme)
        val darkTheme = activity?.findViewById<RadioButton>(R.id.darkThemeSystem)

        if (followSystemThemeChoice.loadFollowSystemThemePreference() == 0) {
            blackTheme?.isChecked = true
        }
        else {
            darkTheme?.isChecked = true
        }

        blackTheme?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (followSystemThemeChoice.loadFollowSystemThemePreference() == 0) {
                Toast.makeText(requireContext(), getString(R.string.follow_system_already_set_to_black_theme), Toast.LENGTH_SHORT).show()
            }
            else {
                if (FollowSystemVersion(requireContext()).loadSystemColor() && AccentColor(requireContext()).loadAccent() == 4) {
                    val alert =
                        MaterialAlertDialogBuilder(
                            requireContext(),
                            AccentColor(requireContext()).alertTheme()
                        )
                    alert.setTitle(getString(R.string.warning))
                    alert.setMessage(getString(R.string.followSystemGoogleDialogWarning))
                    alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                        Vibrate().vibration(requireContext())
                        followSystemThemeChoice.setFollowSystemThemePreference(0)
                        darkTheme?.isChecked = false

                        if (darkThemeData.loadDarkModeState() == 3) {
                            if (getCurrentNightTheme()) {
                                restartThemeChange()
                            }
                        }
                    }
                    alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                        Vibrate().vibration(requireContext())
                        blackTheme.isChecked = false
                    }
                alert.show()
                }
                else {
                    followSystemThemeChoice.setFollowSystemThemePreference(0)
                    darkTheme?.isChecked = false

                    if (darkThemeData.loadDarkModeState() == 3) {
                        if (getCurrentNightTheme()) {
                            restartThemeChange()
                        }
                    }
                }
            }

            //followSystemImageView?.setImageDrawable(setDrawable())
        }

        darkTheme?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (FollowSystemVersion(requireContext()).loadSystemColor() && AccentColor(requireContext()).loadAccent() == 4) {
                val alert =
                    MaterialAlertDialogBuilder(
                        requireContext(),
                        AccentColor(requireContext()).alertTheme()
                    )
                alert.setTitle(getString(R.string.warning))
                alert.setMessage(getString(R.string.followSystemGoogleDialogWarning))
                alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    followSystemThemeChoice.setFollowSystemThemePreference(1)
                    blackTheme?.isChecked = false

                    if (darkThemeData.loadDarkModeState() == 3) {
                        if (getCurrentNightTheme()) {
                            restartThemeChange()
                        }
                    }
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    darkTheme.isChecked = false
                }
                alert.show()
            }
            else {
                followSystemThemeChoice.setFollowSystemThemePreference(1)
                blackTheme?.isChecked = false

                if (darkThemeData.loadDarkModeState() == 3) {
                    if (getCurrentNightTheme()) {
                        restartThemeChange()
                    }
                }
            }
            //followSystemImageView?.setImageDrawable(setDrawable())
        }*/

        val accentColor = AccentColor(requireContext())

        /*val tealAccentButton = activity?.findViewById<RadioButton>(R.id.Teal)
        val pinkAccentButton = activity?.findViewById<RadioButton>(R.id.Pink)
        val orangeAccentButton = activity?.findViewById<RadioButton>(R.id.Orange)
        val redAccentButton = activity?.findViewById<RadioButton>(R.id.Red)
        val systemAccentButton = activity?.findViewById<RadioButton>(R.id.systemAccent)

        val systemAccentImage = activity?.findViewById<ImageView>(R.id.systemAccentImageView)

        if (Build.VERSION.SDK_INT < 31) {
            systemAccentButton?.visibility = View.GONE
            systemAccentImage?.visibility = View.GONE
        }

        when {
            accentColor.loadAccent() == 0 -> {
                tealAccentButton?.isChecked = true
            }
            accentColor.loadAccent() == 1 -> {
                pinkAccentButton?.isChecked = true
            }
            accentColor.loadAccent() == 2 -> {
                orangeAccentButton?.isChecked = true
            }
            accentColor.loadAccent() == 3 -> {
                redAccentButton?.isChecked = true
            }
            accentColor.loadAccent() == 4 -> {
                systemAccentButton?.isChecked = true
            }
        }

        tealAccentButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 0) {
                Toast.makeText(requireContext(), getString(R.string.teal_is_already_chosen), Toast.LENGTH_SHORT).show()
            } else {
                val dialog = BottomSheetDialog(requireContext())
                val restartAppLayout = layoutInflater.inflate(R.layout.restart_app_warning_bottom_sheet, null)
                dialog.setContentView(restartAppLayout)
                dialog.setCancelable(false)
                restartAppLayout.findViewById<Button>(R.id.yesButton).setOnClickListener {
                    Vibrate().vibration(requireContext())

                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 0) {
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashOrange"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashRed"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashPink"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashScreenNoIcon"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                    }
                    dialog.dismiss()
                    accentColor.setAccentState(0)
                    restartApplication()
                }
                restartAppLayout.findViewById<Button>(R.id.noButton).setOnClickListener {
                Vibrate().vibration(requireContext())
                    tealAccentButton.isChecked = false
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        pinkAccentButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 1) {
                Toast.makeText(requireContext(), getString(R.string.pink_is_already_chosen), Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(
                    requireContext(),
                    AccentColor(requireContext()).alertTheme()
                )
                alert.setTitle(getString(R.string.warning))
                alert.setMessage(getString(R.string.restart_warning))
                alert.setCancelable(false)
                alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    Vibrate().vibration(requireContext())
                   if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 0) {
                       activity?.packageManager?.setComponentEnabledSetting(
                           ComponentName(
                               BuildConfig.APPLICATION_ID,
                               "com.cory.hourcalculator.SplashOrange"
                           ),
                           PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                           PackageManager.DONT_KILL_APP
                       )
                       activity?.packageManager?.setComponentEnabledSetting(
                           ComponentName(
                               BuildConfig.APPLICATION_ID,
                               "com.cory.hourcalculator.SplashRed"
                           ),
                           PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                           PackageManager.DONT_KILL_APP
                       )
                       activity?.packageManager?.setComponentEnabledSetting(
                           ComponentName(
                               BuildConfig.APPLICATION_ID,
                               "com.cory.hourcalculator.SplashPink"
                           ),
                           PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                           PackageManager.DONT_KILL_APP
                       )
                       activity?.packageManager?.setComponentEnabledSetting(
                           ComponentName(
                               BuildConfig.APPLICATION_ID,
                               "com.cory.hourcalculator.SplashScreenNoIcon"
                           ),
                           PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                           PackageManager.DONT_KILL_APP
                       )
                   }
                    accentColor.setAccentState(1)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    pinkAccentButton.isChecked = false
                }
                alert.show()
            }
        }
        orangeAccentButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 2) {
                Toast.makeText(requireContext(), getString(R.string.orange_is_already_chosen), Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(
                    requireContext(),
                    AccentColor(requireContext()).alertTheme()
                )
                alert.setTitle(getString(R.string.warning))
                alert.setMessage(getString(R.string.restart_warning))
                alert.setCancelable(false)
                alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 0) {
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashOrange"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashRed"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashPink"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashScreenNoIcon"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                    }
                    accentColor.setAccentState(2)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    orangeAccentButton.isChecked = false
                }
                alert.show()
            }
        }
        redAccentButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 3) {
                Toast.makeText(requireContext(), getString(R.string.red_already_chosen), Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(
                    requireContext(),
                    AccentColor(requireContext()).alertTheme()
                )
                alert.setTitle(getString(R.string.warning))
                alert.setMessage(getString(R.string.restart_warning))
                alert.setCancelable(false)
                alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 0) {
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashOrange"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashRed"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashPink"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashScreenNoIcon"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                    }
                    accentColor.setAccentState(3)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    redAccentButton.isChecked = false
                }
                alert.show()
            }
        }
        systemAccentButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 4) {
                Toast.makeText(requireContext(), getString(R.string.system_accent_color_is_already_enabled), Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(
                    requireContext(),
                    AccentColor(requireContext()).alertTheme()
                )
                alert.setTitle(getString(R.string.warning_experimental))
                alert.setMessage(getString(R.string.system_accent_warning))
                alert.setCancelable(false)
                alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 0) {
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashOrange"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashRed"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashPink"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashScreenNoIcon"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                    }
                    accentColor.setAccentState(4)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    systemAccentButton.isChecked = false
                }
                alert.show()
            }
        }*/

        val coloredNavBarSwitch = view.findViewById<MaterialSwitch>(R.id.coloredNavBarSwitch)
        val coloredNavBarData = ColoredNavBarData(requireContext())
        val coloredNavBarConstraintLayout = view.findViewById<ConstraintLayout>(R.id.coloredNavBarConstraintLayout)

        coloredNavBarSwitch.isChecked = coloredNavBarData.loadNavBar()

        if (coloredNavBarData.loadNavBar()) {
            coloredNavBarSwitch?.isChecked = true
            coloredNavBarSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!coloredNavBarData.loadNavBar()) {
            coloredNavBarSwitch?.isChecked = false
            coloredNavBarSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        coloredNavBarConstraintLayout.setOnClickListener {
            coloredNavBarSwitch.isChecked = !coloredNavBarSwitch.isChecked
            toggleColoredNavBar(coloredNavBarSwitch.isChecked, coloredNavBarData, accentColor)
        }

        coloredNavBarSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            toggleColoredNavBar(isChecked, coloredNavBarData, accentColor)
        }

        val coloredMenuTintSwitch = view.findViewById<MaterialSwitch>(R.id.coloredMenuTintSwitch)
        val coloredMenuTintData = MenuTintData(requireContext())
        val coloredMenuTintConstraintLayout = view.findViewById<ConstraintLayout>(R.id.coloredMenuTintConstraintLayout)

        coloredMenuTintSwitch.isChecked = coloredMenuTintData.loadMenuTint()

        if (coloredMenuTintData.loadMenuTint()) {
            coloredMenuTintSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else {
            coloredMenuTintSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        coloredMenuTintConstraintLayout.setOnClickListener {
            Vibrate().vibration(requireContext())
            coloredMenuTintSwitch.isChecked = !coloredMenuTintSwitch.isChecked
            coloredMenuTintData.setColoredMenuTint(coloredMenuTintSwitch.isChecked)
            if (coloredMenuTintSwitch.isChecked) {
                coloredMenuTintSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            }
            else {
                coloredMenuTintSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            setColoredMenuItemTint(topAppBar)
        }

        coloredMenuTintSwitch.setOnClickListener {
            Vibrate().vibration(requireContext())
            coloredMenuTintData.setColoredMenuTint(coloredMenuTintSwitch.isChecked)
            if (coloredMenuTintSwitch.isChecked) {
                coloredMenuTintSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            }
            else {
                coloredMenuTintSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            setColoredMenuItemTint(topAppBar)
        }

        /*enableColoredNavBar.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (coloredNavBarData.loadNavBar()) {
                Toast.makeText(requireContext(), getString(R.string.colored_nav_bar_is_already_enabled), Toast.LENGTH_SHORT).show()
            } else {
                disableColoredNavBar.isChecked = false
                Vibrate().vibration(requireContext())

                coloredNavBarData.setNavBar(true)

                    when {
                        accentColor.loadAccent() == 0 -> {
                            activity?.window?.navigationBarColor =
                                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                        }
                        accentColor.loadAccent() == 1 -> {
                            activity?.window?.navigationBarColor =
                                ContextCompat.getColor(requireContext(), R.color.pinkAccent)
                        }
                        accentColor.loadAccent() == 2 -> {
                            activity?.window?.navigationBarColor =
                                ContextCompat.getColor(requireContext(), R.color.orangeAccent)
                        }
                        accentColor.loadAccent() == 3 -> {
                            activity?.window?.navigationBarColor =
                                ContextCompat.getColor(requireContext(), R.color.redAccent)
                        }
                        accentColor.loadAccent() == 4 -> {
                            if (!FollowSystemVersion(requireContext()).loadSystemColor()) {
                                activity?.window?.navigationBarColor =
                                    ContextCompat.getColor(requireContext(), R.color.systemAccent)
                            }
                            else {
                                if (themeSelection) {
                                    activity?.window?.navigationBarColor =
                                        ContextCompat.getColor(requireContext(), R.color.navBarGoogle)
                                }
                                else {
                                    activity?.window?.navigationBarColor =
                                        ContextCompat.getColor(requireContext(), R.color.navBarGoogleLight)
                                }
                            }
                        }
                    }

                    Toast.makeText(requireContext(), getString(R.string.colored_nav_bar_enabled), Toast.LENGTH_SHORT).show()
                }
            }*/

        /*disableColoredNavBar.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (!coloredNavBarData.loadNavBar()) {
                Toast.makeText(requireContext(), getString(R.string.colored_navigation_bar_is_already_disabled), Toast.LENGTH_SHORT).show()
            } else {
                enableColoredNavBar.isChecked = false
                activity?.window?.navigationBarColor =
                    ContextCompat.getColor(requireContext(), R.color.black)
                coloredNavBarData.setNavBar(false)

                Toast.makeText(requireContext(), getString(R.string.colored_nav_bar_disabled), Toast.LENGTH_SHORT).show()
            }
        }*/

        val autoIconCheckBox = view.findViewById<RadioButton>(R.id.autoIconRadioButton)
        val tealIconCheckBox = view.findViewById<RadioButton>(R.id.tealIconRadioButton)
        val pinkIconCheckBox = view.findViewById<RadioButton>(R.id.pinkIconRadioButton)
        val orangeIconCheckBox = view.findViewById<RadioButton>(R.id.orangeIconRadioButton)
        val redIconCheckBox = view.findViewById<RadioButton>(R.id.redIconRadioButton)

        val autoIconCardView = view.findViewById<CardView>(R.id.autoCardView)
        val tealIconCardView = view.findViewById<CardView>(R.id.tealCardView)
        val pinkIconCardView = view.findViewById<CardView>(R.id.pinkCardView)
        val orangeIconCardView = view.findViewById<CardView>(R.id.orangeCardView)
        val redIconCardView = view.findViewById<CardView>(R.id.redCardView)

        val autoIcon = view.findViewById<ImageView>(R.id.autoIconImageView)

        if (accentColor.loadAccent() == 0 || accentColor.loadAccent() == 4) {
            autoIcon.setImageResource(R.drawable.hourcalclogoteal)
        }
        else if (accentColor.loadAccent() == 1) {
            autoIcon.setImageResource(R.drawable.hourcalclogopink)
        }
        else if (accentColor.loadAccent() == 2) {
            autoIcon.setImageResource(R.drawable.hourcalclogoorange)
        }
        else if (accentColor.loadAccent() == 3) {
            autoIcon.setImageResource(R.drawable.hourcalclogored)
        }

        val chosenAppIcon = ChosenAppIconData(requireContext())

        if (chosenAppIcon.loadChosenAppIcon() == 0) {
            autoIconCheckBox.isChecked = true
        }
        else if (chosenAppIcon.loadChosenAppIcon() == 1) {
            tealIconCheckBox.isChecked = true
        }
        else if (chosenAppIcon.loadChosenAppIcon() == 2) {
            pinkIconCheckBox.isChecked = true
        }
        else if (chosenAppIcon.loadChosenAppIcon() == 3) {
            orangeIconCheckBox.isChecked = true
        }
        else if (chosenAppIcon.loadChosenAppIcon() == 4) {
            redIconCheckBox.isChecked = true
        }

        autoIconCheckBox.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleAutomaticIcon(chosenAppIcon, accentColor, autoIconCheckBox)
        }

        autoIconCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleAutomaticIcon(chosenAppIcon, accentColor, autoIconCheckBox)
        }

        tealIconCheckBox.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleTealIcon(chosenAppIcon, tealIconCheckBox)
        }

        tealIconCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleTealIcon(chosenAppIcon, tealIconCheckBox)
        }

        pinkIconCheckBox.setOnClickListener {
            Vibrate().vibration(requireContext())
            togglePinkIcon(chosenAppIcon, pinkIconCheckBox)
        }

        pinkIconCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            togglePinkIcon(chosenAppIcon, pinkIconCheckBox)
        }

        orangeIconCheckBox.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleOrangeIcon(chosenAppIcon, orangeIconCheckBox)
        }

        orangeIconCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleOrangeIcon(chosenAppIcon, orangeIconCheckBox)
        }

        redIconCheckBox.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleRedIcon(chosenAppIcon, redIconCheckBox)
        }

        redIconCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleRedIcon(chosenAppIcon, redIconCheckBox)
        }

        /*val normalStyle = view.findViewById<RadioButton>(R.id.normalSystemTheme)
        val googleStyle = view.findViewById<RadioButton>(R.id.googleSystemTheme)
        val followSystemVersion = FollowSystemVersion(requireContext())

        val followSystemVersionCardView = view.findViewById<CardView>(R.id.followSystemVersionCardView)
        val followSystemVersionTextView = view.findViewById<TextView>(R.id.followSystemVersionTextView)

        if (Build.VERSION.SDK_INT < 31) {
            followSystemVersionCardView?.visibility = View.GONE
            followSystemVersionTextView?.visibility = View.GONE
        }

        if (!followSystemVersion.loadSystemColor()) {
            normalStyle.isChecked = true
        } else {
            googleStyle.isChecked = true
        }

        normalStyle.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (followSystemVersion.loadSystemColor()) {
                if (accentColor.loadAccent() == 4) {
                    val alert = MaterialAlertDialogBuilder(
                        requireContext(),
                        AccentColor(requireContext()).alertTheme()
                    )
                    alert.setTitle(requireContext().getString(R.string.warning_experimental))
                    alert.setMessage(getString(R.string.may_produce_unwanted_results))
                    alert.setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                        Vibrate().vibration(requireContext())
                        followSystemVersion.setSystemColor(false)
                        restartApplication()
                    }
                    alert.setNegativeButton(requireContext().getString(R.string.no)) { _, _ ->
                        Vibrate().vibration(requireContext())
                        normalStyle.isChecked = false
                    }
                    alert.show()
                } else {
                    googleStyle.isChecked = false
                    followSystemVersion.setSystemColor(false)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.app_theme_will_now_match_wallpaper),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.follow_app_theming_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        googleStyle.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (!followSystemVersion.loadSystemColor()) {
                if (accentColor.loadAccent() == 4) {
                    val alert = MaterialAlertDialogBuilder(
                        requireContext(),
                        AccentColor(requireContext()).alertTheme()
                    )
                    alert.setTitle(requireContext().getString(R.string.warning_experimental))
                    alert.setMessage(getString(R.string.this_option_may_require_the_app_to_restart_desc))
                    alert.setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                        Vibrate().vibration(requireContext())
                        followSystemVersion.setSystemColor(true)
                        restartApplication()
                    }
                    alert.setNegativeButton(requireContext().getString(R.string.no)) { _, _ ->
                        Vibrate().vibration(requireContext())
                        googleStyle.isChecked = false
                    }
                    alert.show()
                } else {
                    normalStyle.isChecked = false
                    followSystemVersion.setSystemColor(true)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.app_will_now_match_google_apps),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.follow_google_apps_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }*/

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.supportFragmentManager?.popBackStack()
                }
            })
    }

    private fun setColoredMenuItemTint(topAppBar: MaterialToolbar?) {
        val navigationDrawable = topAppBar?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.colorFilter = BlendModeColorFilter(
                ContextCompat.getColor(requireContext(), id),
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
    }

    private fun toggleColoredNavBar(
        isChecked: Boolean,
        coloredNavBarData: ColoredNavBarData,
        accentColor: AccentColor
    ) {
        Vibrate().vibration(requireContext())
        if (isChecked) {
            activity?.findViewById<MaterialSwitch>(R.id.coloredNavBarSwitch)?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            coloredNavBarData.setNavBar(true)

            when {
                accentColor.loadAccent() == 0 -> {
                    activity?.window?.navigationBarColor =
                        ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                }
                accentColor.loadAccent() == 1 -> {
                    activity?.window?.navigationBarColor =
                        ContextCompat.getColor(requireContext(), R.color.pinkAccent)
                }
                accentColor.loadAccent() == 2 -> {
                    activity?.window?.navigationBarColor =
                        ContextCompat.getColor(requireContext(), R.color.orangeAccent)
                }
                accentColor.loadAccent() == 3 -> {
                    activity?.window?.navigationBarColor =
                        ContextCompat.getColor(requireContext(), R.color.redAccent)
                }
                accentColor.loadAccent() == 4 -> {
                    if (!FollowSystemVersion(requireContext()).loadSystemColor()) {
                        activity?.window?.navigationBarColor =
                            ContextCompat.getColor(requireContext(), R.color.systemAccent)
                    } else {
                        if (themeSelection) {
                            activity?.window?.navigationBarColor =
                                ContextCompat.getColor(requireContext(), R.color.navBarGoogle)
                        } else {
                            activity?.window?.navigationBarColor =
                                ContextCompat.getColor(requireContext(), R.color.navBarGoogleLight)
                        }
                    }
                }
            }

            Toast.makeText(
                requireContext(),
                getString(R.string.colored_nav_bar_enabled),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            activity?.findViewById<MaterialSwitch>(R.id.coloredNavBarSwitch)?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            activity?.window?.navigationBarColor =
                ContextCompat.getColor(requireContext(), R.color.black)
            coloredNavBarData.setNavBar(false)

            Toast.makeText(
                requireContext(),
                getString(R.string.colored_nav_bar_disabled),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun toggleTealIcon(
        chosenAppIcon: ChosenAppIconData,
        tealIconCheckBox: RadioButton
    ) {
        if (chosenAppIcon.loadChosenAppIcon() == 1) {
            Toast.makeText(
                requireContext(),
                getString(R.string.teal_icon_already_enabled),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            tealIconCheckBox.isChecked = true
            val alert = MaterialAlertDialogBuilder(
                requireContext(),
                AccentColor(requireContext()).alertTheme()
            )
            alert.setTitle(requireContext().getString(R.string.warning))
            alert.setMessage(getString(R.string.change_app_icon_warning))
            alert.setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                Vibrate().vibration(requireContext())
                chosenAppIcon.setChosenAppIcon(1)
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashOrange"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashRed"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashPink"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashScreenNoIcon"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.MaterialYou"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                restartApplication()
            }
            alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                Vibrate().vibration(requireContext())
                tealIconCheckBox.isChecked = false
            }
            alert.show()
        }
    }

    private fun togglePinkIcon(
        chosenAppIcon: ChosenAppIconData,
        pinkIconCheckBox: RadioButton
    ) {
        if (chosenAppIcon.loadChosenAppIcon() == 2) {
            Toast.makeText(
                requireContext(),
                getString(R.string.pink_icon_already_enabled),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            pinkIconCheckBox.isChecked = true
            val alert = MaterialAlertDialogBuilder(
                requireContext(),
                AccentColor(requireContext()).alertTheme()
            )
            alert.setTitle(requireContext().getString(R.string.warning))
            alert.setMessage(getString(R.string.change_app_icon_warning))
            alert.setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                Vibrate().vibration(requireContext())
                chosenAppIcon.setChosenAppIcon(2)
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashOrange"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashRed"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashPink"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashScreenNoIcon"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.MaterialYou"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                restartApplication()
            }
            alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                Vibrate().vibration(requireContext())
                pinkIconCheckBox.isChecked = false
            }
            alert.show()
        }
    }

    private fun toggleOrangeIcon(
        chosenAppIcon: ChosenAppIconData,
        orangeIconCheckBox: RadioButton
    ) {
        if (chosenAppIcon.loadChosenAppIcon() == 3) {
            Toast.makeText(
                requireContext(),
                getString(R.string.orange_icon_already_enabled),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            orangeIconCheckBox.isChecked = true
            val alert = MaterialAlertDialogBuilder(
                requireContext(),
                AccentColor(requireContext()).alertTheme()
            )
            alert.setTitle(requireContext().getString(R.string.warning))
            alert.setMessage(getString(R.string.change_app_icon_warning))
            alert.setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                Vibrate().vibration(requireContext())
                chosenAppIcon.setChosenAppIcon(3)
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashOrange"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashRed"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashPink"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashScreenNoIcon"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.MaterialYou"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                restartApplication()
            }
            alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                Vibrate().vibration(requireContext())
                orangeIconCheckBox.isChecked = false
            }
            alert.show()
        }
    }

    private fun toggleRedIcon(
        chosenAppIcon: ChosenAppIconData,
        redIconCheckBox: RadioButton
    ) {
        if (chosenAppIcon.loadChosenAppIcon() == 4) {
            Toast.makeText(
                requireContext(),
                getString(R.string.red_icon_already_enabled),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            redIconCheckBox.isChecked = true
            val alert = MaterialAlertDialogBuilder(
                requireContext(),
                AccentColor(requireContext()).alertTheme()
            )
            alert.setTitle(requireContext().getString(R.string.warning))
            alert.setMessage(getString(R.string.change_app_icon_warning))
            alert.setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                Vibrate().vibration(requireContext())
                chosenAppIcon.setChosenAppIcon(4)
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashOrange"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashRed"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashPink"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.SplashScreenNoIcon"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                activity?.packageManager?.setComponentEnabledSetting(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        "com.cory.hourcalculator.MaterialYou"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                restartApplication()
            }
            alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                Vibrate().vibration(requireContext())
                redIconCheckBox.isChecked = false
            }
            alert.show()
        }
    }

    private fun toggleAutomaticIcon(
        chosenAppIcon: ChosenAppIconData,
        accentColor: AccentColor,
        autoIconCheckBox: RadioButton
    ) {
        if (chosenAppIcon.loadChosenAppIcon() == 0) {
            Toast.makeText(
                requireContext(),
                getString(R.string.automatic_icon_already_enabled),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            autoIconCheckBox.isChecked = true
            val alert = MaterialAlertDialogBuilder(
                requireContext(),
                AccentColor(requireContext()).alertTheme()
            )
            alert.setTitle(requireContext().getString(R.string.warning))
            alert.setMessage(getString(R.string.change_app_icon_warning))
            alert.setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                Vibrate().vibration(requireContext())
                chosenAppIcon.setChosenAppIcon(0)

                if (accentColor.loadAccent() == 0) {
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashOrange"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashRed"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashPink"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashScreenNoIcon"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.MaterialYou"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    restartApplication()
                } else if (accentColor.loadAccent() == 1) {
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashOrange"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashRed"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashPink"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashScreenNoIcon"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.MaterialYou"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    restartApplication()
                } else if (accentColor.loadAccent() == 2) {
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashOrange"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashRed"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashPink"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashScreenNoIcon"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.MaterialYou"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    restartApplication()
                } else if (accentColor.loadAccent() == 3) {
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashOrange"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashRed"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashPink"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashScreenNoIcon"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.MaterialYou"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    restartApplication()
                } else if (accentColor.loadAccent() == 4) {
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashOrange"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashRed"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashPink"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashScreenNoIcon"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.MaterialYou"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    restartApplication()
                }
            }
            alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                Vibrate().vibration(requireContext())
                autoIconCheckBox.isChecked = false
            }
            alert.show()
        }
    }

    private fun restartThemeChange() {

        val runnable = Runnable {
            (context as MainActivity).setBackgroundColor()
        }
        MainActivity().runOnUiThread(runnable)

        activity?.supportFragmentManager?.beginTransaction()
            ?.detach(this)?.commitNow()
        activity?.supportFragmentManager?.beginTransaction()
            ?.attach(this)?.commitNow()

        view?.findViewById<NestedScrollView>(R.id.nestedScrollViewAppearance)
            ?.scrollTo(0, AppearanceScrollPosition(requireContext()).loadScroll())

        val collapsingToolbarLayout =
            requireView().findViewById<AppBarLayout>(R.id.appBarLayoutAppearance)

        collapsingToolbarLayout.setExpanded(
            AppearanceScrollPosition(requireContext()).loadCollapsed(),
            false
        )

    }

    private fun reset() {

            Vibrate().vibration(requireContext())
            // if (Build.VERSION.SDK_INT < 31) {
            when {
                AccentColor(requireContext()).loadAccent() != 0 -> {
                    AccentColor(requireContext()).setAccentState(0)
                    DarkThemeData(requireContext()).setDarkModeState(3)
                    restartApplication()
                }
                DarkThemeData(requireContext()).loadDarkModeState() != 3 -> {
                    //view?.findViewById<RadioButton>(R.id.lightTheme)?.isChecked = false
                    //view?.findViewById<RadioButton>(R.id.darkTheme)?.isChecked = false
                    //view?.findViewById<RadioButton>(R.id.blackTheme)?.isChecked = false
                    //view?.findViewById<RadioButton>(R.id.followSystem)?.isChecked = true
                    AccentColor(requireContext()).setAccentState(0)
                    DarkThemeData(requireContext()).setDarkModeState(3)
                    restartThemeChange()
                }
                else -> {
                    AccentColor(requireContext()).setAccentState(0)
                    DarkThemeData(requireContext()).setDarkModeState(3)
                }
            }
            ChosenAppIconData(requireContext()).setChosenAppIcon(0)
            activity?.packageManager?.setComponentEnabledSetting(
                ComponentName(
                    BuildConfig.APPLICATION_ID,
                    "com.cory.hourcalculator.SplashOrange"
                ),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            activity?.packageManager?.setComponentEnabledSetting(
                ComponentName(
                    BuildConfig.APPLICATION_ID,
                    "com.cory.hourcalculator.SplashRed"
                ),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            activity?.packageManager?.setComponentEnabledSetting(
                ComponentName(
                    BuildConfig.APPLICATION_ID,
                    "com.cory.hourcalculator.SplashPink"
                ),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            activity?.packageManager?.setComponentEnabledSetting(
                ComponentName(
                    BuildConfig.APPLICATION_ID,
                    "com.cory.hourcalculator.SplashScreenNoIcon"
                ),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
            )
            /* }
             else if (Build.VERSION.SDK_INT >= 31) {
                 when {
                     AccentColor(requireContext()).loadAccent() != 4 -> {
                         activity?.packageManager?.setComponentEnabledSetting(
                             ComponentName(
                                 BuildConfig.APPLICATION_ID,
                                 "com.cory.hourcalculator.SplashOrange"
                             ),
                             PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                             PackageManager.DONT_KILL_APP
                         )
                         activity?.packageManager?.setComponentEnabledSetting(
                             ComponentName(
                                 BuildConfig.APPLICATION_ID,
                                 "com.cory.hourcalculator.SplashRed"
                             ),
                             PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                             PackageManager.DONT_KILL_APP
                         )
                         activity?.packageManager?.setComponentEnabledSetting(
                             ComponentName(
                                 BuildConfig.APPLICATION_ID,
                                 "com.cory.hourcalculator.SplashPink"
                             ),
                             PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                             PackageManager.DONT_KILL_APP
                         )
                         activity?.packageManager?.setComponentEnabledSetting(
                             ComponentName(
                                 BuildConfig.APPLICATION_ID,
                                 "com.cory.hourcalculator.SplashScreenNoIcon"
                             ),
                             PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                             PackageManager.DONT_KILL_APP
                         )
                         AccentColor(requireContext()).setAccentState(4)
                         DarkThemeData(requireContext()).setDarkModeState(3)
                         restartApplication()
                     }
                     DarkThemeData(requireContext()).loadDarkModeState() != 3 -> {
                         view?.findViewById<RadioButton>(R.id.lightTheme)?.isChecked = false
                         view?.findViewById<RadioButton>(R.id.darkTheme)?.isChecked = false
                         view?.findViewById<RadioButton>(R.id.blackTheme)?.isChecked = false
                         view?.findViewById<RadioButton>(R.id.followSystem)?.isChecked = true
                         AccentColor(requireContext()).setAccentState(4)
                         DarkThemeData(requireContext()).setDarkModeState(3)
                         restartThemeChange()
                     }
                     else -> {
                         AccentColor(requireContext()).setAccentState(4)
                         DarkThemeData(requireContext()).setDarkModeState(3)
                     }
                 }
             }*/
            ColoredNavBarData(requireContext()).setNavBar(false)
        view?.findViewById<MaterialSwitch>(R.id.coloredNavBarSwitch)?.isChecked = false
            //view?.findViewById<RadioButton>(R.id.disableColoredNavBar)?.isChecked = true
            //view?.findViewById<RadioButton>(R.id.enableColoredNavBar)?.isChecked = false
            activity?.window?.navigationBarColor =
                ContextCompat.getColor(requireContext(), R.color.black)

            if (FollowSystemThemeChoice(requireContext()).loadFollowSystemThemePreference() != 0) {
                FollowSystemThemeChoice(requireContext()).setFollowSystemThemePreference(0)
                //view?.findViewById<RadioButton>(R.id.darkThemeSystem)?.isChecked = false
                //view?.findViewById<RadioButton>(R.id.amoledSystemTheme)?.isChecked = true
                restartThemeChange()
            }
            Toast.makeText(requireContext(), getString(R.string.appearance_settings_reset), Toast.LENGTH_LONG).show()
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

    private fun restartApplication() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent =
                requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }, 1000)
    }
}