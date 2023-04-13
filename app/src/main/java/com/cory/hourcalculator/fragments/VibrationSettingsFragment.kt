package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
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
import com.cory.hourcalculator.sharedprefs.ColoredTitleBarTextData
import com.cory.hourcalculator.sharedprefs.MenuTintData
import com.cory.hourcalculator.sharedprefs.VibrationData
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily

class VibrationSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vibration_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateCustomColor()

        val toolbar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarVibrationSettings)

        toolbar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val vibrationOnClickCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibrationOnClick)
        val vibrationOnTimePickerCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibrationOnTimePickerChange)

        vibrationOnClickCardView.shapeAppearanceModel = vibrationOnClickCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        vibrationOnTimePickerCardView.shapeAppearanceModel = vibrationOnTimePickerCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val vibrationData = VibrationData(requireContext())
        val toggleVibration = activity?.findViewById<MaterialSwitch>(R.id.vibrationSwitch)

        if (vibrationData.loadVibrationOnClickState()) {
            toggleVibration?.isChecked = true
            toggleVibration?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!vibrationData.loadVibrationOnClickState()) {
            toggleVibration?.isChecked = false
            toggleVibration?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        vibrationOnClickCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleVibration!!.isChecked = !toggleVibration.isChecked
            vibrationData.setVibrationOnClickState(toggleVibration.isChecked)
            if (toggleVibration.isChecked) {
                toggleVibration.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleVibration.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        vibrationOnClickCardView.setOnLongClickListener {
            Vibrate().vibration(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the app will vibrate every time a button is clicked.\n\n" +
                        "When disabled the app will not vibrate every time a button is clicked."
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

        val toggleTimePickerVibration = activity?.findViewById<MaterialSwitch>(R.id.vibrationOnTimePickerChangeSwitch)

        if (vibrationData.loadVibrationOnTimePickerChangeState()) {
            toggleTimePickerVibration?.isChecked = true
            toggleTimePickerVibration?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!vibrationData.loadVibrationOnTimePickerChangeState()) {
            toggleTimePickerVibration?.isChecked = false
            toggleTimePickerVibration?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        vibrationOnTimePickerCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleTimePickerVibration!!.isChecked = !toggleTimePickerVibration.isChecked
            vibrationData.setVibrationOnTimePickerChangeState(toggleTimePickerVibration.isChecked)
            if (toggleTimePickerVibration.isChecked) {
                toggleTimePickerVibration.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleTimePickerVibration.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }
    }

    fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.vibrationSettingsCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))

        val collapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutVibrationSettings)
        collapsingToolbarLayout.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )

        val vibrateOnClickSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.vibrationSwitch)
        val vibrateOnTimePickerChangeSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.vibrationOnTimePickerChangeSwitch)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )
        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )

        vibrateOnClickSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        vibrateOnClickSwitch.trackTintList = ColorStateList(states, colors)
        vibrateOnTimePickerChangeSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        vibrateOnTimePickerChangeSwitch.trackTintList = ColorStateList(states, colors)

        val vibrationOnClickCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibrationOnClick)
        val vibrationOnTimePickerChangeCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibrationOnTimePickerChange)

        vibrationOnClickCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        vibrationOnTimePickerChangeCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutVibrationSettings)
            ?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutVibrationSettings)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutVibrationSettings)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarVibrationSettings)

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
}