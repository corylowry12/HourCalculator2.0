package com.cory.hourcalculator.intents

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager.widget.ViewPager
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.ChosenAppIconData
import com.cory.hourcalculator.classes.DialogData
import com.cory.hourcalculator.fragments.*
import com.google.android.material.bottomsheet.BottomSheetDialog

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    //private lateinit var viewPagerAdapter : ViewPagerAdapter

    val fragment1 = OnboardingFragment1()
    val fragment2 = OnBoardingCalculationType()
    val fragment3 = OnBoardingBackgroundThemeFragment()
    val fragment4 = OnBoardingAccentFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        theme!!.applyStyle(R.style.teal_accent, true)
        setContentView(R.layout.activity_onboarding)

        if (DialogData(this).loadOnboardingComplete()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_onboarding, fragment1).addToBackStack(null)
            transaction.commit()
        }
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        /*val relativeLayout = findViewById<RelativeLayout>(R.id.layout1)
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dotsIndicator)
        val animationDrawable = relativeLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2500)
        animationDrawable.setExitFadeDuration(1000)
        animationDrawable.start()

        viewPager = findViewById(R.id.viewPager)
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter
        dotsIndicator.setViewPager(viewPager)

        viewPager.addOnPageChangeListener(object: OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    relativeLayout.setBackgroundResource(R.drawable.gradient_animation_123)
                    val animationDrawable = relativeLayout.background as AnimationDrawable
                    animationDrawable.setEnterFadeDuration(2500)
                    animationDrawable.setExitFadeDuration(1000)
                    animationDrawable.start()
                }
                else {
                    relativeLayout.setBackgroundResource(R.drawable.gradient_animation_456)
                    val animationDrawable = relativeLayout.background as AnimationDrawable
                    animationDrawable.setEnterFadeDuration(2500)
                    animationDrawable.setExitFadeDuration(1000)
                    animationDrawable.start()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })*/

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.visibility = View.GONE
        val continueButton = findViewById<Button>(R.id.continueButton)
        continueButton.setOnClickListener {

            if (fragment1.isVisible) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container_onboarding, fragment2).addToBackStack(null)
                transaction.commit()
                backButton.visibility = View.VISIBLE
            }
            else if (fragment2.isVisible) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container_onboarding, fragment3).addToBackStack(null)
                transaction.commit()
                backButton.visibility = View.VISIBLE
            }
            else if (fragment3.isVisible) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container_onboarding, fragment4).addToBackStack(null)
                transaction.commit()
                backButton.visibility = View.VISIBLE
            }
            else if (fragment4.isVisible) {
                val dialog = BottomSheetDialog(this)
                val restartAppLayout = layoutInflater.inflate(R.layout.restart_app_onboarding_bottom_sheet, null)
                dialog.setContentView(restartAppLayout)
                dialog.setCancelable(false)

                restartAppLayout.findViewById<Button>(R.id.restartButton).setOnClickListener {
                    if (AccentColor(this).loadAccent() == 0) {
                        if (ChosenAppIconData(this).loadChosenAppIcon() == 0) {
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashOrange"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashRed"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashPink"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashScreenNoIcon"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.MaterialYou"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                        }
                    }
                    else if (AccentColor(this).loadAccent() == 1) {
                        if (ChosenAppIconData(this).loadChosenAppIcon() == 0) {
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashOrange"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashRed"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashPink"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashScreenNoIcon"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.MaterialYou"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                        }
                    }
                    else if (AccentColor(this).loadAccent() == 2) {
                        if (ChosenAppIconData(this).loadChosenAppIcon() == 0) {
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashOrange"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashRed"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashPink"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashScreenNoIcon"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.MaterialYou"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                        }
                    }
                    else if (AccentColor(this).loadAccent() == 3) {
                        if (ChosenAppIconData(this).loadChosenAppIcon() == 0) {
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashOrange"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashRed"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashPink"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashScreenNoIcon"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.MaterialYou"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                        }
                    }
                    else if (AccentColor(this).loadAccent() == 4) {
                        if (ChosenAppIconData(this).loadChosenAppIcon() == 0) {
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashOrange"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashRed"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashPink"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.SplashScreenNoIcon"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            this.packageManager?.setComponentEnabledSetting(
                                ComponentName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.cory.hourcalculator.MaterialYou"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP
                            )
                        }
                    }
                    restartApplication()
                    DialogData(this).setOnboardingComplete(true)
                }

                dialog.show()
            }
        }
        
        backButton.setOnClickListener {
            if (fragment2.isVisible) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container_onboarding, fragment1).addToBackStack(null)
                transaction.commit()
                backButton.visibility = View.GONE
            }
            else if (fragment3.isVisible) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container_onboarding, fragment2).addToBackStack(null)
                transaction.commit()
                backButton.visibility = View.VISIBLE
            }
            else if (fragment4.isVisible) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container_onboarding, fragment3).addToBackStack(null)
                transaction.commit()
                backButton.visibility = View.VISIBLE
            }
        }
    }

    fun currentVisible() {
        val backButton = findViewById<Button>(R.id.backButton)
        if (!fragment1.isVisible) {
            backButton.visibility = View.VISIBLE
        }
        else {
            backButton.visibility = View.GONE
        }
    }

    private fun restartApplication() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent =
                this.packageManager.getLaunchIntentForPackage(this.packageName)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this.finish()
        }, 1000)
    }
}