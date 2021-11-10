package com.cory.hourcalculator.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import com.cory.hourcalculator.classes.LinkClass
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

        val appearanceHeading = activity?.findViewById<TextView>(R.id.themeHeading)
        val appearanceSubtitle = activity?.findViewById<TextView>(R.id.themeSubtitle)
        val appearanceImage = activity?.findViewById<ImageView>(R.id.appearanceImage)
        val appearanceCardView = activity?.findViewById<CardView>(R.id.themeCardView)

        appearanceHeading?.setOnClickListener {
            openAppearanceFragment()
        }

        appearanceSubtitle?.setOnClickListener {
            openAppearanceFragment()
        }

        appearanceImage?.setOnClickListener {
            openAppearanceFragment()
        }

        appearanceCardView?.setOnClickListener {
            openAppearanceFragment()
        }

        val layoutSettingsHeading = activity?.findViewById<TextView>(R.id.layout)
        val layoutSettingsSubtitle = activity?.findViewById<TextView>(R.id.layoutSubtitle)
        val layoutSettingsCardView = activity?.findViewById<CardView>(R.id.layoutCardView)
        val layoutSettingsImageView = activity?.findViewById<ImageView>(R.id.layoutImage)

        layoutSettingsHeading?.setOnClickListener {
            openLayoutSettingsFragment()
        }
        layoutSettingsSubtitle?.setOnClickListener {
            openLayoutSettingsFragment()
        }
        layoutSettingsCardView?.setOnClickListener {
            openLayoutSettingsFragment()
        }
        layoutSettingsImageView?.setOnClickListener {

        }

        val automaticDeletionHeading = activity?.findViewById<TextView>(R.id.deletionHeading)

        automaticDeletionHeading?.setOnClickListener {
            openAutomaticDeletionFragment()
        }

        val patchNotesHeading = activity?.findViewById<TextView>(R.id.patchNotesHeading)
        val patchNotesSubtitle = activity?.findViewById<TextView>(R.id.patchNotesSubtitle)
        val patchNotesImage = activity?.findViewById<ImageView>(R.id.patchNotesImage)
        val patchNotesCardView = activity?.findViewById<CardView>(R.id.patchNotesCardView)

        patchNotesHeading?.setOnClickListener {
            openPatchNotesFragment()
        }
        patchNotesSubtitle?.setOnClickListener {
            openPatchNotesFragment()
        }
        patchNotesImage?.setOnClickListener {
            openPatchNotesFragment()
        }
        patchNotesCardView?.setOnClickListener {
            openPatchNotesFragment()
        }

        LinkClass(requireContext()).setLink("https://github.com/")

        val githubHeading = activity?.findViewById<TextView>(R.id.textViewGithubHeading)
        val githubSubtitle = activity?.findViewById<TextView>(R.id.textViewGithubCaption)
        val githubImage = activity?.findViewById<ImageView>(R.id.githubImage)
        val githubCardView = activity?.findViewById<CardView>(R.id.cardViewGithub)

        githubHeading?.setOnClickListener {
            openWebviewFragment()
        }
        githubSubtitle?.setOnClickListener {
            openWebviewFragment()
        }
        githubImage?.setOnClickListener {
            openWebviewFragment()
        }
        githubCardView?.setOnClickListener {
            openWebviewFragment()
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
    }

    fun openAppearanceFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()

        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction?.replace(R.id.fragment_container, AppearanceFragment())?.addToBackStack(null)
        transaction?.commit()
    }

    fun openLayoutSettingsFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()

        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction?.replace(R.id.fragment_container, LayoutSettingsFragment())?.addToBackStack(null)
        transaction?.commit()
    }

    fun openAutomaticDeletionFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()

        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction?.replace(R.id.fragment_container, AutomaticDeletionFragment())?.addToBackStack(null)
        transaction?.commit()
    }

    fun openPatchNotesFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()

        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction?.replace(R.id.fragment_container, PatchNotesFragment())?.addToBackStack(null)
        transaction?.commit()
    }

    fun openWebviewFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()

        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction?.replace(R.id.fragment_container, WebviewFragment())?.addToBackStack(null)
        transaction?.commit()
    }

    fun showDeleteDataAlert() {
        val alert = MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme(requireContext()))
        alert.setTitle("Warning")
        alert.setMessage("Would you like to delete all app data?")
        alert.setPositiveButton("Yes") { _, _ ->
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
        alert.setNegativeButton("No", null)
        alert.show()
    }
}