package com.cory.hourcalculator.fragments

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.play.core.review.ReviewManagerFactory

class AboutAppFragment : Fragment() {

    private var packageName = "com.android.chrome"

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
        return inflater.inflate(R.layout.fragment_about_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        val cardView1 = requireActivity().findViewById<MaterialCardView>(R.id.cardView1)
        val cardView2 = requireActivity().findViewById<MaterialCardView>(R.id.cardView2)
        val cardView3 = requireActivity().findViewById<MaterialCardView>(R.id.cardView3)

        cardView1.shapeAppearanceModel = cardView1.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        cardView2.shapeAppearanceModel = cardView2.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        cardView3.shapeAppearanceModel = cardView3.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val versionNumberTextView = view.findViewById<TextView>(R.id.versionNumber)
        versionNumberTextView.text = getString(
            R.string.version_number_about,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        )

        updateCustomColor()

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarAboutApp)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val autoIcon = view.findViewById<ImageView>(R.id.appIconImageView)

        if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == getString(R.string.teal) || ChosenAppIconData(
                requireContext()
            ).loadChosenAppIcon() == getString(
                R.string.material_you
            )
        ) {
            autoIcon.setImageResource(R.drawable.hourcalclogoteal)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == getString(R.string.pink)) {
            autoIcon.setImageResource(R.drawable.hourcalclogopink)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == getString(R.string.orange)) {
            autoIcon.setImageResource(R.drawable.hourcalclogoorange)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == getString(R.string.red)) {
            autoIcon.setImageResource(R.drawable.hourcalclogored)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == getString(R.string.blue)) {
            autoIcon.setImageResource(R.drawable.hourcalclogoblue)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == getString(R.string.og)) {
            autoIcon.setImageResource(R.drawable.hourcalculatorlogoyellowgradient)
        } else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == getString(R.string.snow_falling)) {
            autoIcon.setImageResource(R.drawable.hourcalclogo_christmas)
        }

        val patchNotesTextView = activity?.findViewById<TextView>(R.id.patchNotes)
        val rateOnPlayStore = activity?.findViewById<TextView>(R.id.rate)
        val reportABug = activity?.findViewById<TextView>(R.id.reportABugTextView)
        val versionInfo = activity?.findViewById<TextView>(R.id.versionInfo)

        patchNotesTextView?.setOnClickListener {
            openFragment(PatchNotesFragment())
        }

        rateOnPlayStore?.setOnClickListener {
            leaveAReview()
        }

        reportABug?.setOnClickListener {
            Vibrate().vibration(requireContext())
            LinkData(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0/issues")
            openCustomTab()
        }

        versionInfo?.setOnClickListener {
            openFragment(VersionInfoFragment())
        }

        val githubLogoButton = activity?.findViewById<MaterialButton>(R.id.githubLogoButton)
        githubLogoButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            LinkData(requireContext()).setLink("https://github.com/corylowry12/")
            openCustomTab()
        }
    }

    private fun openCustomTab() {
        val builder = CustomTabsIntent.Builder()
        val params = CustomTabColorSchemeParams.Builder()
        params.setToolbarColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        builder.setDefaultColorSchemeParams(params.build())
        builder.setShowTitle(true)
        builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
        builder.setInstantAppsEnabled(true)
        val customBuilder = builder.build()

        if (this.isPackageInstalled(packageName)) {
            // if chrome is available use chrome custom tabs
            customBuilder.intent.setPackage(packageName)
            customBuilder.launchUrl(
                requireContext(),
                Uri.parse(LinkData(requireContext()).loadLink())
            )
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.there_was_an_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun leaveAReview() {
        Vibrate().vibration(requireContext())
        val reviewManager = ReviewManagerFactory.create(requireContext())
        val requestReviewFlow = reviewManager.requestReviewFlow()
        requestReviewFlow.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                val reviewInfo = request.result
                val flow = reviewManager.launchReviewFlow(requireActivity(), reviewInfo)
                flow.addOnCompleteListener {

                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.there_was_an_error_please_try_again),
                    Toast.LENGTH_SHORT
                ).show()
            }
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

    private fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.aboutAppCoordinatorLayout)
            .setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val cardView1 = requireActivity().findViewById<MaterialCardView>(R.id.cardView1)
        val cardView2 = requireActivity().findViewById<MaterialCardView>(R.id.cardView2)
        val cardView3 = requireActivity().findViewById<MaterialCardView>(R.id.cardView3)

        cardView1.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        cardView2.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        cardView3.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val collapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAbout)
        collapsingToolbarLayout.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )
        collapsingToolbarLayout.setStatusBarScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )

        collapsingToolbarLayout?.setExpandedTitleColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTitleBarExpandedTextColor()
            )
        )

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            collapsingToolbarLayout?.setCollapsedTitleTextColor(
                Color.parseColor(
                    CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()
                )
            )
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            collapsingToolbarLayout?.setCollapsedTitleTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    id
                )
            )
        }

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarAboutApp)

        val navigationDrawable = topAppBar?.navigationIcon
        navigationDrawable?.mutate()

        if (Build.VERSION.SDK_INT >= 29) {
            try {
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
            } catch (e: NoClassDefFoundError) {
                e.printStackTrace()
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                val id = typedValue.resourceId
                navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            }
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun isPackageInstalled(packageName: String): Boolean {
        // check if chrome is installed or not
        return try {
            activity?.packageManager?.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}