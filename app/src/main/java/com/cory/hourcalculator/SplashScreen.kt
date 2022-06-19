package com.cory.hourcalculator

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val darkThemeData = DarkThemeData(this)
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                setTheme(R.style.SplashDarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                setTheme(R.style.SplashLightTheme)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                setTheme(R.style.SplashBlackTheme)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> setTheme(R.style.SplashLightTheme)
                    Configuration.UI_MODE_NIGHT_YES -> setTheme(AccentColor(this).followSystemThemeSplashScreen(this))
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> setTheme(R.style.SplashBlackTheme)
                }
            }
        }
        window.setBackgroundDrawable(null)
        actionBar?.hide()
        setContentView(R.layout.activity_splash_screen)

        val accentColor = AccentColor(this)

        if (Build.VERSION.SDK_INT < 31) {
            load()

            val imageView = findViewById<ImageView>(R.id.SplashScreenImage)
            when {
                accentColor.loadAccent() == 0 || accentColor.loadAccent() == 4 -> {
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.hourcalculatorlogo
                        )
                    )
                }
                accentColor.loadAccent() == 1 -> {
                    imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pinklogo))
                }
                accentColor.loadAccent() == 2 -> {
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.orange_logo
                        )
                    )
                }
                accentColor.loadAccent() == 3 -> {
                    imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_logo))
                }
            }
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun load() {
        val cardView: CardView = findViewById(R.id.cardView)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        cardView.startAnimation(slideAnimation)
        val textView: TextView = findViewById(R.id.hour_calculator)
        textView.startAnimation(slideAnimation)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, 2000)
    }

    override fun onResume() {
        super.onResume()

       if (Build.VERSION.SDK_INT < 31) {
            load()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}