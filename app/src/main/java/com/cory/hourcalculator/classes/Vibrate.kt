package com.cory.hourcalculator.classes

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.cory.hourcalculator.sharedprefs.VibrationData

class Vibrate {

    fun vibration(context: Context) {
        val vibrationData = VibrationData(context)

        val vibrator: Vibrator
        if (vibrationData.loadVibrationState()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibrator = vibratorManager.defaultVibrator
               vibrator.vibrate(
                    VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(2)
            }

        }
    }

    fun vibrateOnError(context: Context) {

        val vibrationData = VibrationData(context)

        val vibrator: Vibrator
        if (vibrationData.loadVibrationState()) {
            val pattern = longArrayOf(0, 50, 15, 50)
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(pattern, -1)
        }
    }

    fun vibrationTimePickers(context: Context) {
        val vibrationData = VibrationData(context)

        val vibrator: Vibrator
        if (vibrationData.loadVibrationState()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                )
            } else {
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(1)
            }
        }
    }
}