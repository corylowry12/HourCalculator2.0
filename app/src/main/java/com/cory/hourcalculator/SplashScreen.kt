package com.cory.hourcalculator

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val darkThemeData = DarkThemeData(this)
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                setTheme(R.style.Theme_MyApplication)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> setTheme(R.style.Theme_MyApplication)
                    Configuration.UI_MODE_NIGHT_YES -> setTheme(R.style.Theme_AMOLED)
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> setTheme(R.style.Theme_AMOLED)
                }
            }
        }
        val accentColor = AccentColor(this)
        when {
            accentColor.loadAccent() == 0 -> {
                theme?.applyStyle(R.style.teal_accent, true)
            }
            accentColor.loadAccent() == 1 -> {
                theme?.applyStyle(R.style.pink_accent, true)
            }
            accentColor.loadAccent() == 2 -> {
                theme?.applyStyle(R.style.orange_accent, true)
            }
            accentColor.loadAccent() == 3 -> {
               theme?.applyStyle(R.style.red_accent, true)
            }
            accentColor.loadAccent() == 4 -> {
                theme?.applyStyle(R.style.system_accent, true)
            }
        }
        window.setBackgroundDrawable(null)
        actionBar?.hide()
        setContentView(R.layout.activity_splash_screen)

        if (Build.VERSION.RELEASE.contains(".")) {
            val version = Build.VERSION.RELEASE.split(".")
            if (version.toString().toInt() < 12) {
                load()

                val imageView = findViewById<ImageView>(R.id.SplashScreenImage)
                when {
                    accentColor.loadAccent() == 0 || accentColor.loadAccent() == 4 -> {
                        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.hourcalculatorlogo))
                    }
                    accentColor.loadAccent() == 1 -> {
                        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pinklogo))
                    }
                    accentColor.loadAccent() == 2 -> {
                        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.orange_logo))
                    }
                    accentColor.loadAccent() == 3 -> {
                        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_logo))
                    }
                }
            }
            else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        else if (Build.VERSION.RELEASE.toInt() < 12) {
            load()

            val accentColor = AccentColor(this)

            val imageView = findViewById<ImageView>(R.id.SplashScreenImage)
            when {
                accentColor.loadAccent() == 0 || accentColor.loadAccent() == 4 -> {
                    imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.hourcalculatorlogo))
                }
                accentColor.loadAccent() == 1 -> {
                    imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pinklogo))
                }
                accentColor.loadAccent() == 2 -> {
                    imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.orange_logo))
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

    fun load() {
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
        if (Build.VERSION.RELEASE.contains(".")) {
            val version = Build.VERSION.RELEASE.split(".")
            if (version.toString().toInt() < 12) {
                load()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        else if (Build.VERSION.RELEASE.toInt() < 12) {
            load()
        }
        else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}