package com.cory.hourcalculator.intents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.*
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.TimeCardItemCustomAdapter
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ViewTimeCardInfoActivity : AppCompatActivity() {

    private lateinit var managePermissions: ManagePermissions

    private val timeCardItemInfoDataList = ArrayList<HashMap<String, String>>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var timeCardItemCustomAdapter: TimeCardItemCustomAdapter

    private lateinit var editable: Editable

    private lateinit var id: String

    private lateinit var image: String

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
                window.statusBarColor = Color.parseColor("#000000")
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
        setContentView(R.layout.activity_view_time_card_info)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
            }
        }

        var list = listOf<String>()
        list = if (Build.VERSION.SDK_INT >= 33) {
            listOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            listOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        managePermissions =
            ManagePermissions(
                this,
                list,
                1
            )

        val topAppBarTimeCardItem =
            findViewById<MaterialToolbar>(R.id.materialToolBarTimeCardItemInfoActivity)
        topAppBarTimeCardItem?.setNavigationOnClickListener {
            Vibrate().vibration(this)
            finishAfterTransition()
        }

        this.findViewById<MaterialCardView>(R.id.timeCardInfoImageViewWrapperActivity).setCardBackgroundColor(
                Color.parseColor(CustomColorGenerator(this).generatePatchNotesCardColor())
            )

        val addDrawable = topAppBarTimeCardItem?.menu?.findItem(R.id.add)?.icon
        addDrawable?.mutate()

        val navigationDrawable = topAppBarTimeCardItem?.navigationIcon
        navigationDrawable?.mutate()

        val collapsingToolbarLayout =
            findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutTimeCardItemInfoActivity)

        collapsingToolbarLayout.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    this
                ).generateTopAppBarColor()
            )
        )
        collapsingToolbarLayout.setStatusBarScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    this
                ).generateTopAppBarColor()
            )
        )

        if (MenuTintData(this).loadMenuTint()) {

            addDrawable?.colorFilter = BlendModeColorFilter(
                Color.parseColor(CustomColorGenerator(this).generateMenuTintColor()),
                BlendMode.SRC_ATOP
            )
            navigationDrawable?.colorFilter = BlendModeColorFilter(
                Color.parseColor(CustomColorGenerator(this).generateMenuTintColor()),
                BlendMode.SRC_ATOP
            )
        } else {
            val typedValue = TypedValue()
            this.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            addDrawable?.colorFilter = BlendModeColorFilter(
                ContextCompat.getColor(this, id),
                BlendMode.SRC_ATOP
            )
            navigationDrawable?.colorFilter = BlendModeColorFilter(
                ContextCompat.getColor(this, id),
                BlendMode.SRC_ATOP
            )
        }

        topAppBarTimeCardItem.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add -> {
                    Vibrate().vibration(this)
                    val dialog = BottomSheetDialog(this)
                    val addAPhotoLayout =
                        layoutInflater.inflate(R.layout.add_photo_bottom_sheet, null)
                    dialog.window?.navigationBarColor =
                        ContextCompat.getColor(this, R.color.black)
                    dialog.setContentView(addAPhotoLayout)

                    val selectAPhotoCardView =
                        addAPhotoLayout.findViewById<MaterialCardView>(R.id.selectAPhotoCardView)
                    val takeAPhotoCardView =
                        addAPhotoLayout.findViewById<MaterialCardView>(R.id.addAPhotoCardView)

                    selectAPhotoCardView.setCardBackgroundColor(
                        Color.parseColor(
                            CustomColorGenerator(this).generateCardColor()
                        )
                    )
                    takeAPhotoCardView.setCardBackgroundColor(
                        Color.parseColor(
                            CustomColorGenerator(
                                this
                            ).generateCardColor()
                        )
                    )

                    selectAPhotoCardView.shapeAppearanceModel =
                        selectAPhotoCardView.shapeAppearanceModel
                            .toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                            .setBottomRightCornerSize(0f)
                            .setBottomLeftCornerSize(0f)
                            .build()
                    takeAPhotoCardView.shapeAppearanceModel =
                        takeAPhotoCardView.shapeAppearanceModel
                            .toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                            .setBottomRightCornerSize(28f)
                            .setBottomLeftCornerSize(28f)
                            .build()

                    selectAPhotoCardView.setOnClickListener {
                        Vibrate().vibration(this)
                        val pickerIntent = Intent(Intent.ACTION_PICK)
                        pickerIntent.type = "image/*"

                        showImagePickerAndroid13.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                        dialog.dismiss()
                    }
                    takeAPhotoCardView.setOnClickListener {
                        Vibrate().vibration(this)
                        list = listOf(
                            android.Manifest.permission.CAMERA
                        )

                        managePermissions =
                            ManagePermissions(
                                this,
                                list,
                                1
                            )
                        //Toast.makeText(this, managePermissions.checkPermissions(this).toString(), Toast.LENGTH_SHORT).show()
                        if (managePermissions.checkPermissions(this)) {

                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            if (intent.resolveActivity(this.packageManager) != null) {
                                var photFile: File? = null

                                try {
                                    photFile = createImageFile()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }

                                if (photFile != null) {
                                    val photoUri = FileProvider.getUriForFile(
                                        this,
                                        "com.cory.hourcalculator.FileProvider",
                                        photFile
                                    )
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                                    showCamera.launch(intent)
                                }
                            }
                            dialog.dismiss()
                        } else {
                            managePermissions.showAlert(this)
                        }
                    }
                    dialog.show()
                    return@setOnMenuItemClickListener true
                }
                else -> false
            }
        }

        timeCardItemCustomAdapter =
            TimeCardItemCustomAdapter(this, timeCardItemInfoDataList)
        linearLayoutManager = LinearLayoutManager(this)

        window?.setBackgroundDrawable(null)

        id = intent.getStringExtra("id")!!
        val name = intent.getStringExtra("name")

        loadIntoList(id)

        val timeCardInfoNameTextInput =
            this.findViewById<TextInputEditText>(R.id.timeCardInfoNameTextInputActivity)
        val textInputLayoutName =
            this.findViewById<TextInputLayout>(R.id.textInputLayoutNameActivity)

        textInputLayoutName?.boxStrokeColor =
            Color.parseColor(CustomColorGenerator(this).generateCustomColorPrimary())
        textInputLayoutName?.hintTextColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateCustomColorPrimary()))
        timeCardInfoNameTextInput.textCursorDrawable = null
        timeCardInfoNameTextInput.highlightColor =
            Color.parseColor(CustomColorGenerator(this).generateCustomColorPrimary())
        timeCardInfoNameTextInput.setTextIsSelectable(false)

        if (ColoredTitleBarTextData(this).loadTitleBarTextState()) {
            this.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutTimeCardItemInfoActivity)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(this).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            this.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            this.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutTimeCardItemInfoActivity)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(this, id))
        }

        editable = if (name != null) {
            Editable.Factory.getInstance().newEditable(name)
        } else {
            Editable.Factory.getInstance().newEditable("")
        }
        timeCardInfoNameTextInput?.text = editable

        timeCardInfoNameTextInput?.setOnKeyListener(View.OnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard(timeCardInfoNameTextInput)
                return@OnKeyListener true
            }
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                TimeCardDBHelper(
                    this,
                    null
                ).updateName(timeCardInfoNameTextInput.text.toString(), id)
                hideKeyboard(timeCardInfoNameTextInput)
                return@OnKeyListener true
            }
            false
        })

        timeCardInfoNameTextInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                TimeCardDBHelper(this@ViewTimeCardInfoActivity, null).updateName(s.toString(), id)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TimeCardDBHelper(this@ViewTimeCardInfoActivity, null).updateName(s.toString(), id)
            }
        })

        val imageView = findViewById<ImageView>(R.id.timeCardImageActivity)
        imageView?.setOnClickListener {
            Vibrate().vibration(this)
            val intent = Intent(this, ImageViewActivity::class.java)
            intent.putExtra("id", id)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                imageView,
                "transition_image"
            )
            startActivity(intent, options.toBundle())
        }

    }

    private fun loadIntoList(id: String) {

        val dbHandler = TimeCardsItemDBHelper(this, null)

        timeCardItemInfoDataList.clear()
        val cursor = dbHandler.getAllRow(this, id)
        cursor!!.moveToFirst()


        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(TimeCardsItemDBHelper.COLUMN_ID))
            map["inTime"] = cursor.getString(cursor.getColumnIndex(TimeCardsItemDBHelper.COLUMN_IN))
            map["outTime"] =
                cursor.getString(cursor.getColumnIndex(TimeCardsItemDBHelper.COLUMN_OUT))
            map["totalHours"] =
                cursor.getString(cursor.getColumnIndex(TimeCardsItemDBHelper.COLUMN_TOTAL))
            map["breakTime"] =
                cursor.getString(cursor.getColumnIndex(TimeCardsItemDBHelper.COLUMN_BREAK))
            map["day"] = cursor.getString(cursor.getColumnIndex(TimeCardsItemDBHelper.COLUMN_DAY))
            timeCardItemInfoDataList.add(map)

            cursor.moveToNext()
        }


        val timeCardRecyclerView =
            findViewById<RecyclerView>(R.id.timeCardItemInfoRecyclerViewActivity)
        timeCardRecyclerView?.layoutManager = linearLayoutManager
        timeCardRecyclerView?.adapter = timeCardItemCustomAdapter

        val imageCursor = TimeCardDBHelper(this, null).getImage(id)
        imageCursor.moveToFirst()
        val imageView = findViewById<ImageView>(R.id.timeCardImageActivity)
        if (imageCursor.getString(imageCursor.getColumnIndex(TimeCardDBHelper.COLUMN_IMAGE)) != null) {
            val imagePath =
                File(imageCursor.getString(imageCursor.getColumnIndex(TimeCardDBHelper.COLUMN_IMAGE)))
            //Toast.makeText(this, imagePath.exists().toString(), Toast.LENGTH_SHORT).show()
            if (imagePath.exists()) {
                Glide.with(this)
                    .load(imagePath)
                    .into(imageView)
                imageView.visibility = View.VISIBLE
            } else {
                imageView.visibility = View.GONE
            }
        } else {
            imageView.visibility = View.GONE
        }
    }

    private val showImagePickerAndroid13 =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

            var imageStream: InputStream? = null
            try {
                imageStream =
                    this.contentResolver?.openInputStream(
                        uri!!
                    )
                val imageBitmap = BitmapFactory.decodeStream(imageStream)
                val stream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                // val byteArray = stream.toByteArray()
                val selectedFile = File(getRealPathFromURI(uri!!))
                this.image = selectedFile.toString()
                val readUriPermission = Intent.FLAG_GRANT_READ_URI_PERMISSION
                this.contentResolver?.takePersistableUriPermission(uri, readUriPermission)
                Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
                TimeCardDBHelper(this, null).addImage(id, image)
                loadIntoList(id)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Adding image failed", Toast.LENGTH_SHORT).show()
            }
        }

    private fun getRealPathFromURI(contentURI: Uri): String {
        var result = ""
        val cursor = this.contentResolver?.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path.toString()
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    private val showCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            val ei = ExifInterface(currentPhotoPath)
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

            val originalBitmap = BitmapFactory.decodeFile(currentPhotoPath)

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
                .format(System.currentTimeMillis())
            val storageDir = File(
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString() + "/HourCalculator/"
            )

            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }
            val image = File.createTempFile(timeStamp, ".jpeg", storageDir)

            val f = File(image.toString())
            val fileOutputStream = FileOutputStream(f)
            val rotatedBitmap = Bitmap.createBitmap(
                originalBitmap,
                0,
                0,
                originalBitmap.width,
                originalBitmap.height,
                m,
                true
            )
            val bitmap = rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            MediaScannerConnection.scanFile(this, arrayOf(image.toString()), null, null)

            this.image = image.toString()

            Toast.makeText(this, "Picture taken and added", Toast.LENGTH_SHORT).show()
            TimeCardDBHelper(this, null).addImage(id, image.toString())
            loadIntoList(id)
        } else {
            Toast.makeText(this, "Adding image failed", Toast.LENGTH_SHORT).show()
        }
    }

    private var currentPhotoPath = ""

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir: File =
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".png", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun hideKeyboard(nameEditText: TextInputEditText) {
        val inputManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = this.currentFocus

        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(
                focusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            if (nameEditText.hasFocus()) {
                nameEditText.clearFocus()
            }
        }
    }
}