package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputEditText
import java.lang.Exception

class AnimationSettingsFragment : Fragment() {

    private lateinit var dialog: BottomSheetDialog

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

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

        updateCustomColor()

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
        return inflater.inflate(R.layout.fragment_animation_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        val topAppBar =
            activity?.findViewById<MaterialToolbar>(R.id.materialToolbarAnimationSettings)

        topAppBar?.setNavigationOnClickListener {
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

        updateCustomColor()

        val historyRecyclerViewLoadingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.historyListLoadingAnimationCardView)
        val historyRecyclerViewScrollingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.historyScrollingAnimationCardView)
        val timeCardRecyclerViewLoadingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.timeCardListLoadingAnimationCardView)
        val timeCardRecyclerViewScrollingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.timeCardScrollingAnimationCardView)
        val imageLoadingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.imageLoadingAnimationCardView)

        historyRecyclerViewLoadingAnimationCardView.shapeAppearanceModel = historyRecyclerViewLoadingAnimationCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        historyRecyclerViewScrollingAnimationCardView.shapeAppearanceModel = historyRecyclerViewScrollingAnimationCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        timeCardRecyclerViewLoadingAnimationCardView.shapeAppearanceModel = timeCardRecyclerViewLoadingAnimationCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        timeCardRecyclerViewScrollingAnimationCardView.shapeAppearanceModel = timeCardRecyclerViewScrollingAnimationCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        imageLoadingAnimationCardView.shapeAppearanceModel = imageLoadingAnimationCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val animationData = AnimationData(requireContext())
        val toggleHistoryLoadingAnimation = requireActivity().findViewById<MaterialSwitch>(R.id.historyListLoadingAnimationSwitch)

        if (animationData.loadHistoryRecyclerViewLoadingAnimation()) {
            toggleHistoryLoadingAnimation?.isChecked = true
            toggleHistoryLoadingAnimation?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!animationData.loadHistoryRecyclerViewLoadingAnimation()) {
            toggleHistoryLoadingAnimation?.isChecked = false
            toggleHistoryLoadingAnimation?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        historyRecyclerViewLoadingAnimationCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleHistoryLoadingAnimation.isChecked = !toggleHistoryLoadingAnimation.isChecked
            if (toggleHistoryLoadingAnimation.isChecked) {
                toggleHistoryLoadingAnimation.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleHistoryLoadingAnimation?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            animationData.setHistoryRecyclerViewLoadingAnimation(toggleHistoryLoadingAnimation.isChecked)
        }

        historyRecyclerViewLoadingAnimationCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the recycler view in History will animate when being shown\n\n" +
                        "When disabled the recycler view in History will not animate when being shown"
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

        val toggleHistoryScrollingAnimation = requireActivity().findViewById<MaterialSwitch>(R.id.historyScrollingAnimationSwitch)

        if (animationData.loadHistoryScrollingAnimation()) {
            toggleHistoryScrollingAnimation?.isChecked = true
            toggleHistoryScrollingAnimation?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!animationData.loadHistoryScrollingAnimation()) {
            toggleHistoryScrollingAnimation?.isChecked = false
            toggleHistoryScrollingAnimation?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        historyRecyclerViewScrollingAnimationCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleHistoryScrollingAnimation.isChecked = !toggleHistoryScrollingAnimation.isChecked
            if (toggleHistoryScrollingAnimation.isChecked) {
                toggleHistoryScrollingAnimation.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleHistoryScrollingAnimation?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            animationData.setHistoryScrollingLoadingAnimation(toggleHistoryScrollingAnimation.isChecked)
        }

        historyRecyclerViewScrollingAnimationCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the recycler view in History will animate when scrolling.\n\n" +
                        "When disabled the recycler view in History will not animate when scrolling."
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

        val toggleTimeCardLoadingAnimationSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.timeCardListLoadingAnimationSwitch)

        if (animationData.loadTimeCardRecyclerViewLoadingAnimation()) {
            toggleTimeCardLoadingAnimationSwitch?.isChecked = true
            toggleTimeCardLoadingAnimationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!animationData.loadTimeCardRecyclerViewLoadingAnimation()) {
            toggleTimeCardLoadingAnimationSwitch?.isChecked = false
            toggleTimeCardLoadingAnimationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        timeCardRecyclerViewLoadingAnimationCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleTimeCardLoadingAnimationSwitch.isChecked = !toggleTimeCardLoadingAnimationSwitch.isChecked
            if (toggleTimeCardLoadingAnimationSwitch.isChecked) {
                toggleTimeCardLoadingAnimationSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleTimeCardLoadingAnimationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            animationData.setTimeCardRecyclerViewLoadingAnimation(toggleTimeCardLoadingAnimationSwitch.isChecked)
        }

        timeCardRecyclerViewLoadingAnimationCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the recycler view in Time Cards will animate when being shown.\n\n" +
                        "When disabled the recycler view in Time Cards will not animate when being shown."
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

        val toggleTimeCardScrollingAnimationSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.timeCardScrollingAnimationSwitch)

        if (animationData.loadTimeCardRecyclerViewLoadingAnimation()) {
            toggleTimeCardScrollingAnimationSwitch?.isChecked = true
            toggleTimeCardScrollingAnimationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!animationData.loadTimeCardRecyclerViewLoadingAnimation()) {
            toggleTimeCardScrollingAnimationSwitch?.isChecked = false
            toggleTimeCardScrollingAnimationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        timeCardRecyclerViewScrollingAnimationCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleTimeCardScrollingAnimationSwitch.isChecked = !toggleTimeCardScrollingAnimationSwitch.isChecked
            if (toggleTimeCardScrollingAnimationSwitch.isChecked) {
                toggleTimeCardScrollingAnimationSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleTimeCardScrollingAnimationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            animationData.setTimeCardsScrollingLoadingAnimation(toggleTimeCardScrollingAnimationSwitch.isChecked)
        }

        timeCardRecyclerViewScrollingAnimationCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the recycler view in Time Cards will animate when scrolling.\n\n" +
                        "When disabled the recycler view in Time Cards will not animate when scrolling."
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

        val imageLoadingAnimationSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.imageLoadingAnimationSwitch)

        if (animationData.loadImageAnimation()) {
            imageLoadingAnimationSwitch?.isChecked = true
            imageLoadingAnimationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!animationData.loadImageAnimation()) {
            imageLoadingAnimationSwitch?.isChecked = false
            imageLoadingAnimationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        imageLoadingAnimationCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            imageLoadingAnimationSwitch.isChecked = !imageLoadingAnimationSwitch.isChecked
            if (imageLoadingAnimationSwitch.isChecked) {
                imageLoadingAnimationSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                imageLoadingAnimationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            animationData.setImageAnimation(imageLoadingAnimationSwitch.isChecked)
        }

        imageLoadingAnimationCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the image will animate when clicked in Time Cards or Gallery.\n\n" +
                        "When disabled the image will not animate when clicked in Time Cards or Gallery."
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
    }

    fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.animationSettingsCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))

        val historyRecyclerViewLoadingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.historyListLoadingAnimationCardView)
        historyRecyclerViewLoadingAnimationCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        val historyRecyclerViewScrollingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.historyScrollingAnimationCardView)
        historyRecyclerViewScrollingAnimationCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        val timeCardRecyclerViewLoadingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.timeCardListLoadingAnimationCardView)
        timeCardRecyclerViewLoadingAnimationCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        val timeCardRecyclerViewScrollingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.timeCardScrollingAnimationCardView)
        timeCardRecyclerViewScrollingAnimationCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        val imageLoadingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.imageLoadingAnimationCardView)
        imageLoadingAnimationCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val toggleHistoryLoadingAnimationSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.historyListLoadingAnimationSwitch)
        val toggleHistoryScrollingAnimationSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.historyScrollingAnimationSwitch)
        val timeCardLoadingAnimationSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.timeCardListLoadingAnimationSwitch)
        val timeCardScrollingAnimationSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.timeCardScrollingAnimationSwitch)
        val imageLoadingAnimationSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.imageLoadingAnimationSwitch)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )

        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )

        toggleHistoryLoadingAnimationSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        toggleHistoryLoadingAnimationSwitch.trackTintList = ColorStateList(states, colors)
        toggleHistoryScrollingAnimationSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        toggleHistoryScrollingAnimationSwitch.trackTintList = ColorStateList(states, colors)
        timeCardLoadingAnimationSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        timeCardLoadingAnimationSwitch.trackTintList = ColorStateList(states, colors)
        timeCardScrollingAnimationSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        timeCardScrollingAnimationSwitch.trackTintList = ColorStateList(states, colors)
        imageLoadingAnimationSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        imageLoadingAnimationSwitch.trackTintList = ColorStateList(states, colors)

        val collapsingToolbarAnimationSettings = requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAnimationSettings)
        collapsingToolbarAnimationSettings.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        collapsingToolbarAnimationSettings.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        val topAppBar =
            activity?.findViewById<MaterialToolbar>(R.id.materialToolbarAnimationSettings)

        val navigationDrawable = topAppBar?.navigationIcon
        navigationDrawable?.mutate()

        val resetDrawable = topAppBar?.menu?.findItem(R.id.reset)?.icon
        resetDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
            navigationDrawable?.colorFilter = BlendModeColorFilter(
                Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                BlendMode.SRC_ATOP
            )
            resetDrawable?.colorFilter = BlendModeColorFilter(
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
            resetDrawable?.colorFilter = BlendModeColorFilter(
                ContextCompat.getColor(requireContext(), id),
                BlendMode.SRC_ATOP
            )
        }

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAnimationSettings)?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAnimationSettings)?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAnimationSettings)?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }
    }

    private fun resetMenuPress() {

        dialog = BottomSheetDialog(requireContext())
        val resetSettingsLayout =
            layoutInflater.inflate(R.layout.reset_settings_bottom_sheet, null)
        dialog.setContentView(resetSettingsLayout)
        dialog.setCancelable(false)
        resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text =
            "Would you like to reset Animation settings?"
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
        AnimationData(requireContext()).setHistoryRecyclerViewLoadingAnimation(false)
        AnimationData(requireContext()).setHistoryScrollingLoadingAnimation(false)
        AnimationData(requireContext()).setTimeCardRecyclerViewLoadingAnimation(false)
        AnimationData(requireContext()).setTimeCardsScrollingLoadingAnimation(false)
        AnimationData(requireContext()).setImageAnimation(true)

        val historyListLoadingAnimation =
            requireActivity().findViewById<MaterialSwitch>(R.id.historyListLoadingAnimationSwitch)
        val historyListScrollingAnimation =
            requireActivity().findViewById<MaterialSwitch>(R.id.historyScrollingAnimationSwitch)
        val timeCardListLoadingAnimation =
            requireActivity().findViewById<MaterialSwitch>(R.id.timeCardListLoadingAnimationSwitch)
        val timeCardListScrollingAnimation =
            requireActivity().findViewById<MaterialSwitch>(R.id.timeCardScrollingAnimationSwitch)
        val imageOpeningAnimation =
            requireActivity().findViewById<MaterialSwitch>(R.id.imageLoadingAnimationSwitch)

        historyListLoadingAnimation?.isChecked = false
        historyListLoadingAnimation?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        historyListScrollingAnimation?.isChecked = false
        historyListScrollingAnimation?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        timeCardListLoadingAnimation?.isChecked = false
        timeCardListLoadingAnimation?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        timeCardListScrollingAnimation?.isChecked = false
        timeCardListScrollingAnimation?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        imageOpeningAnimation?.isChecked = true
        imageOpeningAnimation?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
    }
}