package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
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
        }
        return inflater.inflate(R.layout.fragment_about_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val versionNumberTextView = view.findViewById<TextView>(R.id.versionNumber)
        versionNumberTextView.text = "Version: ${getString(R.string.version_number)} (${getString(R.string.build_number_number)})"
        val accentColor = AccentColor(requireContext())

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarAboutApp)
        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val autoIcon = view.findViewById<ImageView>(R.id.appIconImageView)
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
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0/issues")

            val builder = CustomTabsIntent.Builder()

            // to set the toolbar color use CustomTabColorSchemeParams
            // since CustomTabsIntent.Builder().setToolBarColor() is deprecated

            val params = CustomTabColorSchemeParams.Builder()
            params.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            builder.setDefaultColorSchemeParams(params.build())

            // shows the title of web-page in toolbar
            builder.setShowTitle(true)

            // setShareState(CustomTabsIntent.SHARE_STATE_ON) will add a menu to share the web-page
            builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)

            // To modify the close button, use
            // builder.setCloseButtonIcon(bitmap)

            // to set weather instant apps is enabled for the custom tab or not, use
            builder.setInstantAppsEnabled(true)

            //  To use animations use -
            //  builder.setStartAnimations(this, android.R.anim.start_in_anim, android.R.anim.start_out_anim)
            //  builder.setExitAnimations(this, android.R.anim.exit_in_anim, android.R.anim.exit_out_anim)
            val customBuilder = builder.build()

            if (this.isPackageInstalled(package_name)) {
                // if chrome is available use chrome custom tabs
                customBuilder.intent.setPackage(package_name)
                customBuilder.launchUrl(requireContext(), Uri.parse(LinkClass(requireContext()).loadLink()))
            } else {
                openFragment(WebviewFragment())
            }
        }

        versionInfo?.setOnClickListener {
            openFragment(AboutFragment())
        }

        val githubLogoButton = activity?.findViewById<MaterialButton>(R.id.githubLogoButton)
        githubLogoButton?.setOnClickListener {
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/")
            openFragment(WebviewFragment())
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