package com.cory.hourcalculator.classes

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import com.cory.hourcalculator.sharedprefs.VibrationData

class Vibrate {

    fun vibration(context: Context) {
        try {
            val vibrationData = VibrationData(context)

            if (vibrationData.loadVibrationOnClickState()) {
                val vibratorManager = context
                    .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator

                if (vibrator.hasVibrator()) {
                    context.getSystemService(Vibrator::class.java)
                        .vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
                }
            } else {
                val vib = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (vib.hasVibrator()) {
                    vib.vibrate(2)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun vibrateOnLongClick(context: Context) {
        try {
            val vibrationData = VibrationData(context)

            if (vibrationData.loadVibrationOnLongClickState()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorManager = context
                        .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    val vibrator = vibratorManager.defaultVibrator

                    if (vibrator.hasVibrator()) {
                        context.getSystemService(Vibrator::class.java)
                            .vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
                    }
                } else {
                    val vib = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if (vib.hasVibrator()) {
                        vib.vibrate(2)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun vibrateOnError(context: Context) {
        try {
            val vibrationData = VibrationData(context)

            val vib = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrationData.loadVibrationOnErrorState()) {

                if (vib.hasVibrator()) {
                    val pattern = longArrayOf(0, 50, 15, 50)
                    vib.vibrate(pattern, -1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun vibrationTimePickers(context: Context) {
        try {
            val vibrationData = VibrationData(context)

            if (vibrationData.loadVibrationOnTimePickerChangeState()) {

                if (Build.VERSION.SDK_INT >= 31) {

                    val vibratorManager = context
                        .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    val vibrator = vibratorManager.defaultVibrator

                    if (vibrator.hasVibrator()) {
                        context.getSystemService(Vibrator::class.java)
                            .vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
                    }
                } else {
                    val vib = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if (vib.hasVibrator()) {
                        vib.vibrate(3)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}