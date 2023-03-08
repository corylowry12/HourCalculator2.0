package com.cory.hourcalculator.fragments

import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.net.Uri
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
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.play.core.review.ReviewManagerFactory

class AboutAppFragment : Fragment() {

    var themeSelection = false

    private var package_name = "com.android.chrome"

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
            accentColor.loadAccent() == 5 -> {
                activity?.theme?.applyStyle(R.style.transparent_accent, true)
            }
        }
        return inflater.inflate(R.layout.fragment_about_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val versionNumberTextView = view.findViewById<TextView>(R.id.versionNumber)
        versionNumberTextView.text = "Version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

        if (AccentColor(requireContext()).loadAccent() == 5) {
            updateCustomColor()
        }

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarAboutApp)

        val navigationDrawable = topAppBar?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            if (AccentColor(requireContext()).loadAccent() == 5) {
                navigationDrawable?.colorFilter = BlendModeColorFilter(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                    BlendMode.SRC_ATOP
                )
            }
            else {
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
                val id = typedValue.resourceId
                navigationDrawable?.colorFilter = BlendModeColorFilter(
                    ContextCompat.getColor(requireContext(), id),
                    BlendMode.SRC_ATOP
                )
            }
            }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        /*val autoIcon = view.findViewById<ImageView>(R.id.appIconImageView)
        val appIconCardView = view.findViewById<CardView>(R.id.appIconCardView)
        if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 0) {
            if (accentColor.loadAccent() == 0 || accentColor.loadAccent() == 4) {
                autoIcon.setImageResource(R.drawable.hourcalclogoteal)
                appIconCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }
            else if (accentColor.loadAccent() == 1) {
                appIconCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pinkAccent))
                autoIcon.setImageResource(R.drawable.hourcalclogopink)
            }
            else if (accentColor.loadAccent() == 2) {
                appIconCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orangeAccent))
                autoIcon.setImageResource(R.drawable.hourcalclogoorange)
            }
            else if (accentColor.loadAccent() == 3) {
                appIconCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.redAccent))
                autoIcon.setImageResource(R.drawable.hourcalclogored)
            }
        }
        else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 1) {
            autoIcon.setImageResource(R.drawable.hourcalclogoteal)
            appIconCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }
        else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 2) {
            appIconCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pinkIcon))
            autoIcon.setImageResource(R.drawable.hourcalclogopink)
        }
        else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 3) {
            appIconCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orangeAccent))
            autoIcon.setImageResource(R.drawable.hourcalclogoorange)
        }
        else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 4) {
            appIconCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.redIcon))
            autoIcon.setImageResource(R.drawable.hourcalclogored)
        }*/

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
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0/issues")
            openCustomTab()
        }

        versionInfo?.setOnClickListener {
            openFragment(VersionInfoFragment())
        }

        val githubLogoButton = activity?.findViewById<MaterialButton>(R.id.githubLogoButton)
        githubLogoButton?.setOnClickListener {
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/")
            openCustomTab()
        }
    }

    fun openCustomTab() {
        val builder = CustomTabsIntent.Builder()
        val params = CustomTabColorSchemeParams.Builder()
        val typedValue = TypedValue()
        (context as Activity).getTheme()
            .resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        params.setToolbarColor(ContextCompat.getColor(requireContext(), typedValue.resourceId))
        builder.setDefaultColorSchemeParams(params.build())
        builder.setShowTitle(true)
        builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
        builder.setInstantAppsEnabled(true)
        val customBuilder = builder.build()

        if (this.isPackageInstalled(package_name)) {
            // if chrome is available use chrome custom tabs
            customBuilder.intent.setPackage(package_name)
            customBuilder.launchUrl(requireContext(), Uri.parse(LinkClass(requireContext()).loadLink()))
        } else {
            Toast.makeText(requireContext(), getString(R.string.there_was_an_error), Toast.LENGTH_SHORT).show()
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

    fun updateCustomColor() {
        val cardView1 = requireActivity().findViewById<MaterialCardView>(R.id.cardView1)
        val cardView2 = requireActivity().findViewById<MaterialCardView>(R.id.cardView2)
        val cardView3 = requireActivity().findViewById<MaterialCardView>(R.id.cardView3)

        cardView1.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        cardView2.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        cardView3.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val collapsingToolbarLayout = requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAbout)
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        collapsingToolbarLayout.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
    }

    fun isPackageInstalled(packageName: String): Boolean {
        // check if chrome is installed or not
        return try {
            activity?.packageManager?.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}