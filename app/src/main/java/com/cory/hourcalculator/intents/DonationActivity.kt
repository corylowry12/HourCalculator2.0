package com.cory.hourcalculator.intents

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.common.collect.ImmutableList


class DonationActivity : AppCompatActivity() {

    var productDetailsList: List<ProductDetails>? = null

    private lateinit var billingClient: BillingClient

    var selected_item = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val darkThemeData = DarkThemeData(this)
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                //activity?.setTheme(R.style.Theme_DarkTheme)
            }

            darkThemeData.loadDarkModeState() == 0 -> {
                setTheme(R.style.Theme_MyApplication)
            }

            darkThemeData.loadDarkModeState() == 2 -> {
                setTheme(R.style.Theme_AMOLED)
            }

            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        setTheme(R.style.Theme_MyApplication)
                    }

                    Configuration.UI_MODE_NIGHT_YES -> {
                        setTheme(R.style.Theme_AMOLED)
                    }

                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        setContentView(R.layout.activity_donation)

        val donateButton = findViewById<Button>(R.id.donateButton)

        val candyCardView =
            findViewById<MaterialCardView>(R.id.candyCardView)
        val coffeeCardView =
            findViewById<MaterialCardView>(R.id.coffeeCardView)
        val pizzaCardView =
            findViewById<MaterialCardView>(R.id.pizzaCardView)
        val fiveStarCardView =
            findViewById<MaterialCardView>(R.id.fiveStarCardView)

        candyCardView.setCardBackgroundColor(
            ColorStateList.valueOf(
                Color.parseColor(CustomColorGenerator(this).generateCardColor())
            )
        )
        coffeeCardView.setCardBackgroundColor(
            ColorStateList.valueOf(
                Color.parseColor(CustomColorGenerator(this).generateCardColor())
            )
        )
        pizzaCardView.setCardBackgroundColor(
            ColorStateList.valueOf(
                Color.parseColor(
                    CustomColorGenerator(this).generateCardColor()
                )
            )
        )
        fiveStarCardView.setCardBackgroundColor(
            ColorStateList.valueOf(
                Color.parseColor(
                    CustomColorGenerator(this).generateCardColor()
                )
            )
        )
        donateButton.setBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    this
                ).generateCustomColorPrimary()
            )
        )
        candyCardView.shapeAppearanceModel =
            candyCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
        coffeeCardView.shapeAppearanceModel =
            coffeeCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
        pizzaCardView.shapeAppearanceModel = pizzaCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        fiveStarCardView.shapeAppearanceModel = fiveStarCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val candy =
            findViewById<RadioButton>(R.id.candy)
        val coffee =
            findViewById<RadioButton>(R.id.coffee)
        val pizza =
            findViewById<RadioButton>(R.id.pizza)
        val fiveStarMeal =
            findViewById<RadioButton>(R.id.fiveStar)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )

        val colors = intArrayOf(
            Color.parseColor("#000000"),
            Color.parseColor(CustomColorGenerator(this).generateCustomColorPrimary())
        )

        candy.buttonTintList = ColorStateList(states, colors)
        coffee.buttonTintList = ColorStateList(states, colors)
        pizza.buttonTintList = ColorStateList(states, colors)
        fiveStarMeal.buttonTintList = ColorStateList(states, colors)

        candyCardView.setOnClickListener {
            candy.isChecked = true
            coffee.isChecked = false
            pizza.isChecked = false
            fiveStarMeal.isChecked = false
            selected_item = "one_dollar_donation"
        }
        coffeeCardView.setOnClickListener {
            candy.isChecked = false
            coffee.isChecked = true
            pizza.isChecked = false
            fiveStarMeal.isChecked = false
            selected_item = "five_dollar_donation"
        }
        pizzaCardView.setOnClickListener {
            candy.isChecked = false
            coffee.isChecked = false
            pizza.isChecked = true
            fiveStarMeal.isChecked = false
            selected_item = "20_dollar_donation"
        }
        fiveStarCardView.setOnClickListener {
            candy.isChecked = false
            coffee.isChecked = false
            pizza.isChecked = false
            fiveStarMeal.isChecked = true
            selected_item = "one_hundred_dollar_donation"
        }

        donateButton.setOnClickListener {
        }
    }
}