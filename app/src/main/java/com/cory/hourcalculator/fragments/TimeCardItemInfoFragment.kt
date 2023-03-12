package com.cory.hourcalculator.fragments

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.TimeCardItemCustomAdapter
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import com.cory.hourcalculator.intents.ImageViewActivity
import com.cory.hourcalculator.intents.MainActivity
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

class TimeCardItemInfoFragment : Fragment() {

    private lateinit var managePermissions: ManagePermissions

    private val timeCardItemInfoDataList = ArrayList<HashMap<String, String>>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var timeCardItemCustomAdapter: TimeCardItemCustomAdapter

    private lateinit var editable: Editable

    var themeSelection = false
    private lateinit var id: String

    private lateinit var image: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                activity?.setTheme(R.style.Theme_DarkTheme)
                themeSelection = true
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
                themeSelection = false
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
                themeSelection = true
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                        themeSelection = false
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(
                            AccentColor(requireContext()).followSystemTheme(
                                requireContext()
                            )
                        )
                        themeSelection = true
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                        themeSelection = true
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_time_card_item_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                requireActivity(),
                list,
                1
            )

        try {
            val runnable = Runnable {
                //(activity as MainActivity).setActiveTab(2)
            }

            MainActivity().runOnUiThread(runnable)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val topAppBarTimeCardItem =
            view.findViewById<MaterialToolbar>(R.id.materialToolBarTimeCardItemInfo)
        topAppBarTimeCardItem?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val addDrawable = topAppBarTimeCardItem?.menu?.findItem(R.id.add)?.icon
        addDrawable?.mutate()

        val navigationDrawable = topAppBarTimeCardItem?.navigationIcon
        navigationDrawable?.mutate()

        val collapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutTimeCardItemInfo)

        collapsingToolbarLayout.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )
        collapsingToolbarLayout.setStatusBarScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )

        if (MenuTintData(requireContext()).loadMenuTint()) {

            addDrawable?.colorFilter = BlendModeColorFilter(
                Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                BlendMode.SRC_ATOP
            )
            navigationDrawable?.colorFilter = BlendModeColorFilter(
                Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                BlendMode.SRC_ATOP
            )
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            addDrawable?.colorFilter = BlendModeColorFilter(
                ContextCompat.getColor(requireContext(), id),
                BlendMode.SRC_ATOP
            )
            navigationDrawable?.colorFilter = BlendModeColorFilter(
                ContextCompat.getColor(requireContext(), id),
                BlendMode.SRC_ATOP
            )
        }

        topAppBarTimeCardItem.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add -> {
                    Vibrate().vibration(requireContext())
                    val dialog = BottomSheetDialog(requireContext())
                    val addAPhotoLayout =
                        layoutInflater.inflate(R.layout.add_photo_bottom_sheet, null)
                    dialog.window?.navigationBarColor =
                        ContextCompat.getColor(requireContext(), R.color.black)
                    dialog.setContentView(addAPhotoLayout)

                    val selectAPhotoCardView =
                        addAPhotoLayout.findViewById<MaterialCardView>(R.id.selectAPhotoCardView)
                    val takeAPhotoCardView =
                        addAPhotoLayout.findViewById<MaterialCardView>(R.id.addAPhotoCardView)

                    selectAPhotoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                    takeAPhotoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

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
                        Vibrate().vibration(requireContext())
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
                        Vibrate().vibration(requireContext())
                        list = listOf(
                            android.Manifest.permission.CAMERA
                        )

                        managePermissions =
                            ManagePermissions(
                                requireActivity(),
                                list,
                                1
                            )
                        //Toast.makeText(requireContext(), managePermissions.checkPermissions(requireContext()).toString(), Toast.LENGTH_SHORT).show()
                        if (managePermissions.checkPermissions(requireContext())) {

                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                                var photFile: File? = null

                                try {
                                    photFile = createImageFile()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }

                                if (photFile != null) {
                                    val photoUri = FileProvider.getUriForFile(
                                        requireContext(),
                                        "com.cory.hourcalculator.FileProvider",
                                        photFile
                                    )
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                                    showCamera.launch(intent)
                                }
                            }
                            dialog.dismiss()
                        } else {
                            managePermissions.showAlert(requireContext())
                        }
                    }
                    dialog.show()
                    return@setOnMenuItemClickListener true
                }
                else -> false
            }
        }

        timeCardItemCustomAdapter =
            TimeCardItemCustomAdapter(requireContext(), timeCardItemInfoDataList)
        linearLayoutManager = LinearLayoutManager(requireContext())

        activity?.window?.setBackgroundDrawable(null)

        id = arguments?.getString("id")!!
        val name = arguments?.getString("name")

        loadIntoList(id)

        val timeCardInfoNameTextInput =
            requireActivity().findViewById<TextInputEditText>(R.id.timeCardInfoNameTextInput)
        val textInputLayoutName =
            requireActivity().findViewById<TextInputLayout>(R.id.textInputLayoutName)

        textInputLayoutName?.boxStrokeColor =
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        textInputLayoutName?.hintTextColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        timeCardInfoNameTextInput.textCursorDrawable = null
        timeCardInfoNameTextInput.highlightColor =
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        timeCardInfoNameTextInput.setTextIsSelectable(false)

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutTimeCardItemInfo)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutTimeCardItemInfo)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
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
                    requireContext(),
                    null
                ).updateName(timeCardInfoNameTextInput.text.toString(), id)
                hideKeyboard(timeCardInfoNameTextInput)
                return@OnKeyListener true
            }
            false
        })

        timeCardInfoNameTextInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                TimeCardDBHelper(requireContext(), null).updateName(s.toString(), id)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TimeCardDBHelper(requireContext(), null).updateName(s.toString(), id)
            }
        })
        //loadIntoList(id)

        val imageView = activity?.findViewById<ImageView>(R.id.timeCardImage)
        imageView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            val intent = Intent(requireContext(), ImageViewActivity::class.java)
            intent.putExtra("id", id)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                imageView,
                "transition_image"
            )
            startActivity(intent, options.toBundle())
        }

        val timeCardItemInfoRecyclerView =
            activity?.findViewById<RecyclerView>(R.id.timeCardItemInfoRecyclerView)
        val animation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.listview_animation)
        timeCardItemInfoRecyclerView?.layoutAnimation = animation

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.supportFragmentManager?.popBackStack()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        loadIntoList(id)
    }

    private fun loadIntoList(id: String) {

        val dbHandler = TimeCardsItemDBHelper(requireActivity().applicationContext, null)

        timeCardItemInfoDataList.clear()
        val cursor = dbHandler.getAllRow(requireContext(), id)
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
            activity?.findViewById<RecyclerView>(R.id.timeCardItemInfoRecyclerView)
        timeCardRecyclerView?.layoutManager = linearLayoutManager
        timeCardRecyclerView?.adapter = timeCardItemCustomAdapter

        val imageCursor = TimeCardDBHelper(requireContext(), null).getImage(id)
        imageCursor.moveToFirst()
        val imageView = requireView().findViewById<ImageView>(R.id.timeCardImage)
        if (imageCursor.getString(imageCursor.getColumnIndex(TimeCardDBHelper.COLUMN_IMAGE)) != null) {
            val imagePath =
                File(imageCursor.getString(imageCursor.getColumnIndex(TimeCardDBHelper.COLUMN_IMAGE)))
            //Toast.makeText(requireContext(), imagePath.exists().toString(), Toast.LENGTH_SHORT).show()
            if (imagePath.exists()) {
                Glide.with(requireContext())
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
                    activity?.contentResolver?.openInputStream(
                        uri!!
                    )
                val imageBitmap = BitmapFactory.decodeStream(imageStream)
                val stream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                // val byteArray = stream.toByteArray()
                val selectedFile = File(getRealPathFromURI(uri!!))
                this.image = selectedFile.toString()
                val readUriPermission = Intent.FLAG_GRANT_READ_URI_PERMISSION
                activity?.contentResolver?.takePersistableUriPermission(uri, readUriPermission)
                Toast.makeText(requireContext(), "Image selected", Toast.LENGTH_SHORT).show()
                TimeCardDBHelper(requireContext(), null).addImage(id, image)
                loadIntoList(id)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Adding image failed", Toast.LENGTH_SHORT).show()
            }
        }

    private fun getRealPathFromURI(contentURI: Uri): String {
        var result = ""
        val cursor = requireActivity().contentResolver?.query(contentURI, null, null, null, null)
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
            MediaScannerConnection.scanFile(requireContext(), arrayOf(image.toString()), null, null)

            this.image = image.toString()

            Toast.makeText(requireContext(), "Picture taken and added", Toast.LENGTH_SHORT).show()
            TimeCardDBHelper(requireContext(), null).addImage(id, image.toString())
            loadIntoList(id)
        } else {
            Toast.makeText(requireContext(), "Adding image failed", Toast.LENGTH_SHORT).show()
        }
    }

    private var currentPhotoPath = ""

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir: File =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
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
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = activity?.currentFocus

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