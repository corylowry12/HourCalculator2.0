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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.ColoredTitleBarTextData
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.cory.hourcalculator.sharedprefs.LinkData
import com.cory.hourcalculator.sharedprefs.MenuTintData
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily

class VersionInfoFragment : Fragment() {

    private var package_name = "com.android.chrome"

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        updateCustomTheme()

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
        return inflater.inflate(R.layout.fragment_version_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        updateCustomTheme()

        val topAppBarVersionInfo =
            activity?.findViewById<MaterialToolbar>(R.id.topAppBarVersionInfo)

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

        val versionHeadingCardView = requireActivity().findViewById<MaterialCardView>(R.id.versionHeadingCardView)
        val buildNumberCardView = requireActivity().findViewById<MaterialCardView>(R.id.buildNumberCardView)
        val dateUpdatedCardView = requireActivity().findViewById<MaterialCardView>(R.id.dateUpdatedCardView)
        val materialDependencyCardView = requireActivity().findViewById<MaterialCardView>(R.id.materialDependencyCardView)
        val googleAdsDependencyCardView = requireActivity().findViewById<MaterialCardView>(R.id.googleAdsDependencyCardView)
        val firebaseAnalyticsDependencyCardView = requireActivity().findViewById<MaterialCardView>(R.id.firebaseAnalyticsDependencyCardView)
        val firebaseCrashlyticsDependencyCardView = requireActivity().findViewById<MaterialCardView>(R.id.firebaseCrashlyticsDependencyCardView)
        val firebasePerformanceMonitoringDependencyCardView = requireActivity().findViewById<MaterialCardView>(R.id.firebasePerformanceMonitoringDependencyCardView)
        val inAppReviewDependencyCardView = requireActivity().findViewById<MaterialCardView>(R.id.inAppReviewDependencyCardView)
        val chromeCustomTabsCardView = requireActivity().findViewById<MaterialCardView>(R.id.inAppBrowserDependencyCardView)
        val glideCardView = requireActivity().findViewById<MaterialCardView>(R.id.glideDependencyCardView)
        val touchImageViewCardView = requireActivity().findViewById<MaterialCardView>(R.id.imageViewDependencyCardView)
        val paletteCardView = requireActivity().findViewById<MaterialCardView>(R.id.paletteDependencyCardView)

        versionHeadingCardView.shapeAppearanceModel = versionHeadingCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        buildNumberCardView.shapeAppearanceModel = buildNumberCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        dateUpdatedCardView.shapeAppearanceModel = dateUpdatedCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        materialDependencyCardView.shapeAppearanceModel = materialDependencyCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        googleAdsDependencyCardView.shapeAppearanceModel = googleAdsDependencyCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        firebaseAnalyticsDependencyCardView.shapeAppearanceModel = firebaseAnalyticsDependencyCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        firebaseCrashlyticsDependencyCardView.shapeAppearanceModel = firebaseCrashlyticsDependencyCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        firebasePerformanceMonitoringDependencyCardView.shapeAppearanceModel = firebasePerformanceMonitoringDependencyCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
       inAppReviewDependencyCardView.shapeAppearanceModel = inAppReviewDependencyCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        chromeCustomTabsCardView.shapeAppearanceModel = chromeCustomTabsCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        glideCardView.shapeAppearanceModel = glideCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        touchImageViewCardView.shapeAppearanceModel = touchImageViewCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        paletteCardView.shapeAppearanceModel = paletteCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val materialSubtitle = view.findViewById<TextView>(R.id.materialSubtitle)

        materialDependencyCardView.setOnClickListener {
            LinkData(requireContext()).setLink(materialSubtitle.text.toString())
            openCustomTab()
        }

        val versionNumber = view.findViewById<TextView>(R.id.versionNumber)
        val buildNumber = view.findViewById<TextView>(R.id.buildNumber)

        versionNumber.text = BuildConfig.VERSION_NAME
        buildNumber.text = BuildConfig.VERSION_CODE.toString()

        val googleAdsSubtitle = view.findViewById<TextView>(R.id.googleAdsSubtitle)

        googleAdsDependencyCardView.setOnClickListener {
            LinkData(requireContext()).setLink(googleAdsSubtitle.text.toString())
            openCustomTab()
        }

        val firebaseAnalyticsSubtitle = view.findViewById<TextView>(R.id.firebaseAnalyticsSubtitle)

        firebaseAnalyticsDependencyCardView.setOnClickListener {
            LinkData(requireContext()).setLink(firebaseAnalyticsSubtitle.text.toString())
            openCustomTab()
        }

        val firebaseCrashlyticsSubtitle = view.findViewById<TextView>(R.id.firebaseCrashlyticsSubtitle)

        firebaseCrashlyticsDependencyCardView.setOnClickListener {
            LinkData(requireContext()).setLink(firebaseCrashlyticsSubtitle.text.toString())
            openCustomTab()
        }

        val firebasePerfSubtitle = view.findViewById<TextView>(R.id.firebasePerfSubtitle)

        firebasePerformanceMonitoringDependencyCardView.setOnClickListener {
            LinkData(requireContext()).setLink(firebasePerfSubtitle.text.toString())
            openCustomTab()
        }

        val inAppReviewSubtitle = view.findViewById<TextView>(R.id.inAppReviewSubtitle)

        inAppReviewDependencyCardView.setOnClickListener {
            LinkData(requireContext()).setLink(inAppReviewSubtitle.text.toString())
            openCustomTab()
        }

        val chromeCustomTabsSubtitle = view.findViewById<TextView>(R.id.inAppBrowserSubtitle)

        chromeCustomTabsCardView.setOnClickListener {
            LinkData(requireContext()).setLink(chromeCustomTabsSubtitle.text.toString())
            openCustomTab()
        }

        val glideSubtitle = view.findViewById<TextView>(R.id.glideSubtitle)

        glideCardView.setOnClickListener {
            LinkData(requireContext()).setLink(glideSubtitle.text.toString())
            openCustomTab()
        }

        val touchImageViewSubtitle = view.findViewById<TextView>(R.id.imageViewDependencySubtitle)

        touchImageViewCardView.setOnClickListener {
            LinkData(requireContext()).setLink(touchImageViewSubtitle.text.toString())
            openCustomTab()
        }

        val paletteSubtitle = view.findViewById<TextView>(R.id.paletteDependencySubtitle)

        paletteCardView.setOnClickListener {
            LinkData(requireContext()).setLink(paletteSubtitle.text.toString())
            openCustomTab()
        }
    }

