package com.cory.hourcalculator.fragments

import android.content.Intent
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily

class BackgroundColorFragment : Fragment() {

    var themeSelection = false
    private lateinit var followSystemImageViewDrawable: Drawable

    private fun setDrawable(): Drawable {
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

        return ContextCompat.getDrawable(requireContext(), R.drawable.blackcircleimageview)!!
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
        return inflater.inflate(R.layout.fragment_background_color, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBarBackgroundColorFragment = view.findViewById<MaterialToolbar>(R.id.materialToolBarBackgroundColorFragment)
        topAppBarBackgroundColorFragment?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val resetDrawable = topAppBarBackgroundColorFragment?.menu?.findItem(R.id.reset)?.icon
        val navigationDrawable = topAppBarBackgroundColorFragment?.navigationIcon
        resetDrawable?.mutate()
        navigationDrawable?.mutate()
        val typedValue = TypedValue()
        activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
        val id = typedValue.resourceId
        if (MenuTintData(requireContext()).loadMenuTint()) {
            resetDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }

        topAppBarBackgroundColorFragment.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.reset -> {
                    Vibrate().vibration(requireContext())
                    val dialog = BottomSheetDialog(requireContext())
                    val resetSettingsLayout = layoutInflater.inflate(R.layout.reset_settings_bottom_sheet, null)
                    dialog.setContentView(resetSettingsLayout)
                    dialog.setCancelable(false)
                    resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text = "Would you like to reset Background Color Settings?"
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
                else -> false
            }
        }

        val lightThemeCardView = view.findViewById<MaterialCardView>(R.id.lightThemeCardViewBackgroundColor)
        val darkThemeCardView = view.findViewById<MaterialCardView>(R.id.darkThemeCardViewBackgroundColor)
        val followSystemThemeCardView = view.findViewById<MaterialCardView>(R.id.followSystemThemeCardViewBackgroundColor)
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
        followSystemThemeCardView.shapeAppearanceModel = followSystemThemeCardView.shapeAppearanceModel
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

        val lightThemeConstraintLayout = activity?.findViewById<ConstraintLayout>(R.id.lightThemeConstraintLayout)
        val darkThemeConstraintLayout = activity?.findViewById<ConstraintLayout>(R.id.darkThemeConstraintLayout)
        val followSystemConstraintLayout = activity?.findViewById<ConstraintLayout>(R.id.followSystemConstraintLayout)

        lightThemeConstraintLayout?.setOnClickListener {
            changeLightTheme(
                darkThemeData,
                amoledThemeButton,
                followSystemThemeButton,
                lightThemeButton!!
            )
        }
        darkThemeConstraintLayout?.setOnClickListener {
            changeToDark(
                darkThemeData,
                lightThemeButton,
                followSystemThemeButton,
                amoledThemeButton!!
            )
        }
        followSystemConstraintLayout?.setOnClickListener {
            changeToFollowSystem(
                darkThemeData,
                lightThemeButton,
                amoledThemeButton,
                followSystemThemeButton!!
            )
        }

        val followSystemImageView = activity?.findViewById<ImageView>(R.id.followSystemImageView)
        followSystemImageView?.setImageDrawable(setDrawable())
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

        lightThemeButton?.setOnClickListener {
            changeLightTheme(
                darkThemeData,
                amoledThemeButton,
                followSystemThemeButton,
                lightThemeButton
            )
        }
        amoledThemeButton?.setOnClickListener {
            changeToDark(
                darkThemeData,
                lightThemeButton,
                followSystemThemeButton,
                amoledThemeButton
            )
        }
        followSystemThemeButton?.setOnClickListener {
            changeToFollowSystem(
                darkThemeData,
                lightThemeButton,
                amoledThemeButton,
                followSystemThemeButton
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
        if (darkThemeData.loadDarkModeState() == 3) {
            Toast.makeText(
                requireContext(),
                getString(R.string.system_theme_already_enabled),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (FollowSystemVersion(requireContext()).loadSystemColor() && AccentColor(
                    requireContext()
                ).loadAccent() == 4
            ) {
                /*val alert =
                        MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                    alert.setTitle(getString(R.string.warning))
                    alert.setMessage(getString(R.string.followSystemGoogleDialogWarning))
                    alert.setPositiveButton(getString(R.string.yes)) { _, _ ->*/
                val dialog = BottomSheetDialog(requireContext())
                val warningLayout =
                    layoutInflater.inflate(R.layout.match_google_apps_warning_bottom_sheet, null)
                dialog.setContentView(warningLayout)
                dialog.setCancelable(false)
                val yesButton = warningLayout.findViewById<Button>(R.id.yesButton)
                val noButton = warningLayout.findViewById<Button>(R.id.noButton)
                yesButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                    darkThemeData.setDarkModeState(3)
                    lightThemeButton?.isChecked = false

                    amoledThemeButton?.isChecked = false
                    dialog.dismiss()
                    restartApplication()
                }
                // alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                noButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                    when {
                        darkThemeData.loadDarkModeState() == 0 -> {
                            lightThemeButton?.isChecked = true
                        }
                        darkThemeData.loadDarkModeState() == 1 -> {

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
            } else {
                darkThemeData.setDarkModeState(3)
                lightThemeButton?.isChecked = false
                amoledThemeButton?.isChecked = false
                restartThemeChange()
            }

        }
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
            if (FollowSystemVersion(requireContext()).loadSystemColor() && AccentColor(
                    requireContext()
                ).loadAccent() == 4
            ) {
                /*val alert =
                        MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                    alert.setTitle(getString(R.string.warning))
                    alert.setMessage(getString(R.string.followSystemGoogleDialogWarning))
                    alert.setPositiveButton(getString(R.string.yes)) { _, _ ->*/
                val dialog = BottomSheetDialog(requireContext())
                val warningLayout =
                    layoutInflater.inflate(R.layout.match_google_apps_warning_bottom_sheet, null)
                dialog.setContentView(warningLayout)
                dialog.setCancelable(false)
                val yesButton = warningLayout.findViewById<Button>(R.id.yesButton)
                val noButton = warningLayout.findViewById<Button>(R.id.noButton)
                yesButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                    darkThemeData.setDarkModeState(2)
                    lightThemeButton?.isChecked = false

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
            } else {
                darkThemeData.setDarkModeState(2)
                lightThemeButton?.isChecked = false

                followSystemThemeButton?.isChecked = false
                restartThemeChange()
            }
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
            if (FollowSystemVersion(requireContext()).loadSystemColor() && AccentColor(
                    requireContext()
                ).loadAccent() == 4
            ) {
                /*val alert =
                        MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                    alert.setTitle(getString(R.string.warning))
                    alert.setMessage(getString(R.string.followSystemGoogleDialogWarning))
                    alert.setPositiveButton(getString(R.string.yes)) { _, _ ->*/

                val dialog = BottomSheetDialog(requireContext())
                val warningLayout =
                    layoutInflater.inflate(R.layout.match_google_apps_warning_bottom_sheet, null)
                dialog.setContentView(warningLayout)
                dialog.setCancelable(false)
                val yesButton = warningLayout.findViewById<Button>(R.id.yesButton)
                val noButton = warningLayout.findViewById<Button>(R.id.noButton)
                yesButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                    darkThemeData.setDarkModeState(0)

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
            } else {
                darkThemeData.setDarkModeState(0)
                amoledThemeButton?.isChecked = false
                followSystemThemeButton?.isChecked = false
                restartThemeChange()
            }
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

    fun reset() {
        val lightThemeRadioButton = view?.findViewById<RadioButton>(R.id.lightTheme)
        val darkThemeRadioButton = view?.findViewById<RadioButton>(R.id.blackTheme)
        val followSystemRadioButton = view?.findViewById<RadioButton>(R.id.followSystem)

        lightThemeRadioButton?.isChecked = false
        darkThemeRadioButton?.isChecked = false
        followSystemRadioButton?.isChecked = true

        DarkThemeData(requireContext()).setDarkModeState(3)
        restartThemeChange()
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