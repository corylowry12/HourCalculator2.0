package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception

class WageSettingsFragment : Fragment() {

    private lateinit var dialog: BottomSheetDialog

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        updateCustomColor()

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

        try {
            if (dialog.isShowing) {
                dialog.dismiss()
                resetMenuPress()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
        return inflater.inflate(R.layout.fragment_wage_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        updateCustomColor()

        val toolbar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarWageSettings)

        toolbar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        toolbar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.reset -> {
                    resetMenuPress()
                    true
                }

                else -> true
            }
        }

        val wagesCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewWages)
        val calculateOvertimeInHistoryCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewCalculateOvertimeInHistory)
        val overtimeAmountCardView = requireActivity().findViewById<MaterialCardView>(R.id.overtimeRatioCardView)

        wagesCardView.shapeAppearanceModel = wagesCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        calculateOvertimeInHistoryCardView.shapeAppearanceModel = calculateOvertimeInHistoryCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        overtimeAmountCardView.shapeAppearanceModel =
            overtimeAmountCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(28f)
                .setBottomLeftCornerSize(28f)
                .build()

        val wagesData = WagesData(requireContext())
        val wagesEditText = activity?.findViewById<TextInputEditText>(R.id.Wages)

        String.format(
            "%,.2f",
            wagesData.loadWageAmount()?.toDouble()
        )

        val editable =
            Editable.Factory.getInstance().newEditable(String.format(
                "%,.2f",
                wagesData.loadWageAmount()?.toDouble()
            ))
        wagesEditText?.text = editable

        wagesEditText?.setOnKeyListener(View.OnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard(wagesEditText)
                return@OnKeyListener true
            }
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                wagesData.setWageAmount(wagesEditText.text.toString())
                hideKeyboard(wagesEditText)
                return@OnKeyListener true
            }
            false
        })

        wagesEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    wagesData.setWageAmount(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wagesData.setWageAmount(s.toString())
            }
        })

        val calculateOvertimeInHistoryData = CalculateOvertimeInHistoryData(requireContext())

        val calculateOvertimeInHistorySwitch = activity?.findViewById<MaterialSwitch>(R.id.calculateOvertimeInHistorySwitch)

        if (calculateOvertimeInHistoryData.loadCalculateOvertimeInHistoryState()) {
            calculateOvertimeInHistorySwitch?.isChecked = true
            calculateOvertimeInHistorySwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            calculateOvertimeInHistorySwitch?.jumpDrawablesToCurrentState()
        } else {
            calculateOvertimeInHistorySwitch?.isChecked = false
            calculateOvertimeInHistorySwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            calculateOvertimeInHistorySwitch?.jumpDrawablesToCurrentState()
        }

        calculateOvertimeInHistoryCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            calculateOvertimeInHistorySwitch?.isChecked = !calculateOvertimeInHistorySwitch!!.isChecked
            calculateOvertimeInHistoryData.setCalculateOvertimeInHistoryState(calculateOvertimeInHistorySwitch.isChecked)
            if (calculateOvertimeInHistorySwitch.isChecked) {
                calculateOvertimeInHistorySwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                calculateOvertimeInHistorySwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        calculateOvertimeInHistoryCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)

            if (resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    infoDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                getString(R.string.calculate_overtime_setting_info)
            val yesButton = infoAboutSettingLayout.findViewById<Button>(R.id.yesButton)

            infoAboutSettingLayout.findViewById<MaterialCardView>(R.id.bodyCardView)
                .setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

            yesButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                infoDialog.dismiss()
            }
            infoDialog.show()
            return@setOnLongClickListener true
        }

        val overtimeSlider = requireActivity().findViewById<Slider>(R.id.overtimeAmountSlider)
        overtimeSlider.value = OvertimeData(requireContext()).loadOverTimeAmount()

        overtimeSlider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            Vibrate().vibrationSliders(requireContext())
            OvertimeData(requireContext()).setOverTimeAmount(value)
        })

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (wagesEditText!!.hasFocus()) {
                        wagesEditText.clearFocus()
                    } else {
                       activity?.supportFragmentManager?.popBackStack()
                    }
                }
            })
    }

    fun updateCustomColor() {

        requireActivity().findViewById<CoordinatorLayout>(R.id.wageSettingsCoordinatorLayout)
            .setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val collapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutWageSettings)
        collapsingToolbarLayout.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )

        val wagesCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewWages)
        val calculateOvertimeInHistoryCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewCalculateOvertimeInHistory)
        val overtimeAmountCardView = requireActivity().findViewById<MaterialCardView>(R.id.overtimeRatioCardView)
        wagesCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        calculateOvertimeInHistoryCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        overtimeAmountCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )

        val wagesEditText = requireActivity().findViewById<TextInputEditText>(R.id.Wages)
        val wagesTextInputEditText =
            activity?.findViewById<TextInputLayout>(R.id.outlinedTextFieldWages)

        wagesTextInputEditText?.boxStrokeColor = Color.parseColor("#000000")
        wagesTextInputEditText?.hintTextColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                wagesEditText.textCursorDrawable = null
            }

        wagesTextInputEditText?.defaultHintTextColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        wagesEditText.highlightColor =
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        wagesEditText.setTextIsSelectable(false)

        val calculateOverTimeInHistorySwitch = requireActivity().findViewById<MaterialSwitch>(R.id.calculateOvertimeInHistorySwitch)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )
        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )

        calculateOverTimeInHistorySwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        calculateOverTimeInHistorySwitch.trackTintList = ColorStateList(states, colors)

        val sliderStates = arrayOf(
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_enabled)
        )

        val sliderStatesInactive = arrayOf(
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(-android.R.attr.state_enabled)
        )

        val overtimeSlider = requireActivity().findViewById<Slider>(R.id.overtimeAmountSlider)
        overtimeSlider.trackActiveTintList = ColorStateList(sliderStates, colors)
        overtimeSlider.trackInactiveTintList = ColorStateList(sliderStatesInactive, colors)
        overtimeSlider.thumbTintList = ColorStateList(sliderStates, colors)
        overtimeSlider.tickActiveTintList = ColorStateList.valueOf(Color.parseColor("#e7dfec"))
        overtimeSlider.tickInactiveTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutWageSettings)
            ?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutWageSettings)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutWageSettings)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarWageSettings)

        val navigationDrawable = topAppBar?.navigationIcon
        navigationDrawable?.mutate()

        val resetDrawable = topAppBar?.menu?.findItem(R.id.reset)?.icon
        resetDrawable?.mutate()

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
                navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
                resetDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            }
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            resetDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun hideKeyboard(wagesEditText: TextInputEditText) {
        val inputManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = activity?.currentFocus

        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(
                focusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            if (wagesEditText.hasFocus()) {
                wagesEditText.clearFocus()
            }
        }
    }

    private fun resetMenuPress() {

        dialog = BottomSheetDialog(requireContext())
        val resetSettingsLayout =
            layoutInflater.inflate(R.layout.reset_settings_bottom_sheet, null)
        dialog.setContentView(resetSettingsLayout)
        dialog.setCancelable(false)

        if (resources.getBoolean(R.bool.isTablet)) {
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.isDraggable = false
        }
        resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text =
            getString(R.string.would_you_like_to_reset_wage_settings)
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

    }

    private fun reset() {
        CalculateOvertimeInHistoryData(requireContext()).setCalculateOvertimeInHistoryState(true)

        val calculateOvertimeSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.calculateOvertimeInHistorySwitch)

        calculateOvertimeSwitch?.isChecked = true
        calculateOvertimeSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)

        WagesData(requireContext()).setWageAmount("7.25")
        val editable =
            Editable.Factory.getInstance().newEditable(WagesData(requireContext()).loadWageAmount())
        val wagesEditText = requireActivity().findViewById<TextInputEditText>(R.id.Wages)
        wagesEditText?.text = editable

        hideKeyboard(wagesEditText)

        OvertimeData(requireContext()).setOverTimeAmount(1.5f)
        val overtimeSlider = requireActivity().findViewById<Slider>(R.id.overtimeAmountSlider)
        overtimeSlider.value = OvertimeData(requireContext()).loadOverTimeAmount()
    }
}