    private fun updateCustomTheme() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.coordinatorLayoutVersionInfo).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))

        val versionHeadingCardView = requireActivity().findViewById<MaterialCardView>(R.id.versionHeadingCardView)
        val buildNumberCardView = requireActivity().findViewById<MaterialCardView>(R.id.buildNumberCardView)
        val dateUpdatedCardView = requireActivity().findViewById<MaterialCardView>(R.id.dateUpdatedCardView)
        val materialDependencyCardView = requireActivity().findViewById<MaterialCardView>(R.id.materialDependencyCardView)
        val googleAdsDependencyCardView = requireActivity().findViewById<MaterialCardView>(R.id.googleAdsDependencyCardView)
        val firebaseAnalyticsDependencyCardView = requireActivity().findViewById<MaterialCardView>(R.id.firebaseAnalyticsDependencyCardView)
        val firebaseCrashlyticsDependencyCardView = requireActivity().findViewById<MaterialCardView>(R.id.firebaseCrashlyticsDependencyCardView)
        val firebasePerformanceMonitoringDependencyCardView = requireActivity().findViewById<MaterialCardView>(R.id.firebasePerformanceMonitoringDependencyCardView)
        val inAppReviewDependencyCardView = requireActivity().findViewById<MaterialCardView>(R.id.inAppReviewDependencyCardView)
        val chromeCustomTabsCardView = requireActivity().findViewById<MaterialCardView>(R.id.inAppBrowserDependencyCardView)
        val glideCardView = requireActivity().findViewById<MaterialCardView>(R.id.glideDependencyCardView)
        val touchImageViewCardView = requireActivity().findViewById<MaterialCardView>(R.id.imageViewDependencyCardView)
        val paletteCardView = requireActivity().findViewById<MaterialCardView>(R.id.paletteDependencyCardView)

        versionHeadingCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        buildNumberCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        dateUpdatedCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        materialDependencyCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        googleAdsDependencyCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        firebaseAnalyticsDependencyCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        firebaseCrashlyticsDependencyCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        firebasePerformanceMonitoringDependencyCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        inAppReviewDependencyCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        chromeCustomTabsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        glideCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        touchImageViewCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        paletteCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val topAppBarVersionInfo =
            activity?.findViewById<MaterialToolbar>(R.id.topAppBarVersionInfo)

        val navigationDrawable = topAppBarVersionInfo?.navigationIcon
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

        val collapsingToolbarLayout = requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutVersionInfo)
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        collapsingToolbarLayout.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        collapsingToolbarLayout?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            collapsingToolbarLayout?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            collapsingToolbarLayout?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }
    }

    private fun openCustomTab() {
        Vibrate().vibration(requireContext())
        val builder = CustomTabsIntent.Builder()
        val params = CustomTabColorSchemeParams.Builder()

        params.setToolbarColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        builder.setDefaultColorSchemeParams(params.build())
        builder.setShowTitle(true)
        builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
        builder.setInstantAppsEnabled(true)
        val customBuilder = builder.build()

        if (this.isPackageInstalled(package_name)) {
            // if chrome is available use chrome custom tabs
            customBuilder.intent.setPackage(package_name)
            customBuilder.launchUrl(requireContext(), Uri.parse(LinkData(requireContext()).loadLink()))
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