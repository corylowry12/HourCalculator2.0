package com.cory.hourcalculator.classes

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.cory.hourcalculator.sharedprefs.VibrationData

class Vibrate {

    fun vibration(context: Context) {
        val vibrationData = VibrationData(context)

        //val vibrator: Vibrator
        if (vibrationData.loadVibrationOnClickState()) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibrator = vibratorManager.defaultVibrator
               vibrator.vibrate(
                    VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(2)
            }*/
            val vibratorManager = context
                .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator

            if (vibrator.hasVibrator()) {
                context.getSystemService(Vibrator::class.java)
                    .vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            }
        }
    }

    fun vibrateOnLongClick(context: Context) {
        val vibrationData = VibrationData(context)

        //val vibrator: Vibrator
        if (vibrationData.loadVibrationOnLongClickState()) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(
                    VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(2)
            }*/
            val vibratorManager = context
                .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator

            if (vibrator.hasVibrator()) {
                context.getSystemService(Vibrator::class.java)
                    .vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
            }
        }
    }

    fun vibrateOnError(context: Context) {

        val vibrationData = VibrationData(context)

        val vibrator: Vibrator
        if (vibrationData.loadVibrationOnErrorState()) {
            val vibratorManager = context
                .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibratorCheck = vibratorManager.defaultVibrator

            if (vibratorCheck.hasVibrator()) {
                val pattern = longArrayOf(0, 50, 15, 50)
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(pattern, -1)
            }
        }
    }

    fun vibrationTimePickers(context: Context) {
        val vibrationData = VibrationData(context)

        //val vibrator: Vibrator
        if (vibrationData.loadVibrationOnTimePickerChangeState()) {
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                //val vibratorManager =
                    //context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                //vibrator = vibratorManager.defaultVibrator
                //vibrator.vibrate(
                    //VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
               // )
            val vibratorManager = context
                .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator

            if (vibrator.hasVibrator()) {
                context.getSystemService(Vibrator::class.java)
                    .vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
            }
            //} else {
               // vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                //vibrator.vibrate(1)
           // }
        }
    }
}