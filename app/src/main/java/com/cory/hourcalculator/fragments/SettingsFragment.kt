package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewManagerFactory

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                activity?.setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> activity?.setTheme(R.style.Theme_MyApplication)
                    Configuration.UI_MODE_NIGHT_YES -> activity?.setTheme(R.style.Theme_AMOLED)
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> activity?.setTheme(R.style.Theme_AMOLED)
                }
            }
        }

        val accentColor = AccentColor(requireContext())
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
                activity?.theme?.applyStyle(R.style.system_accent, true)
            }
        }

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.setBackgroundDrawable(null)

        val inputManager: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)

        main()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        main()

    }

    override fun onResume() {
        super.onResume()
        main()
    }

    private fun main() {

        val appearanceConstraintLayout = view?.findViewById<ConstraintLayout>(R.id.constraintAppearance)

        appearanceConstraintLayout?.setOnClickListener {
            openFragment(AppearanceFragment())
        }

        val appSettingsConstraint = view?.findViewById<ConstraintLayout>(R.id.constraintAppSettings)

        appSettingsConstraint?.setOnClickListener {
            openFragment(AppSettingsFragment())
        }

        val automaticDeletionConstraint = view?.findViewById<ConstraintLayout>(R.id.constraintAutomaticDeletion)

        automaticDeletionConstraint?.setOnClickListener {
            openFragment(AutomaticDeletionFragment())
        }

        val patchNotesChevron = activity?.findViewById<ImageView>(R.id.patchNotesChevron)

        val patchNotesConstraint = view?.findViewById<ConstraintLayout>(R.id.constraintPatchNotes)

        patchNotesConstraint?.setOnClickListener {
            openFragment(PatchNotesFragment())
        }

        if (Version(requireContext()).loadVersion() != "9.0.0") {
            patchNotesChevron?.setImageResource(R.drawable.redcircle)
            patchNotesChevron?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.redAccent
                )
            )
        }

        val leaveAReviewConstraint = view?.findViewById<ConstraintLayout>(R.id.constraintLeaveAReview)

        leaveAReviewConstraint?.setOnClickListener {
            leaveAReview()
        }

        val reportABugConstraint = view?.findViewById<ConstraintLayout>(R.id.constraintReportABug)

        reportABugConstraint?.setOnClickListener {
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0/issues")
            openFragment(WebviewFragment())
        }

        val viewGithubConstraint = view?.findViewById<ConstraintLayout>(R.id.constraintViewGithub)

        viewGithubConstraint?.setOnClickListener {
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0")
            openFragment(WebviewFragment())
        }

        val deleteAppDataConstraint = view?.findViewById<ConstraintLayout>(R.id.constraintDeleteAppData)

        deleteAppDataConstraint?.setOnClickListener {
            showDeleteDataAlert()
        }

        val faqConstraint = view?.findViewById<ConstraintLayout>(R.id.constraintFAQ)

        faqConstraint?.setOnClickListener {
            openFragment(FAQFragment())
        }

        val versionInfoConstraint = view?.findViewById<ConstraintLayout>(R.id.constraintVersionInfo)

        versionInfoConstraint?.setOnClickListener {
            openFragment(AboutFragment())
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager?.popBackStack()
            }
        })
    }

    private fun openFragment(fragment: Fragment) {
        Vibrate().vibration(requireContext())

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
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
                    Toast.makeText(requireContext(), "There was an error, please try again", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showDeleteDataAlert() {
        Vibrate().vibration(requireContext())

        val alert = MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
        alert.setTitle("Warning")
        alert.setMessage("Would you like to delete all app data?")
        alert.setPositiveButton("Yes") { _, _ ->
            Vibrate().vibration(requireContext())
            val dbHandler = DBHelper(requireContext(), null)
            requireContext().getSharedPreferences("file", 0).edit().clear().apply()
            requireContext().cacheDir.deleteRecursively()
            dbHandler.deleteAll()
            Toast.makeText(requireContext(), "App Data Cleared", Toast.LENGTH_LONG).show()
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
                intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                activity?.finish()
            }, 1500)
        }
        alert.setNegativeButton("No") { _, _ ->
            Vibrate().vibration(requireContext())
        }
        alert.show()
    }
}