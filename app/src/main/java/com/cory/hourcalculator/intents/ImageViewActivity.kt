package com.cory.hourcalculator.intents

import android.content.res.Configuration
import android.graphics.*
import android.media.ExifInterface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.Fade
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.palette.graphics.Palette
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.sharedprefs.AnimationData
import com.cory.hourcalculator.sharedprefs.ColoredNavBarData
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.cory.hourcalculator.sharedprefs.MatchImageViewContentsBackgroundData
import com.cory.hourcalculator.sharedprefs.MenuTintData
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.ortiz.touchview.TouchImageView


class ImageViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val darkThemeData = DarkThemeData(this)
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                //setTheme(R.style.Theme_DarkTheme)
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
        setContentView(R.layout.activity_image_view)

        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        val imageView = findViewById<TouchImageView>(R.id.touchImageView)
        val viewImageMaterialToolbar = findViewById<MaterialToolbar>(R.id.viewImageToolBar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val fade = Fade()
            fade.excludeTarget(R.id.viewImageToolBar, true)
            fade.excludeTarget(android.R.id.statusBarBackground, true)
            fade.excludeTarget(android.R.id.navigationBarBackground, true)
            window.enterTransition = fade
            window.exitTransition = fade
        }

        val id = intent.getStringExtra("id")
        //val name = intent.getStringExtra("name")

        imageView.setOnLongClickListener {
            Vibrate().vibration(this)
            if (viewImageMaterialToolbar.visibility == View.GONE) {
                viewImageMaterialToolbar.visibility = View.VISIBLE
            } else {
                viewImageMaterialToolbar.visibility = View.GONE
            }
            return@setOnLongClickListener true
        }

        val deleteImageDrawable = viewImageMaterialToolbar.menu.findItem(R.id.deleteImage).icon
        deleteImageDrawable!!.mutate()

        val navigationDrawable = viewImageMaterialToolbar?.navigationIcon
        navigationDrawable?.mutate()

        if (Build.VERSION.SDK_INT >= 29) {
            try {
                if (MenuTintData(this).loadMenuTint()) {

                    deleteImageDrawable.colorFilter = BlendModeColorFilter(
                        Color.parseColor(CustomColorGenerator(this).generateMenuTintColor()),
                        BlendMode.SRC_ATOP
                    )
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor(CustomColorGenerator(this).generateMenuTintColor()),
                        BlendMode.SRC_ATOP
                    )
                } else {
                    val typedValue = TypedValue()
                    theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                    val resourceId = typedValue.resourceId
                    deleteImageDrawable.colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(this, resourceId),
                        BlendMode.SRC_ATOP
                    )
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(this, resourceId),
                        BlendMode.SRC_ATOP
                    )
                }
            } catch (e: NoClassDefFoundError) {
                e.printStackTrace()
                val typedValue = TypedValue()
                theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                val id = typedValue.resourceId
                navigationDrawable?.setColorFilter(ContextCompat.getColor(this, id), PorterDuff.Mode.SRC_ATOP)
                deleteImageDrawable.setColorFilter(ContextCompat.getColor(this, id), PorterDuff.Mode.SRC_ATOP)
            }
        }
        else {
            val typedValue = TypedValue()
            theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val resourceId = typedValue.resourceId
            navigationDrawable?.setColorFilter(ContextCompat.getColor(this, resourceId), PorterDuff.Mode.SRC_ATOP)
            deleteImageDrawable.setColorFilter(ContextCompat.getColor(this, resourceId), PorterDuff.Mode.SRC_ATOP)
        }

        viewImageMaterialToolbar.setNavigationOnClickListener {
            Vibrate().vibration(this)
            imageView.resetZoom()
            if (ColoredNavBarData(this).loadNavBar()) {
                this@ImageViewActivity.window.navigationBarColor =
                    Color.parseColor(CustomColorGenerator(this).generateBottomNavBackgroundColor())
            } else {
                this@ImageViewActivity.window.navigationBarColor =
                    Color.parseColor("#000000")
            }
            //if (AnimationData(this).loadImageAnimation()) {
                finishAfterTransition()
            //}
            //else {
                //finish()
           // }
        }
        viewImageMaterialToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.deleteImage -> {
                    Vibrate().vibration(this)
                    val deleteImageDialog = BottomSheetDialog(this)
                    val deleteImageDialogLayout =
                        layoutInflater.inflate(R.layout.delete_image_bottom_sheet, null)

                    deleteImageDialog.setContentView(deleteImageDialogLayout)

                    val infoCardView =
                        deleteImageDialogLayout.findViewById<MaterialCardView>(R.id.infoCardView)
                    val yesButton = deleteImageDialogLayout.findViewById<Button>(R.id.yesButton)
                    val noButton = deleteImageDialogLayout.findViewById<Button>(R.id.noButton)

                    infoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(this).generateCardColor()))
                    yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(this).generateCustomColorPrimary()))
                    noButton.setTextColor(Color.parseColor(CustomColorGenerator(this).generateCustomColorPrimary()))

                    yesButton.setOnClickListener {
                        Vibrate().vibration(this)
                        deleteImageDialog.dismiss()
                        val imageCursor = TimeCardDBHelper(this, null).getImage(id!!)
                        imageCursor.moveToFirst()

                        val imagePath = imageCursor.getString(imageCursor.getColumnIndex(TimeCardDBHelper.COLUMN_IMAGE))
                        TimeCardDBHelper(this, null).addImage(id, "")

                        val snackBar = Snackbar.make(
                            imageView,
                            "Image deleted",
                            Snackbar.LENGTH_LONG
                        )
                            .setDuration(5000)

                        snackBar.apply {
                            snackBar.view.background = ResourcesCompat.getDrawable(
                                this@ImageViewActivity.resources,
                                R.drawable.snackbar_corners,
                                this@ImageViewActivity.theme
                            )
                        }
                        snackBar.setActionTextColor(
                            Color.parseColor(CustomColorGenerator(this).generateSnackbarActionTextColor())
                        )

                        snackBar.setAction(getString(R.string.undo)) {
                            Vibrate().vibration(this)
                            TimeCardDBHelper(this, null).addImage(id, imagePath)
                            finish()
                        }
                        snackBar.show()
                        //deleteImageDialog.dismiss()
                        Handler().postDelayed({
                            finish()
                        }, 5000)
                        //Toast.makeText(this, "Image Deleted", Toast.LENGTH_SHORT).show()
                    }
                    noButton.setOnClickListener {
                        Vibrate().vibration(this)
                        deleteImageDialog.dismiss()
                        if (ColoredNavBarData(this).loadNavBar()) {
                            this@ImageViewActivity.window.navigationBarColor =
                                Color.parseColor(CustomColorGenerator(this).generateBottomNavBackgroundColor())
                        } else {
                            this@ImageViewActivity.window.navigationBarColor =
                                Color.parseColor("#000000")
                        }
                        //if (AnimationData(this).loadImageAnimation()) {
                            finishAfterTransition()
                        //}
                        //else {
                            //finish()
                        //}
                    }
                    deleteImageDialog.show()
                    return@setOnMenuItemClickListener true
                }
                else -> false
            }
        }

        try {

            val imageCursor = TimeCardDBHelper(this, null).getImage(id!!)
            imageCursor.moveToFirst()

            val imageMap = HashMap<String, String>()
            imageMap["image"] =
                imageCursor.getString(imageCursor.getColumnIndex(TimeCardDBHelper.COLUMN_IMAGE))

            val bitmapSrc = BitmapFactory.decodeFile(imageMap["image"])

            val ei = ExifInterface(imageMap["image"]!!)

            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            val m = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    m.postRotate(90f)
                }
                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    m.postRotate(180f)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    m.postRotate(270f)
                }
            }
            val bitmapScaled =
                Bitmap.createScaledBitmap(bitmapSrc, bitmapSrc.width, bitmapSrc.height, true)
            val bitmap =
                Bitmap.createBitmap(
                    bitmapScaled,
                    0,
                    0,
                    bitmapScaled.width,
                    bitmapScaled.height,
                    m,
                    true
                )
            imageView.setImageBitmap(bitmap)
            if (MatchImageViewContentsBackgroundData(this).loadMatchImageViewContents()) {
                setBackgroundColor(bitmap)
            }

        } catch (e: java.lang.NullPointerException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //imageView.setZoom(1f)
                imageView.resetZoom()
                if (ColoredNavBarData(this@ImageViewActivity).loadNavBar()) {
                    this@ImageViewActivity.window.navigationBarColor =
                        Color.parseColor(CustomColorGenerator(this@ImageViewActivity).generateBottomNavBackgroundColor())
                } else {
                    this@ImageViewActivity.window.navigationBarColor =
                        Color.parseColor("#000000")
                }
                finishAfterTransition()
            }
        })
    }

    private fun setBackgroundColor(bitmap: Bitmap) {
        Palette.Builder(bitmap).generate { palette ->
            val vSwatch = palette?.dominantSwatch?.rgb
            val color =
                Color.rgb(
                    vSwatch!!.red,
                    vSwatch.green,
                    vSwatch.blue
                )

            this@ImageViewActivity.window.navigationBarColor = color
            this@ImageViewActivity.window.statusBarColor = color

            val invertedColor = Color.rgb(
                255 - vSwatch.red, 255 - vSwatch.green,
                255 - vSwatch.blue
            )

            try {
                val imageViewConstraint =
                    findViewById<ConstraintLayout>(R.id.imageViewConstraint)
                imageViewConstraint.setBackgroundColor(color)
                findViewById<MaterialToolbar>(R.id.viewImageToolBar).setNavigationIconTint(
                    invertedColor
                )

                val viewImageMaterialToolbar = findViewById<MaterialToolbar>(R.id.viewImageToolBar)

                val deleteImageDrawable =
                    viewImageMaterialToolbar.menu.findItem(R.id.deleteImage).icon
                deleteImageDrawable!!.mutate()

                val navigationDrawable = viewImageMaterialToolbar?.navigationIcon
                navigationDrawable?.mutate()

                if (Build.VERSION.SDK_INT >= 29) {
                    try {

                        deleteImageDrawable.colorFilter = BlendModeColorFilter(
                            invertedColor,
                            BlendMode.SRC_ATOP
                        )
                        navigationDrawable?.colorFilter = BlendModeColorFilter(
                            invertedColor,
                            BlendMode.SRC_ATOP
                        )
                    } catch (e: NoClassDefFoundError) {
                        e.printStackTrace()
                        val typedValue = TypedValue()
                        theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                        val id = typedValue.resourceId
                        navigationDrawable?.setColorFilter(ContextCompat.getColor(this, id), PorterDuff.Mode.SRC_ATOP)
                        deleteImageDrawable?.setColorFilter(ContextCompat.getColor(this, id), PorterDuff.Mode.SRC_ATOP)
                    }
                }
                else {
                    val typedValue = TypedValue()
                    theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                    val id = typedValue.resourceId
                    navigationDrawable?.setColorFilter(ContextCompat.getColor(this, id), PorterDuff.Mode.SRC_ATOP)
                    deleteImageDrawable?.setColorFilter(ContextCompat.getColor(this, id), PorterDuff.Mode.SRC_ATOP)
                }

            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }
}