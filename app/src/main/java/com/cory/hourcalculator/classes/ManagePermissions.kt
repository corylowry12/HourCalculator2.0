package com.cory.hourcalculator.classes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.CornerFamily

class ManagePermissions(private val activity: Activity, private val list: List<String>, private val code:Int) {

    // Check permissions at runtime
    fun checkPermissions(context: Context) : Boolean {
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
    private fun deniedPermission(context : Context): String {
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

        val okCardView = askPermissionsLayout.findViewById<MaterialCardView>(R.id.okCardView)
        val cancelCardView = askPermissionsLayout.findViewById<MaterialCardView>(R.id.cancelCardView)

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

        val okConstraint = askPermissionsLayout.findViewById<ConstraintLayout>(R.id.okConstraint)
        val cancelConstraint = askPermissionsLayout.findViewById<ConstraintLayout>(R.id.cancelConstraint)
        okConstraint.setOnClickListener {
            Vibrate().vibration(context)
            bottomSheetDialog.dismiss()
            requestPermissions(context)
        }
        cancelConstraint.setOnClickListener {
            Vibrate().vibration(context)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()


        /*val builder = MaterialAlertDialogBuilder(context, R.style.AlertDialogStyle).create()
        val layout =
            LayoutInflater.from(context).inflate(R.layout.need_app_permissions_dialog_layout, null)
        builder.setView(layout)
        builder.setCancelable(false)
        val okButton = layout.findViewById<Button>(R.id.needPermissionsDialogOKButton)
        val cancelButton = layout.findViewById<Button>(R.id.cancelNeedPermissionsDialog)
        okButton.setOnClickListener {
            builder.dismiss()
            requestPermissions(context)
        }
        cancelButton.setOnClickListener {
            builder.dismiss()
            Toast.makeText(context, context.getString(R.string.permission_not_granted), Toast.LENGTH_SHORT)
                    .show()
        }
        builder.show()*/
        }

    // Request the permissions at run time
    private fun requestPermissions(context: Context) {
        val permission = deniedPermission(context)
        ActivityCompat.requestPermissions(activity, list.toTypedArray(), code)

    }
}