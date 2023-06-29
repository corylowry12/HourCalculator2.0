package com.cory.hourcalculator.classes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily

class ManagePermissions(private val activity: Activity, private val list: List<String>, private val code:Int) {

    // Check permissions at runtime
    fun checkPermissions() : Boolean {
        return isPermissionsGranted() == PackageManager.PERMISSION_GRANTED
    }

    // Check permissions status
    private fun isPermissionsGranted(): Int {
        // PERMISSION_GRANTED : Constant Value: 0
        // PERMISSION_DENIED : Constant Value: -1
        var counter = 0
        for (permission in list) {
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return counter
    }


    // Find the first denied permission
    private fun deniedPermission(): String {
        for (permission in list) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) return permission
        }
        return ""
    }

    // Show alert dialog to request permissions
    @SuppressLint("InflateParams")
    fun showAlert(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val askPermissionsLayout = LayoutInflater.from(context).inflate(R.layout.ask_permissions_bottom_sheet, null)
        bottomSheetDialog.setContentView(askPermissionsLayout)
        bottomSheetDialog.setCancelable(false)

        if (context.resources.getBoolean(R.bool.isTablet)) {
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.isDraggable = false
        }

        val okCardView = askPermissionsLayout.findViewById<MaterialCardView>(R.id.okCardView)
        val cancelCardView = askPermissionsLayout.findViewById<MaterialCardView>(R.id.cancelCardView)

        okCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))
        cancelCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))

        okCardView.shapeAppearanceModel = okCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        cancelCardView.shapeAppearanceModel = cancelCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        okCardView.setOnClickListener {
            Vibrate().vibration(context)
            bottomSheetDialog.dismiss()
            requestPermissions()
        }
        cancelCardView.setOnClickListener {
            Vibrate().vibration(context)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
        }

    // Request the permissions at run time
    private fun requestPermissions() {
        //val permission = deniedPermission()
        ActivityCompat.requestPermissions(activity, list.toTypedArray(), code)

    }
}