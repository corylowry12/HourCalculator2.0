package com.cory.hourcalculator.fragments

import android.annotation.SuppressLint
import android.content.Intent
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
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.intents.MainActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlin.math.abs

class AppearanceFragment : Fragment() {

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateCustomColorChange()
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
        return inflater.inflate(R.layout.fragment_appearance, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentSelectedBackgroundColorCardView = requireView().findViewById<MaterialCardView>(R.id.currentSelectedThemeCardView)
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                currentSelectedBackgroundColorCardView.setCardBackgroundColor(Color.parseColor("#000000"))
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                currentSelectedBackgroundColorCardView.setCardBackgroundColor(Color.parseColor("#ffffff"))
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                currentSelectedBackgroundColorCardView.setCardBackgroundColor(Color.parseColor("#000000"))
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        currentSelectedBackgroundColorCardView.setCardBackgroundColor(Color.parseColor("#ffffff"))
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        currentSelectedBackgroundColorCardView.setCardBackgroundColor(Color.parseColor("#000000"))
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        currentSelectedBackgroundColorCardView.setCardBackgroundColor(Color.parseColor("#000000"))
                    }
                }
            }
        }
        val currentSelectedAccentColorCardView =
            activity?.findViewById<MaterialCardView>(R.id.currentSelectedAccentColorCardView)
        currentSelectedAccentColorCardView?.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCustomColorPrimary()
            )
        )

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarAppearance)

        updateCustomColorChange()

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
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

        val themeCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.themeCardViewAppearance)
        val accentColorCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.accentColorCardView)
        val appIconCardView = requireActivity().findViewById<MaterialCardView>(R.id.appIconCardView)
        val coloredNavigationBarCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.navbarCardView)
        val coloredMenuTintCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.coloredMenuTintCardView)
        val coloredTitleBarTextCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.coloredTitleBarTextCardView)

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
        coloredNavigationBarCardView.shapeAppearanceModel =
            coloredNavigationBarCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
        coloredMenuTintCardView.shapeAppearanceModel =
            coloredNavigationBarCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
        coloredTitleBarTextCardView.shapeAppearanceModel =
            coloredTitleBarTextCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(28f)
                .setBottomLeftCornerSize(28f)
                .build()

        val currentBackgroundColorCardView =
            activity?.findViewById<MaterialCardView>(R.id.currentSelectedThemeCardView)
        currentBackgroundColorCardView?.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))

        themeCardView?.setOnClickListener {
            openFragment(BackgroundColorFragment())
        }

        accentColorCardView?.setOnClickListener {
            openFragment(AccentColorFragment())
        }

        val appIconImageTextView = activity?.findViewById<TextView>(R.id.appIconImageTextView)
        val appIconImageView =
            activity?.findViewById<ImageView>(R.id.currentSelectedAppIconImageView)

        if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "auto") {
            appIconImageTextView?.text = "App Icon (*)"
            appIconImageView?.setImageResource(R.drawable.hourcalclogoteal)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "teal") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogoteal)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "pink") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogopink)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "orange") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogoorange)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "red") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogored)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "blue") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogoblue)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "og") {
            appIconImageView?.setImageResource(R.drawable.hourcalculatorlogoyellowgradient)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "snow falling") {
            appIconImageView?.setImageResource(R.drawable.hourcalclogo_christmas)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "material you") {
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
            coloredNavBarSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!coloredNavBarData.loadNavBar()) {
            coloredNavBarSwitch?.isChecked = false
            coloredNavBarSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        coloredNavigationBarCardView.setOnClickListener {
            coloredNavBarSwitch.isChecked = !coloredNavBarSwitch.isChecked
            toggleColoredNavBar(coloredNavBarSwitch.isChecked, coloredNavBarData, accentColor)
        }

        val coloredMenuTintSwitch = view.findViewById<MaterialSwitch>(R.id.coloredMenuTintSwitch)
        val coloredMenuTintData = MenuTintData(requireContext())

        coloredMenuTintSwitch.isChecked = coloredMenuTintData.loadMenuTint()

        if (coloredMenuTintData.loadMenuTint()) {
            coloredMenuTintSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else {
            coloredMenuTintSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        coloredMenuTintCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            coloredMenuTintSwitch.isChecked = !coloredMenuTintSwitch.isChecked
            coloredMenuTintData.setColoredMenuTint(coloredMenuTintSwitch.isChecked)
            if (coloredMenuTintSwitch.isChecked) {
                coloredMenuTintSwitch?.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                coloredMenuTintSwitch?.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            setColoredMenuItemTint(topAppBar)
        }

        val coloredTitleBarTextSwitch =
            view.findViewById<MaterialSwitch>(R.id.coloredTitleBarTextSwitch)
        val coloredTitleBarData = ColoredTitleBarTextData(requireContext())
        coloredTitleBarTextSwitch.isChecked = coloredTitleBarData.loadTitleBarTextState()

        if (coloredTitleBarData.loadTitleBarTextState()) {
            coloredTitleBarTextSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else {
            coloredTitleBarTextSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        coloredTitleBarTextCardView.setOnClickListener {
            coloredTitleBarTextSwitch.isChecked = !coloredTitleBarTextSwitch.isChecked
            Vibrate().vibration(requireContext())
            coloredTitleBarData.setTitleBarTextState(coloredTitleBarTextSwitch.isChecked)
            val collapsingToolbarLayout =
                requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAppearance)
            if (coloredTitleBarTextSwitch.isChecked) {
                coloredTitleBarTextSwitch?.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                collapsingToolbarLayout.setCollapsedTitleTextColor(
                    Color.parseColor(
                        CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()
                    )
                )
            } else {
                coloredTitleBarTextSwitch?.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                val id = typedValue.resourceId
                collapsingToolbarLayout.setCollapsedTitleTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        id
                    )
                )
            }
        }

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
    }

    private fun toggleColoredNavBar(
        isChecked: Boolean,
        coloredNavBarData: ColoredNavBarData,
        accentColor: AccentColor
    ) {
        Vibrate().vibration(requireContext())
        if (isChecked) {
            activity?.findViewById<MaterialSwitch>(R.id.coloredNavBarSwitch)?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            coloredNavBarData.setNavBar(true)

            activity?.window?.navigationBarColor =
                Color.parseColor(CustomColorGenerator(requireContext()).generateNavBarColor())

            Toast.makeText(
                requireContext(),
                getString(R.string.colored_nav_bar_enabled),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            activity?.findViewById<MaterialSwitch>(R.id.coloredNavBarSwitch)?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
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

    private fun updateCustomColorChange() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.coordinatorLayoutAppearance).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val customColorGenerator = CustomColorGenerator(requireContext())

        val backgroundColorCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.themeCardViewAppearance)
        val accentColorCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.accentColorCardView)
        val appIconCardView = requireActivity().findViewById<MaterialCardView>(R.id.appIconCardView)
        val coloredNavBarCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.navbarCardView)
        val coloredMenuItemsCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.coloredMenuTintCardView)
        val coloredTitleBarTextCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.coloredTitleBarTextCardView)

        backgroundColorCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        accentColorCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        appIconCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        coloredNavBarCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        coloredMenuItemsCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        coloredTitleBarTextCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAppearance)
            ?.setContentScrimColor(
                Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor())
            )
        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAppearance)
            ?.setStatusBarScrimColor(
                Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor())
            )

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )
        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(customColorGenerator.generateCustomColorPrimary())
        )

        val toggleColoredNavBarSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.coloredNavBarSwitch)
        val toggleColoredMenuItemsSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.coloredMenuTintSwitch)
        val toggleColoredTitleBarTextSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.coloredTitleBarTextSwitch)

        toggleColoredNavBarSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(customColorGenerator.generateCustomColorPrimary()))
        toggleColoredNavBarSwitch.trackTintList = ColorStateList(states, colors)
        toggleColoredMenuItemsSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(customColorGenerator.generateCustomColorPrimary()))
        toggleColoredMenuItemsSwitch.trackTintList = ColorStateList(states, colors)
        toggleColoredTitleBarTextSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(customColorGenerator.generateCustomColorPrimary()))
        toggleColoredTitleBarTextSwitch.trackTintList = ColorStateList(states, colors)

        val topAppBarAppearance =
            requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarAppearance)

        val navigationDrawable = topAppBarAppearance?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
            navigationDrawable?.colorFilter = BlendModeColorFilter(
                Color.parseColor(customColorGenerator.generateMenuTintColor()),
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

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAppearance)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAppearance)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }
    }
}