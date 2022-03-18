package com.cory.hourcalculator.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class AboutFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val materialConstraint = view.findViewById<ConstraintLayout>(R.id.materialConstraintLayout)
        val materialSubtitle = view.findViewById<TextView>(R.id.materialSubtitle)

        materialConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(materialSubtitle.text.toString())
            openFragment(WebviewFragment())
        }

        val googleAdsConstraint = view.findViewById<ConstraintLayout>(R.id.googleAdsConstraint)
        val googleAdsSubtitle = view.findViewById<TextView>(R.id.googleAdsSubtitle)

        googleAdsConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(googleAdsSubtitle.text.toString())
            openFragment(WebviewFragment())
        }

        val firebaseAnalyticsConstraint = view.findViewById<ConstraintLayout>(R.id.firebaseAnalyticsConstraint)
        val firebaseAnalyticsSubtitle = view.findViewById<TextView>(R.id.firebaseAnalyticsSubtitle)

        firebaseAnalyticsConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(firebaseAnalyticsSubtitle.text.toString())
            openFragment(WebviewFragment())
        }

        val firebaseCrashlyticsConstraint = view.findViewById<ConstraintLayout>(R.id.firebaseCrashlyticsConstraint)
        val firebaseCrashlyticsSubtitle = view.findViewById<TextView>(R.id.firebaseCrashlyticsSubtitle)

        firebaseCrashlyticsConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(firebaseCrashlyticsSubtitle.text.toString())
            openFragment(WebviewFragment())
        }

        val firebasePerfConstraint = view.findViewById<ConstraintLayout>(R.id.firebasePerfConstraint)
        val firebasePerfSubtitle = view.findViewById<TextView>(R.id.firebasePerfSubtitle)

        firebasePerfConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(firebasePerfSubtitle.text.toString())
            openFragment(WebviewFragment())
        }

        val inAppReviewConstraint = view.findViewById<ConstraintLayout>(R.id.inAppReviewConstraint)
        val inAppReviewSubtitle = view.findViewById<TextView>(R.id.inAppReviewSubtitle)

        inAppReviewConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(inAppReviewSubtitle.text.toString())
            openFragment(WebviewFragment())
        }

        val appUpdaterConstraint = view.findViewById<ConstraintLayout>(R.id.appUpdaterConstraint)
        val appUpdaterSubtitle = view.findViewById<TextView>(R.id.appUpdaterSubtitle)

        appUpdaterConstraint.setOnClickListener {
            LinkClass(requireContext()).setLink(appUpdaterSubtitle.text.toString())
            openFragment(WebviewFragment())
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
}