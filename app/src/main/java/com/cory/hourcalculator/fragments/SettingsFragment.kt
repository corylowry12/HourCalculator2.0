package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class SettingsFragment : Fragment() {

    var themeSelection = false
    private var scrollPosition = 0

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
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.setBackgroundDrawable(null)

        val inputManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)

        main()

        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nestedScrollViewSettings)

        nestedScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->

            scrollPosition = scrollX
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        main()

    }

    override fun onResume() {
        super.onResume()
        main()

        //val nestedScrollView = requireView().findViewById<NestedScrollView>(R.id.nestedScrollViewSettings)

        //nestedScrollView.scrollTo(scrollPosition, 0)
    }

    private fun main() {

        val appearanceConstraintLayout =
            view?.findViewById<ConstraintLayout>(R.id.constraintAppearance)

        appearanceConstraintLayout?.setOnClickListener {
            (context as MainActivity).currentSettingsItem = 0
            openFragment(AppearanceFragment())
        }

        val appSettingsConstraint = view?.findViewById<ConstraintLayout>(R.id.constraintAppSettings)

        appSettingsConstraint?.setOnClickListener {
            (context as MainActivity).currentSettingsItem = 1
            openFragment(AppSettingsFragment())
        }

        val constraintHistorySettings =
            view?.findViewById<ConstraintLayout>(R.id.constraintHistorySettings)

        constraintHistorySettings?.setOnClickListener {
            (context as MainActivity).currentSettingsItem = 2
            openFragment(HistorySettingsFragment())
        }

        val patchNotesChevron = activity?.findViewById<ImageView>(R.id.patchNotesChevron)

        val patchNotesConstraint = view?.findViewById<ConstraintLayout>(R.id.constraintPatchNotes)

        patchNotesConstraint?.setOnClickListener {
            (context as MainActivity).currentSettingsItem = 3
            openFragment(PatchNotesFragment())
        }

        if (Version(requireContext()).loadVersion() != getString(R.string.version_number)) {
            patchNotesChevron?.setImageResource(R.drawable.redcircle)
            patchNotesChevron?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.redAccent
                )
            )
        }

        val faqConstraint = view?.findViewById<ConstraintLayout>(R.id.constraintFAQ)

        faqConstraint?.setOnClickListener {
            (context as MainActivity).currentSettingsItem = 5
            openFragment(FAQFragment())
        }

        val constraintAboutApp = view?.findViewById<ConstraintLayout>(R.id.constraintAboutApp)

        constraintAboutApp?.setOnClickListener {
            (context as MainActivity).currentSettingsItem = 6
            openFragment(AboutAppFragment())
        }

        val deleteAppDataConstraint =
            view?.findViewById<ConstraintLayout>(R.id.constraintDeleteAppData)

        deleteAppDataConstraint?.setOnClickListener {
            showDeleteDataAlert()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.supportFragmentManager?.popBackStack()
                }
            })
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

    private fun showDeleteDataAlert() {
        Vibrate().vibration(requireContext())

        val alert =
            MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
        alert.setTitle(getString(R.string.warning))
        alert.setMessage(getString(R.string.would_you_like_to_delete_all_app_data))
        alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
            Vibrate().vibration(requireContext())
            val dbHandler = DBHelper(requireContext(), null)
            requireContext().getSharedPreferences("file", 0).edit().clear().apply()
            requireContext().cacheDir.deleteRecursively()
            dbHandler.deleteAll()
            Toast.makeText(requireContext(), getString(R.string.app_data_cleared), Toast.LENGTH_LONG).show()
            Handler(Looper.getMainLooper()).postDelayed({
                val intent =
                    requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
                intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                activity?.finish()
            }, 1500)
        }
        alert.setNegativeButton(getString(R.string.no)) { _, _ ->
            Vibrate().vibration(requireContext())
        }
        alert.show()
    }

}