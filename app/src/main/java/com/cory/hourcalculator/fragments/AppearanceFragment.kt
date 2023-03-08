package com.cory.hourcalculator.fragments

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
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
            accentColor.loadAccent() == 5 -> {
                activity?.theme?.applyStyle(R.style.transparent_accent, true)
            }
        }
        return inflater.inflate(R.layout.fragment_appearance, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val accentColorImageView = activity?.findViewById<ImageView>(R.id.currentSelectedAccentColorImageView)
        if (AccentColor(requireContext()).loadAccent() == 5) {
            accentColorImageView?.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        }

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarAppearance)

        setColoredMenuItemTint(topAppBar)

        if (AccentColor(requireContext()).loadAccent() == 5) {
            updateCustomColorChange()
        }

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

        val themeCardView = requireActivity().findViewById<MaterialCardView>(R.id.themeCardViewAppearance)
        val accentColorCardView = requireActivity().findViewById<MaterialCardView>(R.id.accentColorCardView)
        val appIconCardView = requireActivity().findViewById<MaterialCardView>(R.id.appIconCardView)
        val coloredNavigationBarCardView = requireActivity().findViewById<MaterialCardView>(R.id.navbarCardView)
        val coloredMenuTintCardView = requireActivity().findViewById<MaterialCardView>(R.id.coloredMenuTintCardView)

        themeCardView.shapeAppearanceModel = themeCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        accentColorCardView.shapeAppearanceModel = accentColorCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        appIconCardView.shapeAppearanceModel = appIconCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
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

        themeCardView?.setOnClickListener {
            openFragment(BackgroundColorFragment())
        }

        accentColorCardView?.setOnClickListener {
            openFragment(AccentColorFragment())
        }

        val appIconImageTextView = activity?.findViewById<TextView>(R.id.appIconImageTextView)
        val appIconImageView = activity?.findViewById<ImageView>(R.id.currentSelectedAppIconImageView)

        if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "auto") {
            appIconImageTextView?.text = "App Icon (*)"
            appIconImageView?.setImageResource(R.drawable.hourcalclogoteal)
        }
        else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "teal") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogoteal)
        }
        else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "pink") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogopink)
        }
        else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "orange") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogoorange)
        }
        else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "red") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogored)
        }
        else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "blue") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogoblue)
        }
        else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "og") {
            appIconImageView?.setImageResource(R.drawable.hourcalculatorlogoyellowgradient)
        }
        else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "snow falling") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogo_christmas)
        }
        else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "material you") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogoteal)
        }
        appIconCardView?.setOnClickListener {
            openFragment(ChooseAppIconFragment())
        }

        val accentColor = AccentColor(requireContext())

        val coloredNavBarSwitch = view.findViewById<MaterialSwitch>(R.id.coloredNavBarSwitch)
        val coloredNavBarData = ColoredNavBarData(requireContext())

        coloredNavBarSwitch.isChecked = coloredNavBarData.loadNavBar()

        if (coloredNavBarData.loadNavBar()) {
            coloredNavBarSwitch?.isChecked = true
            coloredNavBarSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!coloredNavBarData.loadNavBar()) {
            coloredNavBarSwitch?.isChecked = false
            coloredNavBarSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        coloredNavigationBarCardView.setOnClickListener {
            coloredNavBarSwitch.isChecked = !coloredNavBarSwitch.isChecked
            toggleColoredNavBar(coloredNavBarSwitch.isChecked, coloredNavBarData, accentColor)
        }

        coloredNavBarSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            toggleColoredNavBar(isChecked, coloredNavBarData, accentColor)
        }

        val coloredMenuTintSwitch = view.findViewById<MaterialSwitch>(R.id.coloredMenuTintSwitch)
        val coloredMenuTintData = MenuTintData(requireContext())

        coloredMenuTintSwitch.isChecked = coloredMenuTintData.loadMenuTint()

        if (coloredMenuTintData.loadMenuTint()) {
            coloredMenuTintSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else {
            coloredMenuTintSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        coloredMenuTintCardView.setOnClickListener {
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
                accentColor.loadAccent() == 5 -> {
                    activity?.window?.navigationBarColor =
                        Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
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
        if (chosenAppIcon.loadChosenAppIcon() == "teal") {
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
                chosenAppIcon.setChosenAppIcon("teal")
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
        if (chosenAppIcon.loadChosenAppIcon() == "pink") {
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
                chosenAppIcon.setChosenAppIcon("pink")
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
        if (chosenAppIcon.loadChosenAppIcon() == "orange") {
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
                chosenAppIcon.setChosenAppIcon("orange")
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
        if (chosenAppIcon.loadChosenAppIcon() == "red") {
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
                chosenAppIcon.setChosenAppIcon("red")
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
        if (chosenAppIcon.loadChosenAppIcon() == "auto") {
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
                chosenAppIcon.setChosenAppIcon("auto")

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
            ChosenAppIconData(requireContext()).setChosenAppIcon("auto")
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

    fun updateCustomColorChange() {
        val customColorGenerator = CustomColorGenerator(requireContext())

        val backgroundColorCardView = requireActivity().findViewById<MaterialCardView>(R.id.themeCardViewAppearance)
        val accentColorCardView = requireActivity().findViewById<MaterialCardView>(R.id.accentColorCardView)
        val appIconCardView = requireActivity().findViewById<MaterialCardView>(R.id.appIconCardView)
        val coloredNavBarCardView = requireActivity().findViewById<MaterialCardView>(R.id.navbarCardView)
        val coloredMenuItemsCardView = requireActivity().findViewById<MaterialCardView>(R.id.coloredMenuTintCardView)

        backgroundColorCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        accentColorCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        appIconCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        coloredNavBarCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        coloredMenuItemsCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAppearance)?.setContentScrimColor(
            Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAppearance)?.setStatusBarScrimColor(
            Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )
        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(customColorGenerator.generateCustomColorPrimary())
        )

        val toggleColoredNavBarSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.coloredNavBarSwitch)
        val toggleColoredMenuItemsSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.coloredMenuTintSwitch)

        toggleColoredNavBarSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(customColorGenerator.generateCustomColorPrimary()))
        toggleColoredNavBarSwitch.trackTintList = ColorStateList(states, colors)
        toggleColoredMenuItemsSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(customColorGenerator.generateCustomColorPrimary()))
        toggleColoredMenuItemsSwitch.trackTintList = ColorStateList(states, colors)

        val topAppBarAppearance = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarAppearance)

        val navigationDrawable = topAppBarAppearance?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            if (AccentColor(requireContext()).loadAccent() == 5) {
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
                navigationDrawable?.colorFilter = BlendModeColorFilter(
                    Color.parseColor(customColorGenerator.generateMenuTintColor()),
                    BlendMode.SRC_ATOP
                )
            }
            else {
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
                val id = typedValue.resourceId
                navigationDrawable?.colorFilter = BlendModeColorFilter(
                    id,
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
    }
}