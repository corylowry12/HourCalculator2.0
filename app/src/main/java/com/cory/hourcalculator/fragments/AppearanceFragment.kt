package com.cory.hourcalculator.fragments

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import com.google.android.material.appbar.MaterialToolbar

class AppearanceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        if (darkThemeData.loadDarkModeState()) {
            activity?.setTheme(R.style.AMOLED)
        } else {
            activity?.setTheme(R.style.Theme_MyApplication)
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
        return inflater.inflate(R.layout.fragment_appearance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarAppearance)

        topAppBar?.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        val darkThemeData = DarkThemeData(requireContext())

        val lightThemeButton = activity?.findViewById<RadioButton>(R.id.lightTheme)
        val darkThemeButton = activity?.findViewById<RadioButton>(R.id.darkTheme)

        if (darkThemeData.loadDarkModeState()) {
            darkThemeButton?.isChecked = true
        } else if (!darkThemeData.loadDarkModeState()) {
            lightThemeButton?.isChecked = true
        }

        lightThemeButton?.setOnClickListener {
            //vibration(vibrationData)
            if (!darkThemeData.loadDarkModeState()) {
                Toast.makeText(requireContext(), "Light theme is already enabled", Toast.LENGTH_SHORT).show()
            } else {
                darkThemeData.setDarkModeState(false)
                restartThemeChange()
            }
        }
        darkThemeButton?.setOnClickListener {
            //vibration(vibrationData)
            if (darkThemeData.loadDarkModeState()) {
                Toast.makeText(requireContext(), "Dark mode is already enabled", Toast.LENGTH_SHORT).show()
            } else {
                darkThemeData.setDarkModeState(true)
                restartThemeChange()
            }
        }

        val accentColor = AccentColor(requireContext())

        val tealAccentButton = activity?.findViewById<RadioButton>(R.id.Teal)
        val pinkAccentButton = activity?.findViewById<RadioButton>(R.id.Pink)
        val orangeAccentButton = activity?.findViewById<RadioButton>(R.id.Orange)
        val redAccentButton = activity?.findViewById<RadioButton>(R.id.Red)
        val systemAccentButton = activity?.findViewById<RadioButton>(R.id.systemAccent)

        when {
            accentColor.loadAccent() == 0 -> {
                tealAccentButton?.isChecked = true
            }
            accentColor.loadAccent() == 1 -> {
                pinkAccentButton?.isChecked = true
            }
            accentColor.loadAccent() == 2 -> {
                orangeAccentButton?.isChecked = true
            }
            accentColor.loadAccent() == 3 -> {
                redAccentButton?.isChecked = true
            }
            accentColor.loadAccent() == 4 -> {
                systemAccentButton?.isChecked = true
            }
        }

        tealAccentButton?.setOnClickListener {
            //vibration(vibrationData)
            if (accentColor.loadAccent() == 0) {
                Toast.makeText(requireContext(), "Teal already chosen", Toast.LENGTH_SHORT).show()
            } else {
                    accentColor.setAccentState(0)
                    restartApplication()

            }
        }
        pinkAccentButton?.setOnClickListener {
           // vibration(vibrationData)
            if (accentColor.loadAccent() == 1) {
                Toast.makeText(requireContext(), "Pink already chosen", Toast.LENGTH_SHORT).show()
            } else {
                    accentColor.setAccentState(1)
                    restartApplication()
            }
        }
        orangeAccentButton?.setOnClickListener {
            //vibration(vibrationData)
            if (accentColor.loadAccent() == 2) {
                Toast.makeText(requireContext(), "orange already chosen", Toast.LENGTH_SHORT).show()
            } else {
                    accentColor.setAccentState(2)
                    restartApplication()

            }
        }
        redAccentButton?.setOnClickListener {
            //vibration(vibrationData)
            if (accentColor.loadAccent() == 3) {
                Toast.makeText(requireContext(), "Red is already chosen", Toast.LENGTH_SHORT).show()
            } else {
                    accentColor.setAccentState(3)
                    restartApplication()

            }
        }
        systemAccentButton?.setOnClickListener {
            //vibration(vibrationData)
            if (accentColor.loadAccent() == 4) {
                Toast.makeText(requireContext(), "Already chosen", Toast.LENGTH_SHORT).show()
            } else {
                    accentColor.setAccentState(4)
                    restartApplication()

            }
        }

       activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
           override fun handleOnBackPressed() {
               Toast.makeText(requireContext(), "clicked", Toast.LENGTH_LONG).show()
               activity?.supportFragmentManager?.popBackStack()
           }
       })
    }

    private fun restartThemeChange() {

        activity?.supportFragmentManager?.beginTransaction()?.detach(this)?.commitNow();
        activity?.supportFragmentManager?.beginTransaction()?.attach(this)?.commitNow();
    }

    private fun restartApplication() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }, 1000)
    }
}