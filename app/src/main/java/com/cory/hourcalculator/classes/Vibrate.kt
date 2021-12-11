package com.cory.hourcalculator.classes

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

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
                    VibrationEffect.createOneShot(
                        1,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        1,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }

        }
    }
}