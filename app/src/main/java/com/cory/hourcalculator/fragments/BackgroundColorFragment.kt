package com.cory.hourcalculator.fragments

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
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.intents.MainActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily

class BackgroundColorFragment : Fragment() {

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

        return inflater.inflate(R.layout.fragment_background_color, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBarBackgroundColorFragment =
            view.findViewById<MaterialToolbar>(R.id.materialToolBarBackgroundColorFragment)
        topAppBarBackgroundColorFragment?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        updateCustomColorChange()

        topAppBarBackgroundColorFragment.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.reset -> {
                    Vibrate().vibration(requireContext())
                    val dialog = BottomSheetDialog(requireContext())
                    val resetSettingsLayout =
                        layoutInflater.inflate(R.layout.reset_settings_bottom_sheet, null)
                    val yesResetButton =
                        resetSettingsLayout.findViewById<Button>(R.id.yesResetButton)
                    val cancelResetButton =
                        resetSettingsLayout.findViewById<Button>(R.id.cancelResetButton)

                    resetSettingsLayout.findViewById<MaterialCardView>(R.id.bodyCardView)
                        .setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                    yesResetButton.setBackgroundColor(
                        Color.parseColor(
                            CustomColorGenerator(
                                requireContext()
                            ).generateCustomColorPrimary()
                        )
                    )
                    cancelResetButton.setTextColor(
                        Color.parseColor(
                            CustomColorGenerator(
                                requireContext()
                            ).generateCustomColorPrimary()
                        )
                    )

                    dialog.setContentView(resetSettingsLayout)
                    dialog.setCancelable(false)
                    resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text =
                        "Would you like to reset Background Color Settings?"
                    /*if (resources.getBoolean(R.bool.isTablet)) {
                        val bottomSheet =
                            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheetBehavior.skipCollapsed = true
                        bottomSheetBehavior.isHideable = false
                        bottomSheetBehavior.isDraggable = false
                    }*/
                    yesResetButton.setOnClickListener {
                        Vibrate().vibration(requireContext())
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
                else -> false
            }
        }

        val lightThemeCardView =
            view.findViewById<MaterialCardView>(R.id.lightThemeCardViewBackgroundColor)
        val darkThemeCardView =
            view.findViewById<MaterialCardView>(R.id.darkThemeCardViewBackgroundColor)
        val followSystemThemeCardView =
            view.findViewById<MaterialCardView>(R.id.followSystemThemeCardViewBackgroundColor)
        lightThemeCardView.shapeAppearanceModel = lightThemeCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        darkThemeCardView.shapeAppearanceModel = darkThemeCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        followSystemThemeCardView.shapeAppearanceModel =
            followSystemThemeCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(28f)
                .setBottomLeftCornerSize(28f)
                .build()

        val darkThemeData = DarkThemeData(requireContext())

        val lightThemeButton = activity?.findViewById<RadioButton>(R.id.lightTheme)
        val amoledThemeButton = activity?.findViewById<RadioButton>(R.id.blackTheme)
        val followSystemThemeButton = activity?.findViewById<RadioButton>(R.id.followSystem)

        val followSystemCardView = activity?.findViewById<MaterialCardView>(R.id.followSystemCardView)

                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        followSystemCardView?.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        followSystemCardView?.setCardBackgroundColor(Color.parseColor("#000000"))
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        followSystemCardView?.setCardBackgroundColor(Color.parseColor("#000000"))
                    }
                }

        when {
            darkThemeData.loadDarkModeState() == 1 -> {

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

        lightThemeCardView?.setOnClickListener {
            changeLightTheme(
                darkThemeData,
                amoledThemeButton,
                followSystemThemeButton,
                lightThemeButton!!
            )
        }
        darkThemeCardView?.setOnClickListener {
            changeToDark(
                darkThemeData,
                lightThemeButton,
                followSystemThemeButton,
                amoledThemeButton!!
            )
        }
        followSystemThemeCardView?.setOnClickListener {
            changeToFollowSystem(
                darkThemeData,
                lightThemeButton,
                amoledThemeButton,
                followSystemThemeButton!!
            )
        }
    }

    private fun changeToFollowSystem(
        darkThemeData: DarkThemeData,
        lightThemeButton: RadioButton?,
        amoledThemeButton: RadioButton?,
        followSystemThemeButton: RadioButton
    ) {
        Vibrate().vibration(requireContext())
        darkThemeData.setDarkModeState(3)
        lightThemeButton?.isChecked = false
        amoledThemeButton?.isChecked = false
        followSystemThemeButton.isChecked = true
        updateCustomColorChange()
    }

    private fun changeToDark(
        darkThemeData: DarkThemeData,
        lightThemeButton: RadioButton?,
        followSystemThemeButton: RadioButton?,
        amoledThemeButton: RadioButton
    ) {
        Vibrate().vibration(requireContext())
        if (darkThemeData.loadDarkModeState() == 2) {
            Toast.makeText(
                requireContext(),
                getString(R.string.black_theme_already_enabled),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            darkThemeData.setDarkModeState(2)
            lightThemeButton?.isChecked = false
            followSystemThemeButton?.isChecked = false
            amoledThemeButton.isChecked = true
            updateCustomColorChange()
        }
    }

    private fun changeLightTheme(
        darkThemeData: DarkThemeData,
        amoledThemeButton: RadioButton?,
        followSystemThemeButton: RadioButton?,
        lightThemeButton: RadioButton
    ) {
        Vibrate().vibration(requireContext())
        if (darkThemeData.loadDarkModeState() == 0) {
            Toast.makeText(
                requireContext(),
                getString(R.string.light_theme_is_already_enabled),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            darkThemeData.setDarkModeState(0)
            amoledThemeButton?.isChecked = false
            followSystemThemeButton?.isChecked = false
            lightThemeButton.isChecked = true
            updateCustomColorChange()
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

        // view?.findViewById<NestedScrollView>(R.id.nestedScrollViewAppearance)
        // ?.scrollTo(0, AppearanceScrollPosition(requireContext()).loadScroll())

        //val collapsingToolbarLayout =
        //requireView().findViewById<AppBarLayout>(R.id.appBarLayoutAppearance)

        //collapsingToolbarLayout.setExpanded(
        // AppearanceScrollPosition(requireContext()).loadCollapsed(),
        //false
        //)
    }

    private fun reset() {
        val lightThemeRadioButton = view?.findViewById<RadioButton>(R.id.lightTheme)
        val darkThemeRadioButton = view?.findViewById<RadioButton>(R.id.blackTheme)
        val followSystemRadioButton = view?.findViewById<RadioButton>(R.id.followSystem)

        lightThemeRadioButton?.isChecked = false
        darkThemeRadioButton?.isChecked = false
        followSystemRadioButton?.isChecked = true

        DarkThemeData(requireContext()).setDarkModeState(3)
        updateCustomColorChange()
    }

    private fun updateCustomColorChange() {
        val lightThemeCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.lightThemeCardViewBackgroundColor)
        val darkThemeCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.darkThemeCardViewBackgroundColor)
        val followSystemThemeCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.followSystemThemeCardViewBackgroundColor)

        lightThemeCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )
        darkThemeCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )
        followSystemThemeCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )

        val topAppBarBackgroundColorFragment =
            requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarBackgroundColorFragment)
        val resetDrawable = topAppBarBackgroundColorFragment?.menu?.findItem(R.id.reset)?.icon
        val navigationDrawable = topAppBarBackgroundColorFragment?.navigationIcon
        resetDrawable?.mutate()
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            navigationDrawable?.colorFilter = BlendModeColorFilter(
                Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                BlendMode.SRC_ATOP
            )
            resetDrawable?.colorFilter = BlendModeColorFilter(
                Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                BlendMode.SRC_ATOP
            )
        } else {
            val darkThemeData = DarkThemeData(requireContext())
            when {
                darkThemeData.loadDarkModeState() == 1 -> {
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#ffffff"),
                        BlendMode.SRC_ATOP
                    )
                    resetDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#ffffff"),
                        BlendMode.SRC_ATOP
                    )
                }
                darkThemeData.loadDarkModeState() == 0 -> {
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#000000"),
                        BlendMode.SRC_ATOP
                    )
                    resetDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#000000"),
                        BlendMode.SRC_ATOP
                    )
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#ffffff"),
                        BlendMode.SRC_ATOP
                    )
                    resetDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#ffffff"),
                        BlendMode.SRC_ATOP
                    )
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            navigationDrawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor("#000000"),
                                BlendMode.SRC_ATOP
                            )
                            resetDrawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor("#000000"),
                                BlendMode.SRC_ATOP
                            )
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            navigationDrawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor("#ffffff"),
                                BlendMode.SRC_ATOP
                            )
                            resetDrawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor("#ffffff"),
                                BlendMode.SRC_ATOP
                            )
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            navigationDrawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor("#ffffff"),
                                BlendMode.SRC_ATOP
                            )
                            resetDrawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor("#ffffff"),
                                BlendMode.SRC_ATOP
                            )
                        }
                    }
                }
            }
        }

        val collapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarBackgroundColorFragment)
        collapsingToolbarLayout.setStatusBarScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )
        collapsingToolbarLayout.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        val lightThemeButton = requireActivity().findViewById<RadioButton>(R.id.lightTheme)
        val blackThemeButton = requireActivity().findViewById<RadioButton>(R.id.blackTheme)
        val followSystemThemeButton = requireActivity().findViewById<RadioButton>(R.id.followSystem)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )

        val colors = intArrayOf(
            Color.parseColor("#000000"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )
        lightThemeButton.buttonTintList = ColorStateList(states, colors)
        blackThemeButton.buttonTintList = ColorStateList(states, colors)
        followSystemThemeButton.buttonTintList = ColorStateList(states, colors)

        val runnable = Runnable {
            (context as MainActivity).updateBottomNavCustomColor()
            (context as MainActivity).setBackgroundColor()
            if (ColoredNavBarData(requireContext()).loadNavBar()) {
                activity?.window?.navigationBarColor =
                    Color.parseColor(CustomColorGenerator(requireContext()).generateNavBarColor())
            }
        }
        MainActivity().runOnUiThread(runnable)

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarBackgroundColorFragment)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarBackgroundColorFragment)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }

        requireActivity().findViewById<CoordinatorLayout>(R.id.backgroundColorCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))

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
                            AccentColor(requireContext()).followSystemTheme(
                                requireContext()
                            )
                        )
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
    }

}