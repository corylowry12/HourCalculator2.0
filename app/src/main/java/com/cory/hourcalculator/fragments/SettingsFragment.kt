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
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import com.cory.hourcalculator.classes.LinkClass
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.database.DBHelper
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
        val appearanceHeading = activity?.findViewById<TextView>(R.id.themeHeading)
        val appearanceSubtitle = activity?.findViewById<TextView>(R.id.themeSubtitle)
        val appearanceImage = activity?.findViewById<ImageView>(R.id.appearanceImage)
        val appearanceCardView = activity?.findViewById<CardView>(R.id.themeCardView)

        appearanceHeading?.setOnClickListener {
            openFragment(AppearanceFragment())
        }

        appearanceSubtitle?.setOnClickListener {
            openFragment(AppearanceFragment())
        }

        appearanceImage?.setOnClickListener {
            openFragment(AppearanceFragment())
        }

        appearanceCardView?.setOnClickListener {
            openFragment(AppearanceFragment())
        }

        val appSettingsHeading = activity?.findViewById<TextView>(R.id.appSettings)
        val appSettingsSubtitle = activity?.findViewById<TextView>(R.id.appSettingsSubtitle)
        val appSettingsCardView = activity?.findViewById<CardView>(R.id.layoutCardView)
        val appSettingsImageView = activity?.findViewById<ImageView>(R.id.appSettingsImage)

        appSettingsHeading?.setOnClickListener {
            openFragment(AppSettingsFragment())
        }
        appSettingsSubtitle?.setOnClickListener {
            openFragment(AppSettingsFragment())
        }
        appSettingsCardView?.setOnClickListener {
            openFragment(AppSettingsFragment())
        }
        appSettingsImageView?.setOnClickListener {
            openFragment(AppSettingsFragment())
        }

        val automaticDeletionHeading = activity?.findViewById<TextView>(R.id.deletionHeading)
        val automaticDeletionSubtitle = activity?.findViewById<TextView>(R.id.deletionSubtitle)
        val automaticDeletionCardView = activity?.findViewById<CardView>(R.id.deletionCardView)
        val automaticDeletionImageView = activity?.findViewById<ImageView>(R.id.deletionImage)

        automaticDeletionHeading?.setOnClickListener {
            openFragment(AutomaticDeletionFragment())
        }
        automaticDeletionSubtitle?.setOnClickListener {
            openFragment(AutomaticDeletionFragment())
        }
        automaticDeletionCardView?.setOnClickListener {
            openFragment(AutomaticDeletionFragment())
        }
        automaticDeletionImageView?.setOnClickListener {
            openFragment(AutomaticDeletionFragment())
        }

        val patchNotesHeading = activity?.findViewById<TextView>(R.id.patchNotesHeading)
        val patchNotesSubtitle = activity?.findViewById<TextView>(R.id.patchNotesSubtitle)
        val patchNotesImage = activity?.findViewById<ImageView>(R.id.patchNotesImage)
        val patchNotesCardView = activity?.findViewById<CardView>(R.id.patchNotesCardView)

        patchNotesHeading?.setOnClickListener {
            openFragment(PatchNotesFragment())
        }
        patchNotesSubtitle?.setOnClickListener {
            openFragment(PatchNotesFragment())
        }
        patchNotesImage?.setOnClickListener {
            openFragment(PatchNotesFragment())
        }
        patchNotesCardView?.setOnClickListener {
            openFragment(PatchNotesFragment())
        }

        val reviewHeading = activity?.findViewById<TextView>(R.id.reviewHeading)
        val reviewSubtitle = activity?.findViewById<TextView>(R.id.reviewSubtitle)
        val reviewCardView = activity?.findViewById<CardView>(R.id.reviewCardView)
        val reviewImageView = activity?.findViewById<ImageView>(R.id.reviewImage)

        reviewHeading?.setOnClickListener {
            leaveAReview()
        }
        reviewSubtitle?.setOnClickListener {
            leaveAReview()
        }
        reviewCardView?.setOnClickListener {
            leaveAReview()
        }
        reviewImageView?.setOnClickListener {
            leaveAReview()
        }

        val reportBugHeading = activity?.findViewById<TextView>(R.id.bugHeading)
        val reportBugSubtitle = activity?.findViewById<TextView>(R.id.bugSubtitle)
        val reportBugCardView = activity?.findViewById<CardView>(R.id.bugCardView)
        val reportBugImageView = activity?.findViewById<ImageView>(R.id.reportABug)

        reportBugHeading?.setOnClickListener {
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0/issues")
            openFragment(WebviewFragment())
        }
        reportBugSubtitle?.setOnClickListener {
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0/issues")
            openFragment(WebviewFragment())
        }
        reportBugCardView?.setOnClickListener {
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0/issues")
            openFragment(WebviewFragment())
        }
        reportBugImageView?.setOnClickListener {
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0/issues")
            openFragment(WebviewFragment())
        }

        val githubHeading = activity?.findViewById<TextView>(R.id.textViewGithubHeading)
        val githubSubtitle = activity?.findViewById<TextView>(R.id.textViewGithubCaption)
        val githubImage = activity?.findViewById<ImageView>(R.id.githubImage)
        val githubCardView = activity?.findViewById<CardView>(R.id.cardViewGithub)

        githubHeading?.setOnClickListener {
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0")
            openFragment(WebviewFragment())
        }
        githubSubtitle?.setOnClickListener {
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0")
            openFragment(WebviewFragment())
        }
        githubImage?.setOnClickListener {
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0")
            openFragment(WebviewFragment())
        }
        githubCardView?.setOnClickListener {
            LinkClass(requireContext()).setLink("https://github.com/corylowry12/HourCalculator2.0")
            openFragment(WebviewFragment())
        }

        val deleteDataHeading = activity?.findViewById<TextView>(R.id.deleteDataHeading)
        val deleteDataSubtitle = activity?.findViewById<TextView>(R.id.deleteDataSubtitle)
        val deleteDataImage = activity?.findViewById<ImageView>(R.id.deleteDataImage)
        val deleteDataCardView = activity?.findViewById<CardView>(R.id.deleteDataCardView)

        deleteDataHeading?.setOnClickListener {
            showDeleteDataAlert()
        }
        deleteDataSubtitle?.setOnClickListener {
            showDeleteDataAlert()
        }
        deleteDataImage?.setOnClickListener {
            showDeleteDataAlert()
        }
        deleteDataCardView?.setOnClickListener {
            showDeleteDataAlert()
        }

        val faqHeading = view?.findViewById<TextView>(R.id.FAQDataHeading)
        val faqSubtitle = view?.findViewById<TextView>(R.id.FAQDataSubtitle)
        val faqImageView = view?.findViewById<ImageView>(R.id.FAQDataImage)
        val faqCardView = view?.findViewById<CardView>(R.id.FAQDataCardView)

        faqHeading?.setOnClickListener {
            openFragment(FAQFragment())
        }
        faqSubtitle?.setOnClickListener {
            openFragment(FAQFragment())
        }
        faqImageView?.setOnClickListener {
            openFragment(FAQFragment())
        }
        faqCardView?.setOnClickListener {
            openFragment(FAQFragment())
        }

        val versionHeading = view?.findViewById<TextView>(R.id.versionHeading)
        val versionSubtitle = view?.findViewById<TextView>(R.id.versionSubtitle)
        val versionImage = view?.findViewById<ImageView>(R.id.versionImage)
        val versionCardView = view?.findViewById<CardView>(R.id.versionCardView)

       versionHeading?.setOnClickListener {
           openFragment(AboutFragment())
       }

        versionSubtitle!!.setOnClickListener {
            openFragment(AboutFragment())
        }

        versionImage!!.setOnClickListener {
            openFragment(AboutFragment())
        }

        versionCardView!!.setOnClickListener {
            openFragment(AboutFragment())
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager?.popBackStack()
            }
        })
    }

    fun openFragment(fragment: Fragment) {
        Vibrate().vibration(requireContext())

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)
        transaction?.commit()
    }

    fun leaveAReview() {
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

    fun showDeleteDataAlert() {
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