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
import android.support.v4.os.IResultReceiver.Default
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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception

class TimeCardSettingsFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_time_card_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        val topAppBar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBarTimeCardSettings)
        topAppBar.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.reset -> {
                    resetMenuPress()
                    true
                }

                else -> true
            }
        }

        val toggleTimeCardsCardView = requireActivity().findViewById<MaterialCardView>(R.id.disableTimeCardsCardView)
        val showWagesInTimeCardsCardView = requireActivity().findViewById<MaterialCardView>(R.id.showWagesInTimeCardsCardView)
        val coloredImageViewBackgroundCardView = requireActivity().findViewById<MaterialCardView>(R.id.coloredImageViewBackgroundCardView)
        val defaultNameCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewDefaultName)
        val fabPositioningTimeCard = requireActivity().findViewById<MaterialCardView>(R.id.cardViewFABPositioningTimeCards)

        toggleTimeCardsCardView.shapeAppearanceModel =
            toggleTimeCardsCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
        showWagesInTimeCardsCardView.shapeAppearanceModel =
            showWagesInTimeCardsCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
        coloredImageViewBackgroundCardView.shapeAppearanceModel =
            coloredImageViewBackgroundCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
        defaultNameCardView.shapeAppearanceModel =
            defaultNameCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
        fabPositioningTimeCard.shapeAppearanceModel =
            fabPositioningTimeCard.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(28f)
                .setBottomLeftCornerSize(28f)
                .build()

        updateCustomColor()

        val toggleTimeCards = TimeCardsToggleData(requireContext())
        val toggleTimeCardSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.disableTimeCardsSwitch)

        if (toggleTimeCards.loadTimeCardsState()) {
            toggleTimeCardSwitch.isChecked = true
            toggleTimeCardSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            toggleTimeCardSwitch?.jumpDrawablesToCurrentState()
        }
        else {
            toggleTimeCardSwitch.isChecked = false
            toggleTimeCardSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            toggleTimeCardSwitch?.jumpDrawablesToCurrentState()
        }

        toggleTimeCardsCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleTimeCardSwitch.isChecked = !toggleTimeCardSwitch.isChecked
            if (toggleTimeCardSwitch.isChecked) {
                toggleTimeCardSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {

                val timeCardWarningBottomSheet = BottomSheetDialog(requireContext())
                val timeCardSettingsBottomSheet = LayoutInflater.from(context)
                    .inflate(R.layout.history_settings_warning_bottom_sheet, null)
                timeCardWarningBottomSheet.setContentView(timeCardSettingsBottomSheet)
                timeCardWarningBottomSheet.setCancelable(false)

                if (resources.getBoolean(R.bool.isTablet)) {
                    val bottomSheet =
                        timeCardWarningBottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                    val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    bottomSheetBehavior.skipCollapsed = true
                    bottomSheetBehavior.isHideable = false
                    bottomSheetBehavior.isDraggable = false
                }

                val title =
                    timeCardWarningBottomSheet.findViewById<TextView>(R.id.headingTextView)
                val body =
                    timeCardWarningBottomSheet.findViewById<TextView>(R.id.bodyTextView)
                val infoCardView =
                    timeCardWarningBottomSheet.findViewById<MaterialCardView>(R.id.bodyCardView)
                val yesButton =
                    timeCardWarningBottomSheet.findViewById<Button>(R.id.yesButton)
                val noButton =
                    timeCardWarningBottomSheet.findViewById<Button>(R.id.cancelButton)

                title!!.text = "Time Cards"
                body!!.text = "Would you like to clear Time Cards?"

                infoCardView!!.setCardBackgroundColor(
                    Color.parseColor(
                        CustomColorGenerator(
                            requireContext()
                        ).generateCardColor()
                    )
                )
                yesButton!!.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                noButton!!.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                yesButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                    timeCardWarningBottomSheet.dismiss()
                    val dbHandler = TimeCardDBHelper(requireContext(), null)
                    val dbHandler2 = TimeCardsItemDBHelper(requireContext(), null)
                    dbHandler.deleteAll()
                    dbHandler2.deleteAll()
                    Toast.makeText(
                        requireContext(),
                        "Time Cards deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    val changeBadgeNumber = Runnable {
                        (context as MainActivity).changeBadgeNumber()
                    }

                    MainActivity().runOnUiThread(changeBadgeNumber)
                }
                noButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                    timeCardWarningBottomSheet.dismiss()
                }
                timeCardWarningBottomSheet.show()
                toggleTimeCardSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            toggleTimeCards.setTimeCardsToggle(toggleTimeCardSwitch.isChecked)
            val toggleTimeCardsRunnable = Runnable {
                (context as MainActivity).toggleTimeCards()
            }

            MainActivity().runOnUiThread(toggleTimeCardsRunnable)
        }

        val showWagesInTimeCards = ShowWagesInTimeCardData(requireContext())
        val showWagesInTimeCardSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.showWagesInTimeCardsSwitch)

        if (showWagesInTimeCards.loadShowWages()) {
            showWagesInTimeCardSwitch.isChecked = true
            showWagesInTimeCardSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            showWagesInTimeCardSwitch?.jumpDrawablesToCurrentState()
        }
        else {
            showWagesInTimeCardSwitch.isChecked = false
            showWagesInTimeCardSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            showWagesInTimeCardSwitch?.jumpDrawablesToCurrentState()
        }

        showWagesInTimeCardsCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            showWagesInTimeCardSwitch.isChecked = !showWagesInTimeCardSwitch.isChecked
            showWagesInTimeCards.setShowWages(showWagesInTimeCardSwitch.isChecked)

            if (showWagesInTimeCardSwitch.isChecked) {
                showWagesInTimeCardSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            }
            else {
                showWagesInTimeCardSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        showWagesInTimeCardsCardView.setOnLongClickListener {
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
                "When enabled each time card item will show wages based on the number of hours that is calculated for that entry and the wages that are stored\n\n" +
                        "When disabled each time card item will not show wages"
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

        val imageViewBackground = MatchImageViewContentsBackgroundData(requireContext())
        val imageViewBackgroundSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.coloredImageViewBackgroundSwitch)

        if (imageViewBackground.loadMatchImageViewContents()) {
            imageViewBackgroundSwitch.isChecked = true
            imageViewBackgroundSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            imageViewBackgroundSwitch?.jumpDrawablesToCurrentState()
        }
        else {
            imageViewBackgroundSwitch.isChecked = false
            imageViewBackgroundSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            imageViewBackgroundSwitch?.jumpDrawablesToCurrentState()
        }

        coloredImageViewBackgroundCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            imageViewBackgroundSwitch.isChecked = !imageViewBackgroundSwitch.isChecked
            imageViewBackground.setMatchImageViewContents(imageViewBackgroundSwitch.isChecked)

            if (imageViewBackgroundSwitch.isChecked) {
                imageViewBackgroundSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            }
            else {
                imageViewBackgroundSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        coloredImageViewBackgroundCardView.setOnLongClickListener {
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
                "When enabled the background when viewing a time card image will match the primary color of the image\n\n" +
                        "When disabled the background will be gray when viewing image"
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

        val defaultTimeCardNameData = DefaultTimeCardNameData(requireContext())
        val defaultNameEditText = requireActivity().findViewById<TextInputEditText>(R.id.defaultTimeCardName)

        val defaultNameEditTextLayout =
            activity?.findViewById<TextInputLayout>(R.id.outlinedTextFieldDefaultName)

        defaultNameEditTextLayout?.boxStrokeColor = Color.parseColor("#000000")
        defaultNameEditTextLayout?.hintTextColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                defaultNameEditText.textCursorDrawable = null
            }
        defaultNameEditTextLayout?.defaultHintTextColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        defaultNameEditText.highlightColor =
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        defaultNameEditText.setTextIsSelectable(false)

        val editable =
            Editable.Factory.getInstance().newEditable(defaultTimeCardNameData.loadDefaultName().trim())
        defaultNameEditText?.text = editable

        defaultNameEditText?.setOnKeyListener(View.OnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard(defaultNameEditText)
                return@OnKeyListener true
            }
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                defaultTimeCardNameData.setDefaultName(defaultNameEditText.text.toString().trim())
                hideKeyboard(defaultNameEditText)
                return@OnKeyListener true
            }
            false
        })

        defaultNameEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    defaultTimeCardNameData.setDefaultName(s.toString().trim())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                defaultTimeCardNameData.setDefaultName(s.toString().trim())
            }
        })

        fabPositioningTimeCard.setOnClickListener {
            Vibrate().vibration(requireContext())
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (AnimationData(requireContext()).loadSettingsFragmentSwitchingAnimation()) {
                transaction?.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
            }
            transaction?.replace(R.id.fragment_container, TimeCardFABPositioningFragment())?.addToBackStack(null)
            transaction?.commit()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (defaultNameEditText!!.hasFocus()) {
                        defaultNameEditText.clearFocus()
                    } else {
                        activity?.supportFragmentManager?.popBackStack()
                    }
                }
            })
    }

    fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.coordinatorLayoutTimeCardSettings).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val collapsingToolbarLayout = requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutTimeCardSettings)
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        collapsingToolbarLayout.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        requireActivity().findViewById<MaterialCardView>(R.id.disableTimeCardsCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        requireActivity().findViewById<MaterialCardView>(R.id.cardViewDefaultName).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        requireActivity().findViewById<MaterialCardView>(R.id.coloredImageViewBackgroundCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        requireActivity().findViewById<MaterialCardView>(R.id.showWagesInTimeCardsCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        requireActivity().findViewById<MaterialCardView>(R.id.cardViewFABPositioningTimeCards).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val toggleTimeCardsSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.disableTimeCardsSwitch)
        val showWagesInTimeCardsSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.showWagesInTimeCardsSwitch)
        val coloredBackgroundSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.coloredImageViewBackgroundSwitch)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )
        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )

        toggleTimeCardsSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        toggleTimeCardsSwitch.trackTintList = ColorStateList(states, colors)
        showWagesInTimeCardsSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        showWagesInTimeCardsSwitch.trackTintList = ColorStateList(states, colors)
        coloredBackgroundSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        coloredBackgroundSwitch.trackTintList = ColorStateList(states, colors)

        val topAppBar =
            requireActivity().findViewById<MaterialToolbar>(R.id.topAppBarTimeCardSettings)

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

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutTimeCardSettings)
            ?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutTimeCardSettings)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutTimeCardSettings)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
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
            "Would you like to reset Time Card settings?"
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

    }

    private fun reset() {
        ShowWagesInTimeCardData(requireContext()).setShowWages(false)
        MatchImageViewContentsBackgroundData(requireContext()).setMatchImageViewContents(false)

        val showWagesSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.showWagesInTimeCardsSwitch)
        val coloredImageBackgroundSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.coloredImageViewBackgroundSwitch)

        showWagesSwitch?.isChecked = false
        showWagesSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        coloredImageBackgroundSwitch?.isChecked = false
        coloredImageBackgroundSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)

        DefaultTimeCardNameData(requireContext()).setDefaultName("")
        val editable =
            Editable.Factory.getInstance().newEditable(DefaultTimeCardNameData(requireContext()).loadDefaultName())
        val defaultNameEditText = requireActivity().findViewById<TextInputEditText>(R.id.defaultTimeCardName)
        defaultNameEditText?.text = editable

        hideKeyboard(defaultNameEditText)
    }
}