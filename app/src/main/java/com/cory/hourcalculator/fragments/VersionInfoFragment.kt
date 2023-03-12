package com.cory.hourcalculator.fragments

import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class VersionInfoFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_version_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBarVersionInfo =
            activity?.findViewById<MaterialToolbar>(R.id.topAppBarVersionInfo)

        val navigationDrawable = topAppBarVersionInfo?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }

        topAppBarVersionInfo?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.supportFragmentManager?.popBackStack()
                }
            })

        val materialConstraint = view.findViewById<ConstraintLayout>(R.id.materialConstraintLayout)
        val materialSubtitle = view.findViewById<TextView>(R.id.materialSubtitle)

        materialConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(materialSubtitle.text.toString())
            openCustomTab()
        }

        val versionNumber = view.findViewById<TextView>(R.id.versionNumber)
        val buildNumber = view.findViewById<TextView>(R.id.buildNumber)

        versionNumber.text = BuildConfig.VERSION_NAME
        buildNumber.text = BuildConfig.VERSION_CODE.toString()

        val googleAdsConstraint = view.findViewById<ConstraintLayout>(R.id.googleAdsConstraint)
        val googleAdsSubtitle = view.findViewById<TextView>(R.id.googleAdsSubtitle)

        googleAdsConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(googleAdsSubtitle.text.toString())
            openCustomTab()
        }

        val firebaseAnalyticsConstraint = view.findViewById<ConstraintLayout>(R.id.firebaseAnalyticsConstraint)
        val firebaseAnalyticsSubtitle = view.findViewById<TextView>(R.id.firebaseAnalyticsSubtitle)

        firebaseAnalyticsConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(firebaseAnalyticsSubtitle.text.toString())
            openCustomTab()
        }

        val firebaseCrashlyticsConstraint = view.findViewById<ConstraintLayout>(R.id.firebaseCrashlyticsConstraint)
        val firebaseCrashlyticsSubtitle = view.findViewById<TextView>(R.id.firebaseCrashlyticsSubtitle)

        firebaseCrashlyticsConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(firebaseCrashlyticsSubtitle.text.toString())
            openCustomTab()
        }

        val firebasePerfConstraint = view.findViewById<ConstraintLayout>(R.id.firebasePerfConstraint)
        val firebasePerfSubtitle = view.findViewById<TextView>(R.id.firebasePerfSubtitle)

        firebasePerfConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(firebasePerfSubtitle.text.toString())
            openCustomTab()
        }

        val inAppReviewConstraint = view.findViewById<ConstraintLayout>(R.id.inAppReviewConstraint)
        val inAppReviewSubtitle = view.findViewById<TextView>(R.id.inAppReviewSubtitle)

        inAppReviewConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(inAppReviewSubtitle.text.toString())
            openCustomTab()
        }
    }

    private fun openCustomTab() {
        val builder = CustomTabsIntent.Builder()
        val params = CustomTabColorSchemeParams.Builder()
        val typedValue = TypedValue()
        (context as Activity).theme
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