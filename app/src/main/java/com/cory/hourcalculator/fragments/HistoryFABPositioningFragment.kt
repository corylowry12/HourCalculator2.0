package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.ColoredTitleBarTextData
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.cory.hourcalculator.sharedprefs.HistoryFABPositioning
import com.cory.hourcalculator.sharedprefs.MenuTintData
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily

class HistoryFABPositioningFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_f_a_b_positioning, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        val topAppBarBackgroundColorFragment =
            view.findViewById<MaterialToolbar>(R.id.materialToolBarfabPositioningFragment)
        topAppBarBackgroundColorFragment?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

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
                        "Would you like to reset FAB Positioning settings?"

                    if (resources.getBoolean(R.bool.isTablet)) {
                        val bottomSheet =
                            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheetBehavior.skipCollapsed = true
                        bottomSheetBehavior.isHideable = false
                        bottomSheetBehavior.isDraggable = false
                    }
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

        updateCustomColor()

        val leftCardView = requireActivity().findViewById<MaterialCardView>(R.id.leftCardViewFabPositioning)
        val middleCardView = requireActivity().findViewById<MaterialCardView>(R.id.middleCardViewFabPositioning)
        val rightCardView = requireActivity().findViewById<MaterialCardView>(R.id.rightCardViewFabPositioning)

        leftCardView.shapeAppearanceModel = leftCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        middleCardView.shapeAppearanceModel = middleCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        rightCardView.shapeAppearanceModel = rightCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val leftRadioButton = requireActivity().findViewById<RadioButton>(R.id.leftRadioButton)
        val middleRadioButton = requireActivity().findViewById<RadioButton>(R.id.middleRadioButton)
        val rightRadioButton = requireActivity().findViewById<RadioButton>(R.id.rightRadioButton)


        if (HistoryFABPositioning(requireContext()).loadFABPosition() == 0) {
            leftRadioButton.isChecked = true
        }
        if (HistoryFABPositioning(requireContext()).loadFABPosition() == 1) {
            middleRadioButton.isChecked = true
        }
        if (HistoryFABPositioning(requireContext()).loadFABPosition() == 2) {
            rightRadioButton.isChecked = true
        }

        leftCardView.setOnClickListener {
            HistoryFABPositioning(requireContext()).setFABPosition(0)
            leftRadioButton.isChecked = true
            middleRadioButton.isChecked = false
            rightRadioButton.isChecked = false
        }
        middleCardView.setOnClickListener {
            HistoryFABPositioning(requireContext()).setFABPosition(1)
            leftRadioButton.isChecked = false
            middleRadioButton.isChecked = true
            rightRadioButton.isChecked = false
        }
        rightCardView.setOnClickListener {
            HistoryFABPositioning(requireContext()).setFABPosition(2)
            leftRadioButton.isChecked = false
            middleRadioButton.isChecked = false
            rightRadioButton.isChecked = true
        }
    }

    fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.fabPositioningCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val leftCardView = requireActivity().findViewById<MaterialCardView>(R.id.leftCardViewFabPositioning)
        val middleCardView = requireActivity().findViewById<MaterialCardView>(R.id.middleCardViewFabPositioning)
        val rightCardView = requireActivity().findViewById<MaterialCardView>(R.id.rightCardViewFabPositioning)

        leftCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        middleCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        rightCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val startButton = requireActivity().findViewById<RadioButton>(R.id.leftRadioButton)
        val middleButton = requireActivity().findViewById<RadioButton>(R.id.middleRadioButton)
        val rightButton = requireActivity().findViewById<RadioButton>(R.id.rightRadioButton)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )

        val colors = intArrayOf(
            Color.parseColor("#000000"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )
        startButton.buttonTintList = ColorStateList(states, colors)
        middleButton.buttonTintList = ColorStateList(states, colors)
        rightButton.buttonTintList = ColorStateList(states, colors)

        val collapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarFabPositioningFragment)
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

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarFabPositioningFragment)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarFabPositioningFragment)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }

        val topAppBarBackgroundColorFragment =
            requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarfabPositioningFragment)
        val resetDrawable = topAppBarBackgroundColorFragment?.menu?.findItem(R.id.reset)?.icon
        val navigationDrawable = topAppBarBackgroundColorFragment?.navigationIcon
        resetDrawable?.mutate()
        navigationDrawable?.mutate()

        if (Build.VERSION.SDK_INT >= 29) {
            try {
                if (MenuTintData(requireContext()).loadMenuTint()) {

                    resetDrawable?.colorFilter = BlendModeColorFilter(
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
                    resetDrawable?.colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), id),
                        BlendMode.SRC_ATOP
                    )
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), id),
                        BlendMode.SRC_ATOP
                    )
                }
            } catch (e: NoClassDefFoundError) {
                e.printStackTrace()
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                val id = typedValue.resourceId
                navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP);
                resetDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            }
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP);
            resetDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun reset() {
        val leftRadioButton = view?.findViewById<RadioButton>(R.id.leftRadioButton)
        val middleRadioButton = view?.findViewById<RadioButton>(R.id.middleRadioButton)
        val rightRadioButton = view?.findViewById<RadioButton>(R.id.rightRadioButton)

        leftRadioButton?.isChecked = false
        middleRadioButton?.isChecked = true
        rightRadioButton?.isChecked = false

        HistoryFABPositioning(requireContext()).setFABPosition(1)

        Toast.makeText(
            requireContext(),
            getString(R.string.reset_to_default),
            Toast.LENGTH_LONG
        )
            .show()
    }
}