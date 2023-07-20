package com.cory.hourcalculator.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
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
import com.cory.hourcalculator.sharedprefs.AnimationData
import com.cory.hourcalculator.sharedprefs.ColoredTitleBarTextData
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.cory.hourcalculator.sharedprefs.MenuTintData
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class TimeCardItemInfoFragment : Fragment() {

    private lateinit var dialog : BottomSheetDialog
    private lateinit var imageViewOptionsDialog : BottomSheetDialog
    private lateinit var deleteImageDialog: BottomSheetDialog

    private lateinit var managePermissions: ManagePermissions

    private val timeCardItemInfoDataList = ArrayList<HashMap<String, String>>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var timeCardItemCustomAdapter: TimeCardItemCustomAdapter

    private lateinit var editable: Editable

    private lateinit var id: String

    private lateinit var image: String

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                //activity?.setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(
                            R.style.Theme_AMOLED
                        )
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }

        timeCardItemCustomAdapter.updateCardColor()
        updateCustomColor()

        try {
            if (dialog.isShowing) {
                dialog.dismiss()
                addImage()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            if (imageViewOptionsDialog.isShowing) {
                imageViewOptionsDialog.dismiss()
                imageViewOptions()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            if (deleteImageDialog.isShowing) {
                deleteImageDialog.dismiss()
                deleteImage()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                //activity?.setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(
                            R.style.Theme_AMOLED
                        )
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_time_card_item_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 2
            (activity as MainActivity).setActiveTab(2)
        }
        MainActivity().runOnUiThread(runnable)

        updateCustomColor()

        val topAppBarTimeCardItem =
            view.findViewById<MaterialToolbar>(R.id.materialToolBarTimeCardItemInfo)
        topAppBarTimeCardItem?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        topAppBarTimeCardItem.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add -> {
                    Vibrate().vibration(requireContext())
                    addImage()
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

        val imageView = activity?.findViewById<ImageView>(R.id.timeCardImage)
        imageView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            val intent = Intent(requireContext(), ImageViewActivity::class.java)
            intent.putExtra("id", id)
            if (AnimationData(requireContext()).loadImageAnimation()) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    requireActivity(),
                    imageView,
                    "transition_image"
                )
                startActivity(intent, options.toBundle())
            }
            else {
              startActivity(intent)
            }
        }

        imageView?.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            imageViewOptions()
            return@setOnLongClickListener true
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (timeCardInfoNameTextInput!!.hasFocus()) {
                        timeCardInfoNameTextInput.clearFocus()
                    } else {
                        activity?.supportFragmentManager?.popBackStack()
                    }
                }
            })
    }

    private fun imageViewOptions() {
        imageViewOptionsDialog = BottomSheetDialog(requireContext())
        val addAPhotoLayout =
            layoutInflater.inflate(R.layout.image_options_bottom_sheet, null)
        imageViewOptionsDialog.setContentView(addAPhotoLayout)

        if (resources.getBoolean(R.bool.isTablet)) {
            val bottomSheet =
                imageViewOptionsDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.isDraggable = false
        }

        val removePhotoCardView =
            addAPhotoLayout.findViewById<MaterialCardView>(R.id.removePhotoCardView)
        val selectAPhotoCardView =
            addAPhotoLayout.findViewById<MaterialCardView>(R.id.selectAPhotoCardView)
        val takeAPhotoCardView =
            addAPhotoLayout.findViewById<MaterialCardView>(R.id.addAPhotoCardView)

        removePhotoCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )
        selectAPhotoCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )
        takeAPhotoCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )

        removePhotoCardView.shapeAppearanceModel =
            removePhotoCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()

        val hasCamera = requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)

        if (!hasCamera) {
            takeAPhotoCardView.visibility = View.GONE
            selectAPhotoCardView.shapeAppearanceModel =
                selectAPhotoCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(28f)
                    .setBottomLeftCornerSize(28f)
                    .build()
        }
        else {
            selectAPhotoCardView.shapeAppearanceModel =
                selectAPhotoCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
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
        }

        if (requireActivity().findViewById<ImageView>(R.id.timeCardImage).visibility == View.GONE) {
            removePhotoCardView.visibility = View.GONE
            val layoutParams: MarginLayoutParams =
                selectAPhotoCardView.layoutParams as MarginLayoutParams
            layoutParams.setMargins(0, 15, 0, 0)
            selectAPhotoCardView.requestLayout()

            selectAPhotoCardView.shapeAppearanceModel =
                selectAPhotoCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
        }

        removePhotoCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            deleteImage()
        }

        selectAPhotoCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            val pickerIntent = Intent(Intent.ACTION_PICK)
            pickerIntent.type = "image/*"

            showImagePickerAndroid13.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
            imageViewOptionsDialog.dismiss()
        }

        takeAPhotoCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            val list = listOf(
                Manifest.permission.CAMERA
            )

            managePermissions =
                ManagePermissions(
                    requireActivity(),
                    list,
                    1
                )
            //Toast.makeText(requireContext(), managePermissions.checkPermissions(requireContext()).toString(), Toast.LENGTH_SHORT).show()
            if (managePermissions.checkPermissions()) {

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
                            photFile!!
                        )
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        showCamera.launch(intent)
                    }
                }
                imageViewOptionsDialog.dismiss()
            } else {
                managePermissions.showAlert(requireContext())
            }
        }
        imageViewOptionsDialog.show()
    }

    private fun deleteImage() {
        deleteImageDialog = BottomSheetDialog(requireContext())
        val deleteImageDialogLayout =
            layoutInflater.inflate(R.layout.delete_image_bottom_sheet, null)

        deleteImageDialog.setContentView(deleteImageDialogLayout)


        if (resources.getBoolean(R.bool.isTablet)) {
            val bottomSheet =
                deleteImageDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.isDraggable = false
        }

        val infoCardView =
            deleteImageDialogLayout.findViewById<MaterialCardView>(R.id.infoCardView)
        val yesButton = deleteImageDialogLayout.findViewById<Button>(R.id.yesButton)
        val noButton = deleteImageDialogLayout.findViewById<Button>(R.id.noButton)

        infoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        noButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

        yesButton.setOnClickListener {
            Vibrate().vibration(requireContext())
            try {
                imageViewOptionsDialog.dismiss()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            try {
                dialog.dismiss()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            deleteImageDialog.dismiss()
            TimeCardDBHelper(requireContext(), null).addImage(id, "")
            loadIntoList(id)
            Toast.makeText(requireContext(), "Image Deleted", Toast.LENGTH_SHORT).show()
        }
        noButton.setOnClickListener {
            Vibrate().vibration(requireContext())
            deleteImageDialog.dismiss()
            loadIntoList(id)
        }
        deleteImageDialog.show()
    }

    private fun addImage() {
        var list: List<String> = if (Build.VERSION.SDK_INT >= 33) {
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
        dialog = BottomSheetDialog(requireContext())
        val addAPhotoLayout =
            layoutInflater.inflate(R.layout.image_options_bottom_sheet, null)
        dialog.setContentView(addAPhotoLayout)

        if (resources.getBoolean(R.bool.isTablet)) {
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.isDraggable = false
        }

        val removePhotoCardView =
            addAPhotoLayout.findViewById<MaterialCardView>(R.id.removePhotoCardView)
        val selectAPhotoCardView =
            addAPhotoLayout.findViewById<MaterialCardView>(R.id.selectAPhotoCardView)
        val takeAPhotoCardView =
            addAPhotoLayout.findViewById<MaterialCardView>(R.id.addAPhotoCardView)

        removePhotoCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )
        selectAPhotoCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )
        takeAPhotoCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )

        removePhotoCardView.shapeAppearanceModel =
            removePhotoCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
        val hasCamera = requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)

        if (!hasCamera) {
            takeAPhotoCardView.visibility = View.GONE
            selectAPhotoCardView.shapeAppearanceModel =
                selectAPhotoCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(28f)
                    .setBottomLeftCornerSize(28f)
                    .build()
        }
        else {
            selectAPhotoCardView.shapeAppearanceModel =
                selectAPhotoCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
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
        }

        if (requireActivity().findViewById<ImageView>(R.id.timeCardImage).visibility == View.GONE) {
            removePhotoCardView.visibility = View.GONE
            val layoutParams: MarginLayoutParams =
                selectAPhotoCardView.layoutParams as MarginLayoutParams
            layoutParams.setMargins(0, 25, 0, 0)
            selectAPhotoCardView.requestLayout()

            if (hasCamera) {
                selectAPhotoCardView.shapeAppearanceModel =
                    selectAPhotoCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                        .setBottomRightCornerSize(0f)
                        .setBottomLeftCornerSize(0f)
                        .build()
            }
            else {
                selectAPhotoCardView.shapeAppearanceModel =
                    selectAPhotoCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                        .setBottomRightCornerSize(28f)
                        .setBottomLeftCornerSize(28f)
                        .build()
            }
        }

        removePhotoCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            deleteImage()
        }

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
            val list1 = listOf(
                Manifest.permission.CAMERA
            )

            managePermissions =
                ManagePermissions(
                    requireActivity(),
                    list1,
                    1
                )
            //Toast.makeText(requireContext(), managePermissions.checkPermissions(requireContext()).toString(), Toast.LENGTH_SHORT).show()
            if (managePermissions.checkPermissions()) {

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
    }

    override fun onResume() {
        super.onResume()
        loadIntoList(id)
    }

    private fun loadIntoList(id: String) {

        val dbHandler = TimeCardsItemDBHelper(requireActivity().applicationContext, null)

        timeCardItemInfoDataList.clear()
        val cursor = dbHandler.getAllRow(id)
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

            val imageStream: InputStream?
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
        val result: String
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
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            MediaScannerConnection.scanFile(requireContext(), arrayOf(image.toString()), null, null)

            this.image = image.toString()

            Toast.makeText(requireContext(), getString(R.string.picture_taken_and_added), Toast.LENGTH_SHORT).show()
            TimeCardDBHelper(requireContext(), null).addImage(id, image.toString())
            loadIntoList(id)
        } else {
            Toast.makeText(requireContext(), getString(R.string.adding_image_failed), Toast.LENGTH_SHORT).show()
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

    fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.timeCardItemInfoCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        requireActivity().findViewById<MaterialCardView>(R.id.timeCardInfoImageViewWrapper).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generatePatchNotesCardColor()))

        val topAppBarTimeCardItem =
            requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarTimeCardItemInfo)
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

        if (Build.VERSION.SDK_INT >= 29) {
            try {
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
            } catch (e: NoClassDefFoundError) {
                e.printStackTrace()
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                val id = typedValue.resourceId
                navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
                addDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            }
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            addDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
        }

        val timeCardInfoNameTextInput =
            requireActivity().findViewById<TextInputEditText>(R.id.timeCardInfoNameTextInput)
        val textInputLayoutName =
            requireActivity().findViewById<TextInputLayout>(R.id.textInputLayoutName)

        textInputLayoutName?.boxStrokeColor = Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        textInputLayoutName?.hintTextColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        timeCardInfoNameTextInput.textCursorDrawable = null
        textInputLayoutName?.defaultHintTextColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        timeCardInfoNameTextInput.highlightColor =
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        timeCardInfoNameTextInput.setTextIsSelectable(false)


        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutTimeCardItemInfo)
            ?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

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
    }
}