package com.cory.hourcalculator.adapters

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.ChosenAppIconData
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.Vibrate
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily


class AppIconAdapter(val context: Context,
                     private val dataList:  ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val iconDisableArray = arrayListOf<String>()
    private var iconEnableID = ""

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            val iconImageView = itemView.findViewById<ImageView>(R.id.iconImageView)
            val iconNameTextView = itemView.findViewById<TextView>(R.id.icon_name)
            val iconCardView = itemView.findViewById<MaterialCardView>(R.id.cardViewAppIcon)

            iconCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))

            if (position == 0) {
                iconCardView.shapeAppearanceModel = iconCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
            }
            else if (position > 0 && position < dataList.count() - 1) {

                    if (Build.VERSION.SDK_INT < 31) {
                        if (position == dataList.count() - 2) {
                            iconCardView.shapeAppearanceModel = iconCardView.shapeAppearanceModel
                                .toBuilder()
                                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                .setBottomRightCornerSize(28f)
                                .setBottomLeftCornerSize(28f)
                                .build()
                        }
                        else {
                            iconCardView.shapeAppearanceModel = iconCardView.shapeAppearanceModel
                                .toBuilder()
                                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                .setBottomRightCornerSize(0f)
                                .setBottomLeftCornerSize(0f)
                                .build()
                        }
                    } else {
                        iconCardView.shapeAppearanceModel = iconCardView.shapeAppearanceModel
                            .toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                            .setBottomRightCornerSize(0f)
                            .setBottomLeftCornerSize(0f)
                            .build()
                    }
            }
            else {
                iconCardView.shapeAppearanceModel = iconCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(28f)
                    .setBottomLeftCornerSize(28f)
                    .build()
            }
            if (Build.VERSION.SDK_INT < 31) {
                if (position == dataList.count() - 1) {
                    iconCardView.visibility = View.GONE
                }
                else {
                    val dataItem = dataList[position]
                    iconNameTextView.text = dataItem["name"]
                    iconImageView.setImageResource(dataItem["icon"]!!.toInt())
                }
            }
            else {
                val dataItem = dataList[position]
                iconNameTextView.text = dataItem["name"]
                iconImageView.setImageResource(dataItem["icon"]!!.toInt())
            }

            if (ChosenAppIconData(context).loadChosenAppIcon().toString().lowercase() == iconNameTextView.text.toString().lowercase()) {
                iconCardView.strokeWidth = 7
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.app_icon_list_row, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.findViewById<MaterialCardView>(R.id.cardViewAppIcon).setOnClickListener {
            Vibrate().vibration(context)
            if (holder.itemView.findViewById<TextView>(R.id.icon_name).text.toString().lowercase() == ChosenAppIconData(context).loadChosenAppIcon()) {
                Toast.makeText(context, "This icon is already enabled", Toast.LENGTH_SHORT).show()
            }
            else {
                val dialog = BottomSheetDialog(context)
                val changeAppIconBottomSheet = LayoutInflater.from(context)
                    .inflate(R.layout.restart_app_icon_warning_bottom_sheet, null)
                dialog.setContentView(changeAppIconBottomSheet)
                dialog.setCancelable(true)

                val infoCardView = changeAppIconBottomSheet.findViewById<MaterialCardView>(R.id.bodyCardView)
                val yesButton = changeAppIconBottomSheet.findViewById<Button>(R.id.yesAppIconButton)
                val noButton =
                    changeAppIconBottomSheet.findViewById<Button>(R.id.cancelAppIconButton)

                infoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))
                yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCustomColorPrimary()))
                noButton.setTextColor(Color.parseColor(CustomColorGenerator(context).generateCustomColorPrimary()))

                yesButton.setOnClickListener {
                    Vibrate().vibration(context)
                    when (position) {
                        0 -> {
                            iconDisableArray.clear()
                            iconEnableID = "com.cory.hourcalculator.SplashScreenNoIcon"
                            iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                            iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                            iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                            iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                            iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                            changeIcons()
                            ChosenAppIconData(context).setChosenAppIcon("teal")
                            restartApplication()
                        }
                        1 -> {
                            iconDisableArray.clear()
                            iconEnableID = "com.cory.hourcalculator.SplashPink"
                            iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                            iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                            iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                            iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                            iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                            changeIcons()
                            ChosenAppIconData(context).setChosenAppIcon("pink")
                            restartApplication()
                        }
                        2 -> {
                            iconDisableArray.clear()
                            iconEnableID = "com.cory.hourcalculator.SplashOrange"
                            iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                            iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                            iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                            iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                            iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                            iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                            changeIcons()
                            ChosenAppIconData(context).setChosenAppIcon("orange")
                            restartApplication()
                        }
                        3 -> {
                            iconDisableArray.clear()
                            iconEnableID = "com.cory.hourcalculator.SplashRed"
                            iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                            iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                            iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                            iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                            iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                            changeIcons()
                            ChosenAppIconData(context).setChosenAppIcon("red")
                            restartApplication()
                        }
                        4 -> {
                            iconDisableArray.clear()
                            iconEnableID = "com.cory.hourcalculator.SplashBlue"
                            iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                            iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                            iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                            iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                            iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                            changeIcons()
                            ChosenAppIconData(context).setChosenAppIcon("blue")
                            restartApplication()
                        }
                        5 -> {
                            iconDisableArray.clear()
                            iconEnableID = "com.cory.hourcalculator.SplashOG"
                            iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                            iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                            iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                            iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                            iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                            iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                            changeIcons()
                            ChosenAppIconData(context).setChosenAppIcon("og")
                            restartApplication()
                        }
                        6 -> {
                            iconDisableArray.clear()
                            iconEnableID = "com.cory.hourcalculator.SplashSnowFalling"
                            iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                            iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                            iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                            iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                            iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                            changeIcons()
                            ChosenAppIconData(context).setChosenAppIcon("snow falling")
                            restartApplication()
                        }
                        dataList.count() - 1 -> {
                            iconDisableArray.clear()
                            iconEnableID = "com.cory.hourcalculator.MaterialYou"
                            iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                            iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                            iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                            iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                            iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                            iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                            changeIcons()
                            ChosenAppIconData(context).setChosenAppIcon("material you")
                            restartApplication()
                        }
                    }
                    dialog.dismiss()
                }
                noButton.setOnClickListener {
                    Vibrate().vibration(context)
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        (holder as AppIconAdapter.ViewHolder).bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private fun changeIcons() {
        for (i in 0 until iconDisableArray.count()) {
            context.packageManager?.setComponentEnabledSetting(
                ComponentName(
                    BuildConfig.APPLICATION_ID,
                    iconDisableArray.elementAt(i)
                ),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
        context.packageManager?.setComponentEnabledSetting(
            ComponentName(
                BuildConfig.APPLICATION_ID,
                iconEnableID
            ),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun restartApplication() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent =
                context.packageManager.getLaunchIntentForPackage(context.packageName)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            (context as Activity).finish()
        }, 1000)
    }
}