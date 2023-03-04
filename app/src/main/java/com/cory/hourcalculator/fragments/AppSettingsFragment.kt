package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class AppSettingsFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_app_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarAppSettings)

        val resetDrawable = topAppBar?.menu?.findItem(R.id.reset)?.icon
        resetDrawable?.mutate()

        val navigationDrawable = topAppBar?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
            val id = typedValue.resourceId
            resetDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            resetDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
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
                    resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text = "Would you like to reset App Settings?"
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
                else -> true
            }
        }

        val outTimeCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewSetOutTime)
        val calculationTypeCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewCalculation)
        val longClickEnabledCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewDisableLongPress)
        val clickableHistoryCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewHistoryClickable)
        val vibrationCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibration)
        val historyCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewHistory)
        val breakTextBoxCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewBreakTextBox)
        val wagesCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewWages)

        outTimeCardView.shapeAppearanceModel = outTimeCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        calculationTypeCardView.shapeAppearanceModel = calculationTypeCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        longClickEnabledCardView.shapeAppearanceModel = longClickEnabledCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        clickableHistoryCardView.shapeAppearanceModel = clickableHistoryCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        vibrationCardView.shapeAppearanceModel = vibrationCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        historyCardView.shapeAppearanceModel = historyCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        breakTextBoxCardView.shapeAppearanceModel = breakTextBoxCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        wagesCardView.shapeAppearanceModel = wagesCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val outTimeData = OutTimeData(requireContext())

        val outTimeSwitch = activity?.findViewById<MaterialSwitch>(R.id.setOutTimeSwitch)

        if (!outTimeData.loadOutTimeState()) {
            outTimeSwitch?.isChecked = true
            outTimeSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        }
        else {
            outTimeSwitch?.isChecked = false
            outTimeSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        outTimeSwitch?.setOnClickListener {
            Vibrate().vibration(requireContext())
            outTimeData.setOutTimeState(!outTimeSwitch.isChecked)
            if (outTimeSwitch.isChecked) {
                outTimeSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                Toast.makeText(requireContext(), getString(R.string.out_time_will_now_automatically_be_set_to_current_time), Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                outTimeSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                Toast.makeText(requireContext(), getString(R.string.out_time_will_now_automatically_be_set_to_previous_stored_time), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val calculationData = CalculationType(requireContext())

        val calculationTypeSwitch = activity?.findViewById<MaterialSwitch>(R.id.setCalculationTypeSwitch)

        if (calculationData.loadCalculationState()) {
            calculationTypeSwitch?.isChecked = false
            calculationTypeSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }
        else {
            calculationTypeSwitch?.isChecked = true
            calculationTypeSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        }

        calculationTypeSwitch?.setOnClickListener {
            Vibrate().vibration(requireContext())
            calculationData.setCalculationState(!calculationTypeSwitch.isChecked)
            if (calculationTypeSwitch.isChecked) {
                calculationTypeSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                Toast.makeText(requireContext(), getString(R.string.decimal_calculation_enabled), Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                calculationTypeSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                Toast.makeText(requireContext(), getString(R.string.time_calculation_enabled), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val longClickEnabled = LongPressCalculateButtonEnabled(requireContext())

        val longClickEnabledSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.toggleLongPressingCalculateSwitch)

        if (longClickEnabled.loadLongClick()) {
            longClickEnabledSwitch.isChecked = true
            longClickEnabledSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        }
        else {
            longClickEnabledSwitch.isChecked = false
            longClickEnabledSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        longClickEnabledSwitch.setOnClickListener {
            Vibrate().vibration(requireContext())
            longClickEnabled.setLongClick(longClickEnabledSwitch.isChecked)
            if (longClickEnabledSwitch.isChecked) {
                longClickEnabledSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                Toast.makeText(requireContext(), "Can now long click calculate button", Toast.LENGTH_SHORT).show()
            }
            else {
                longClickEnabledSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                Toast.makeText(requireContext(), "Can no longer long click calculate button", Toast.LENGTH_SHORT).show()
            }
        }

        val historyClickable = ClickableHistoryEntry(requireContext())
        val historyClickableSwitch = activity?.findViewById<MaterialSwitch>(R.id.clickableHistorySwitch)

        if (historyClickable.loadHistoryItemClickable()) {
            historyClickableSwitch?.isChecked = true
            historyClickableSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!historyClickable.loadHistoryItemClickable()) {
            historyClickableSwitch?.isChecked = false
            historyClickableSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        historyClickableSwitch?.setOnClickListener {
            Vibrate().vibration(requireContext())
            historyClickable.setHistoryItemClickable(historyClickableSwitch.isChecked)
            if (historyClickableSwitch.isChecked) {
                historyClickableSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                Toast.makeText(requireContext(), "History items are now clickable", Toast.LENGTH_SHORT).show()
            }
            else {
                historyClickableSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                Toast.makeText(requireContext(), "History items are not clickable", Toast.LENGTH_SHORT).show()
            }
        }

        val vibrationData = VibrationData(requireContext())
        val toggleVibration = activity?.findViewById<MaterialSwitch>(R.id.vibrationSwitch)

        if (vibrationData.loadVibrationState()) {
            toggleVibration?.isChecked = true
            toggleVibration?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!vibrationData.loadVibrationState()) {
            toggleVibration?.isChecked = false
            toggleVibration?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        toggleVibration?.setOnClickListener {
            Vibrate().vibration(requireContext())
            vibrationData.setVibrationState(toggleVibration.isChecked)
            if (toggleVibration.isChecked) {
                toggleVibration.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                Toast.makeText(requireContext(), getString(R.string.vibration_enabled), Toast.LENGTH_SHORT).show()
            }
            else {
                toggleVibration.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                Toast.makeText(requireContext(), getString(R.string.vibration_disabled), Toast.LENGTH_SHORT).show()
            }
        }

        val historyToggleData = HistoryToggleData(requireContext())
        val toggleHistory = activity?.findViewById<MaterialSwitch>(R.id.historySwitch)

        if (historyToggleData.loadHistoryState()) {
            toggleHistory?.isChecked = true
            toggleHistory?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!historyToggleData.loadHistoryState()) {
            toggleHistory?.isChecked = true
            toggleHistory?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        toggleHistory?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (toggleHistory.isChecked) {
                toggleHistory.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                Toast.makeText(requireContext(), getString(R.string.history_is_enabled), Toast.LENGTH_SHORT).show()
            } else {
                val alertDialog = MaterialAlertDialogBuilder(
                    requireContext(),
                    AccentColor(requireContext()).alertTheme()
                )
                alertDialog.setCancelable(false)
                alertDialog.setTitle(getString(R.string.history))
                alertDialog.setMessage(getString(R.string.what_would_you_like_to_do_with_history))
                alertDialog.setPositiveButton(getString(R.string.delete)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    val dbHandler = DBHelper(requireContext(), null)
                    dbHandler.deleteAll()
                    Toast.makeText(requireContext(), getString(R.string.history_deleted), Toast.LENGTH_SHORT).show()
                    val runnable = Runnable {
                        (context as MainActivity).changeBadgeNumber()
                    }

                    MainActivity().runOnUiThread(runnable)
                }
                alertDialog.setNeutralButton(getString(R.string.nothing)) { _, _ ->
                    Vibrate().vibration(requireContext())
                }
                alertDialog.create().show()
                toggleHistory.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                Toast.makeText(requireContext(), getString(R.string.history_disabled), Toast.LENGTH_SHORT).show()
            }
            historyToggleData.setHistoryToggle(toggleHistory.isChecked)
            val runnable = Runnable {
                (context as MainActivity).toggleHistory()
            }

            MainActivity().runOnUiThread(runnable)
        }

        val breakTextBoxVisiblityClass = BreakTextBoxVisibilityClass(requireContext())
        val toggleBreakTextBox = activity?.findViewById<MaterialSwitch>(R.id.breakTextBoxSwitch)

        if (breakTextBoxVisiblityClass.loadVisiblity() == 0) {
            toggleBreakTextBox?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            toggleBreakTextBox?.isChecked = true
        } else if (breakTextBoxVisiblityClass.loadVisiblity() == 1) {
            toggleBreakTextBox?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            toggleBreakTextBox?.isChecked = false
        }

        toggleBreakTextBox?.setOnClickListener {
            if (toggleBreakTextBox.isChecked) {
                toggleBreakTextBox.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                breakTextBoxVisiblityClass.setVisibility(0)
                Toast.makeText(requireContext(), getString(R.string.break_text_box_enabled), Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                toggleBreakTextBox.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                breakTextBoxVisiblityClass.setVisibility(1)
                Toast.makeText(requireContext(), getString(R.string.break_text_box_disabled), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        /*toggleBreakTextBox?.setOnCheckedChangeListener { buttonView, isChecked ->
            Vibrate().vibration(requireContext())
            if (isChecked) {
                toggleBreakTextBox.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                breakTextBoxVisiblityClass.setVisibility(0)
                Toast.makeText(requireContext(), getString(R.string.break_text_box_enabled), Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                toggleBreakTextBox.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                breakTextBoxVisiblityClass.setVisibility(1)
                Toast.makeText(requireContext(), getString(R.string.break_text_box_disabled), Toast.LENGTH_SHORT)
                    .show()
            }
        }*/

        val wagesData = WagesData(requireContext())
        val wagesEditText = activity?.findViewById<TextInputEditText>(R.id.Wages)

        val editable =
            Editable.Factory.getInstance().newEditable(wagesData.loadWageAmount().toString())
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
    }

    private fun reset() {
        val setOutTimeSwitch = view?.findViewById<MaterialSwitch>(R.id.setOutTimeSwitch)
        val calculationTypeSwitch = view?.findViewById<MaterialSwitch>(R.id.setCalculationTypeSwitch)
        val clickableHistorySwitch = view?.findViewById<MaterialSwitch>(R.id.clickableHistorySwitch)
        val vibrationSwitch= view?.findViewById<MaterialSwitch>(R.id.vibrationSwitch)
        val toggleHistory = view?.findViewById<MaterialSwitch>(R.id.historySwitch)
        val toggleBreakTextBox = view?.findViewById<MaterialSwitch>(R.id.breakTextBoxSwitch)
        val wagesEditText = view?.findViewById<TextInputEditText>(R.id.Wages)

        CalculationType(requireContext()).setCalculationState(true)
        VibrationData(requireContext()).setVibrationState(true)
        HistoryToggleData(requireContext()).setHistoryToggle(true)
        BreakTextBoxVisibilityClass(requireContext()).setVisibility(0)
        WagesData(requireContext()).setWageAmount(getString(R.string.wages_default))
        OutTimeData(requireContext()).setOutTimeState(true)
        ClickableHistoryEntry(requireContext()).setHistoryItemClickable(true)

        setOutTimeSwitch?.isChecked = true
        calculationTypeSwitch?.isChecked = true
        clickableHistorySwitch?.isChecked = true
        vibrationSwitch?.isChecked = true
        toggleHistory?.isChecked = true
        toggleBreakTextBox?.isChecked = true
        wagesEditText?.setText(getString(R.string.wages_default))

        setOutTimeSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        calculationTypeSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        clickableHistorySwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        vibrationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        toggleHistory?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        toggleBreakTextBox?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)

        val runnable = Runnable {
            (context as MainActivity).toggleHistory()
        }

        MainActivity().runOnUiThread(runnable)

        Toast.makeText(requireContext(), getString(R.string.app_settings_reset_to_default), Toast.LENGTH_LONG)
            .show()
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
}