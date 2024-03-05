package com.cory.hourcalculator.fragments

import android.app.Activity
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.ManagePermissions
import com.cory.hourcalculator.classes.SavedBackupDirectory
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.AnimationData
import com.cory.hourcalculator.sharedprefs.BottomNavBadgeColorData
import com.cory.hourcalculator.sharedprefs.BreakTextBoxVisibilityData
import com.cory.hourcalculator.sharedprefs.CalculateOvertimeInHistoryData
import com.cory.hourcalculator.sharedprefs.CalculationTypeData
import com.cory.hourcalculator.sharedprefs.ChosenAppIconData
import com.cory.hourcalculator.sharedprefs.ClearBreakTextAutomaticallyData
import com.cory.hourcalculator.sharedprefs.ClickableHistoryEntryData
import com.cory.hourcalculator.sharedprefs.ColoredNavBarData
import com.cory.hourcalculator.sharedprefs.ColoredTitleBarTextData
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.cory.hourcalculator.sharedprefs.DaysWorkedPerWeekData
import com.cory.hourcalculator.sharedprefs.DefaultTimeCardNameData
import com.cory.hourcalculator.sharedprefs.DeleteAllOnLimitReachedData
import com.cory.hourcalculator.sharedprefs.DialogData
import com.cory.hourcalculator.sharedprefs.GenerateARandomColorData
import com.cory.hourcalculator.sharedprefs.GenerateARandomColorMethodData
import com.cory.hourcalculator.sharedprefs.HistoryAutomaticDeletionData
import com.cory.hourcalculator.sharedprefs.HistoryFABPositioning
import com.cory.hourcalculator.sharedprefs.HistoryToggleData
import com.cory.hourcalculator.sharedprefs.ItemPositionData
import com.cory.hourcalculator.sharedprefs.LongPressCalculateButtonData
import com.cory.hourcalculator.sharedprefs.MatchImageViewContentsBackgroundData
import com.cory.hourcalculator.sharedprefs.MaterialYouData
import com.cory.hourcalculator.sharedprefs.MaterialYouOptionData
import com.cory.hourcalculator.sharedprefs.MenuTintData
import com.cory.hourcalculator.sharedprefs.MoreColorfulBackgroundData
import com.cory.hourcalculator.sharedprefs.NavigationRailMenuGravityData
import com.cory.hourcalculator.sharedprefs.OutTimeData
import com.cory.hourcalculator.sharedprefs.RandomMaterialYouColorData
import com.cory.hourcalculator.sharedprefs.ShowBreakTimeInDecimalData
import com.cory.hourcalculator.sharedprefs.ShowPatchNotesOnAppLaunchData
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.CornerFamily
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.OutputStream
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BackupRestoreFragment : Fragment() {

    private val iconDisableArray = arrayListOf<String>()
    private var iconEnableID = ""

    var filePath = ""

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        updateCustomColor()

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
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
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
        return inflater.inflate(R.layout.fragment_backup_restore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        val topAppBarBackupRestore = view.findViewById<MaterialToolbar>(R.id.topAppBarBackupRestore)

        topAppBarBackupRestore?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val backupNowCardView = view.findViewById<MaterialCardView>(R.id.backupNowCardView)
        val restoreFromBackupCardView = view.findViewById<MaterialCardView>(R.id.restoreFromBackupCardView)

        backupNowCardView.shapeAppearanceModel = backupNowCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        restoreFromBackupCardView.shapeAppearanceModel = restoreFromBackupCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        updateCustomColor()

        backupNowCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            backupNow()
        }
        restoreFromBackupCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            getRestoreFilePath()
        }
    }

    fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.backupRestoreCoordinator).setBackgroundColor(
            Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val customColorGenerator = CustomColorGenerator(requireContext())

        val backupNowCardView = requireActivity().findViewById<MaterialCardView>(R.id.backupNowCardView)
        val restoreFromBackupCardView = requireActivity().findViewById<MaterialCardView>(R.id.restoreFromBackupCardView)

        backupNowCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        restoreFromBackupCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarBackupRestoreFragment)?.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarBackupRestoreFragment)?.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        val topAppBarBackupRestore = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBarBackupRestore)

        val navigationDrawable = topAppBarBackupRestore?.navigationIcon
        navigationDrawable?.mutate()

        if (Build.VERSION.SDK_INT >= 29) {
            try {
                if (MenuTintData(requireContext()).loadMenuTint()) {
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                        BlendMode.SRC_ATOP
                    )
                } else {
                    val typedValue = TypedValue()
                    activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                    val id = typedValue.resourceId
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
            }
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
        }

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarBackupRestoreFragment)?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarBackupRestoreFragment)?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarBackupRestoreFragment)?.setCollapsedTitleTextColor(
                ContextCompat.getColor(requireContext(), id))
        }
    }

    private fun backupNow() {

        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("file", Context.MODE_PRIVATE)
        val allPrefs = sharedPreferences.all.map { it }

        val path =
            File(
                SavedBackupDirectory(requireContext()).loadBackupDirectory()
            )

        val list = listOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val managePermissions =
            ManagePermissions(
                requireActivity(),
                list,
                123
            )


        if (Build.VERSION.SDK_INT >= 33 || managePermissions.checkPermissions()) {
            val outputStream : OutputStream
            val backupObjects = JSONObject()
            val hoursJSONArray = JSONArray()
            val timeCardsJSONArray = JSONArray()
            val timeCardsItemJSONArray = JSONArray()

            if (!path.exists()) {
                path.mkdirs()
            }

            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm")
            val formatted = current.format(formatter)

            val backupFile = Environment.DIRECTORY_DOWNLOADS+"/Hour Calculator backups/hc_backup_$formatted.txt"

            if (Build.VERSION.SDK_INT < 31) {
                File(backupFile).parentFile!!.mkdirs()
                File(backupFile).createNewFile()
            }

            val backup = JSONObject()

            for (i in 0 until allPrefs.count()) {

                if (allPrefs[i].key != "Link") {
                    backup.put(
                        allPrefs[i].key,
                        allPrefs[i].value
                    )
                }
            }

            val hoursDBHandler =
                DBHelper(requireActivity().applicationContext, null)


            val hoursCursor = hoursDBHandler.getAllRow(requireContext())
            hoursCursor?.moveToFirst()

            while (!hoursCursor!!.isAfterLast) {
                val classes = JSONObject()
                classes.put(
                    "id",
                    hoursCursor.getString(hoursCursor.getColumnIndex(DBHelper.COLUMN_ID))
                )
                classes.put(
                    "inTime",
                    hoursCursor.getString(hoursCursor.getColumnIndex(DBHelper.COLUMN_IN))
                )
                classes.put(
                    "outTime",
                    hoursCursor.getString(hoursCursor.getColumnIndex(DBHelper.COLUMN_OUT))
                )
                classes.put(
                    "totalHours",
                    hoursCursor.getString(hoursCursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                )
                classes.put(
                    "date",
                    hoursCursor.getString(hoursCursor.getColumnIndex(DBHelper.COLUMN_DAY))
                )
                classes.put(
                    "breakTime",
                    hoursCursor.getString(hoursCursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                )

                hoursJSONArray.put(hoursCursor.position, classes)

                hoursCursor.moveToNext()
            }

            val timeCardsDBHandler = TimeCardDBHelper(requireContext(), null)

            val timeCardsCursor = timeCardsDBHandler.getAllRow()
            timeCardsCursor!!.moveToFirst()

            while (!timeCardsCursor.isAfterLast) {
                val timeCards = JSONObject()
                timeCards.put(
                    "id",
                    timeCardsCursor.getString(timeCardsCursor.getColumnIndex(TimeCardDBHelper.COLUMN_ID))
                )
                timeCards.put(
                    "name",
                    timeCardsCursor.getString(timeCardsCursor.getColumnIndex(TimeCardDBHelper.COLUMN_NAME))
                )
                timeCards.put(
                    "totalHours",
                    timeCardsCursor.getString(timeCardsCursor.getColumnIndex(TimeCardDBHelper.COLUMN_TOTAL))
                )
                timeCards.put(
                    "week",
                    timeCardsCursor.getString(timeCardsCursor.getColumnIndex(TimeCardDBHelper.COLUMN_WEEK))
                )

                if (timeCardsCursor.getString(timeCardsCursor.getColumnIndex(TimeCardDBHelper.COLUMN_IMAGE)) != null) {
                    timeCards.put(
                        "image",
                        timeCardsCursor.getString(
                            timeCardsCursor.getColumnIndex(
                                TimeCardDBHelper.COLUMN_IMAGE
                            )
                        )
                    )
                } else {
                    timeCards.put("image", "")
                }
                timeCardsJSONArray.put(timeCardsCursor.position, timeCards)
                timeCardsCursor.moveToNext()
            }

            val timeCardsItemDBHandler = TimeCardsItemDBHelper(requireContext(), null)

            val timeCardsItemCursor = timeCardsItemDBHandler.getAll()
            timeCardsItemCursor!!.moveToFirst()

            while (!timeCardsItemCursor.isAfterLast) {
                val timeCardsItem = JSONObject()
                timeCardsItem.put(
                    "id",
                    timeCardsItemCursor.getString(
                        timeCardsItemCursor.getColumnIndex(
                            TimeCardsItemDBHelper.COLUMN_ID
                        )
                    )
                )
                timeCardsItem.put(
                    "itemId",
                    timeCardsItemCursor.getString(
                        timeCardsItemCursor.getColumnIndex(
                            TimeCardsItemDBHelper.COLUMN_ITEM_ID
                        )
                    )
                )
                timeCardsItem.put(
                    "inTime",
                    timeCardsItemCursor.getString(
                        timeCardsItemCursor.getColumnIndex(
                            TimeCardsItemDBHelper.COLUMN_IN
                        )
                    )
                )
                timeCardsItem.put(
                    "outTime",
                    timeCardsItemCursor.getString(
                        timeCardsItemCursor.getColumnIndex(
                            TimeCardsItemDBHelper.COLUMN_OUT
                        )
                    )
                )
                timeCardsItem.put(
                    "totalHours",
                    timeCardsItemCursor.getString(
                        timeCardsItemCursor.getColumnIndex(
                            TimeCardsItemDBHelper.COLUMN_TOTAL
                        )
                    )
                )
                timeCardsItem.put(
                    "date",
                    timeCardsItemCursor.getString(
                        timeCardsItemCursor.getColumnIndex(
                            TimeCardsItemDBHelper.COLUMN_DAY
                        )
                    )
                )
                timeCardsItem.put(
                    "breakTime",
                    timeCardsItemCursor.getString(
                        timeCardsItemCursor.getColumnIndex(
                            TimeCardsItemDBHelper.COLUMN_BREAK
                        )
                    )
                )
                timeCardsItemJSONArray.put(timeCardsItemCursor.position, timeCardsItem)

                timeCardsItemCursor.moveToNext()
            }

            backupObjects.put("backup", backup)
            backupObjects.put("hours", hoursJSONArray)
            backupObjects.put("timeCards", timeCardsJSONArray)
            backupObjects.put("timeCardsItem", timeCardsItemJSONArray)

            if (Build.VERSION.SDK_INT >= 31) {
                val externalUri =
                    MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

                val relativeLocation = Environment.DIRECTORY_DOWNLOADS + "/Hour Calculator backups"

                val contentValues = ContentValues()
                contentValues.put(
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    "hc_backup_$formatted.txt"
                )
                contentValues.put(
                    MediaStore.Files.FileColumns.MIME_TYPE,
                    "application/text"
                )
                contentValues.put(MediaStore.Files.FileColumns.TITLE, "hc_backup_$formatted")
                contentValues.put(
                    MediaStore.Files.FileColumns.DATE_ADDED,
                    System.currentTimeMillis() / 1000
                )
                contentValues.put(
                    MediaStore.Files.FileColumns.RELATIVE_PATH,
                    relativeLocation
                )
                contentValues.put(
                    MediaStore.Files.FileColumns.DATE_TAKEN,
                    System.currentTimeMillis()
                )
                val fileUri: Uri =
                    requireActivity().contentResolver.insert(externalUri, contentValues)!!
                outputStream = requireActivity().contentResolver.openOutputStream(fileUri)!!
                outputStream.write(backupObjects.toString(2).toByteArray())
                outputStream.close()
            }
            else {
                PrintWriter(FileWriter(backupFile))
                    .use { it.write(backupObjects.toString(2)) }
            }
            Toast.makeText(
                requireContext(),
                "Backup created successfully in your Downloads folder",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            managePermissions.showAlert(requireContext())
        }
    }

    fun restoreBackup(restoreFilePath: Uri) {

        try {

            val dialog = BottomSheetDialog(requireContext())
            val restoreLayout = layoutInflater.inflate(R.layout.restore_bottom_sheet, null)
            dialog.setContentView(restoreLayout)

            if (resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }
            val bodyCardView =
                restoreLayout.findViewById<MaterialCardView>(R.id.bodyCardView)
            val yesButton = restoreLayout.findViewById<Button>(R.id.yesOverwriteButton)
            val noButton = restoreLayout.findViewById<Button>(R.id.cancelOverwriteButton)

            bodyCardView.setCardBackgroundColor(
                Color.parseColor(
                    CustomColorGenerator(requireContext()).generateCardColor()
                )
            )
            yesButton.setBackgroundColor(
                Color.parseColor(
                    CustomColorGenerator(
                        requireContext()
                    ).generateCustomColorPrimary()
                )
            )
            noButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

            yesButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                /*val path =
                File(
                    restoreFilePath
                ) as File*/

                //if (path.exists()) {
                // val input = FileInputStream(restoreFilePath)
                val input = requireContext().contentResolver.openInputStream(restoreFilePath)!!
                val size = input.available()
                val buffer = ByteArray(size)
                input.read(buffer)
                input.close()
                val json = String(buffer)

                val jsonContact = JSONObject(json)
                val jsonArrayBackup: JSONObject = jsonContact.getJSONObject("backup")

                val keys : Iterator<String> = jsonArrayBackup.keys()
                val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("file", Context.MODE_PRIVATE)
                requireContext().getSharedPreferences("file", 0).edit().clear().apply()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val editor = sharedPreferences.edit()

                    if (jsonArrayBackup.get(key) is Boolean) {
                        editor.putBoolean(key, jsonArrayBackup.get(key).toString().toBoolean())
                    }
                    else if (jsonArrayBackup.get(key) is String) {
                        editor.putString(key, jsonArrayBackup.get(key).toString())
                    }
                    else if (jsonArrayBackup.get(key) is Int) {
                        editor.putInt(key, jsonArrayBackup.get(key).toString().toInt())
                    }

                    editor.apply()

                }
                val jsonArrayClasses: JSONArray = jsonContact.getJSONArray("hours")

                val jsonArrayHoursSize = jsonArrayClasses.length()
                DBHelper(
                    requireContext(),
                    null
                ).deleteAll()
                for (i in 0 until jsonArrayHoursSize) {
                    val jsonObjectDetail: JSONObject = jsonArrayClasses.getJSONObject(i)

                    DBHelper(
                        requireContext(),
                        null
                    ).insertRow(
                        jsonObjectDetail.get("id").toString(),
                        jsonObjectDetail.get("inTime").toString(),
                        jsonObjectDetail.get("outTime").toString(),
                        jsonObjectDetail.get("totalHours").toString(),
                        jsonObjectDetail.get("date").toString().toLong(),
                        jsonObjectDetail.get("breakTime").toString(),
                    )

                }

                val jsonArrayTimeCards: JSONArray = jsonContact.getJSONArray("timeCards")

                val jsonArrayTimeCardsSize = jsonArrayTimeCards.length()
                TimeCardDBHelper(requireContext(), null).deleteAll()
                for (i in 0 until jsonArrayTimeCardsSize) {
                    val jsonObjectDetail: JSONObject = jsonArrayTimeCards.getJSONObject(i)

                    TimeCardDBHelper(requireContext(), null).insertRestoreRow(
                        jsonObjectDetail.get("id").toString(),
                        jsonObjectDetail.get("name").toString(),
                        jsonObjectDetail.get("week").toString(),
                        jsonObjectDetail.get("totalHours").toString(),
                        jsonObjectDetail.get("image").toString()
                    )
                }

                val jsonArrayGrades: JSONArray = jsonContact.getJSONArray("timeCardsItem")

                val jsonArrayGradesSize = jsonArrayGrades.length()
                TimeCardsItemDBHelper(requireContext(), null).deleteAll()
                for (i in 0 until jsonArrayGradesSize) {
                    val jsonObjectDetail: JSONObject = jsonArrayGrades.getJSONObject(i)

                    TimeCardsItemDBHelper(requireContext(), null).insertRestoreRow(
                        jsonObjectDetail.get("id").toString(),
                        jsonObjectDetail.get("itemId").toString(),
                        jsonObjectDetail.get("inTime").toString(),
                        jsonObjectDetail.get("outTime").toString(),
                        jsonObjectDetail.get("totalHours").toString(),
                        jsonObjectDetail.get("date").toString().toLong(),
                        jsonObjectDetail.get("breakTime").toString(),
                    )
                }
                iconDisableArray.clear()
                if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == requireContext().getString(R.string.teal)) {
                    iconEnableID = "com.cory.hourcalculator.SplashScreenNoIcon"
                    iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                    iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                    iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                    iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                    iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                    changeIcons()
                }
                else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == requireContext().getString(R.string.pink)) {
                    iconEnableID = "com.cory.hourcalculator.SplashPink"
                    iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                    iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                    iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                    iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                    iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                    changeIcons()
                }
                else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == requireContext().getString(R.string.orange)) {
                    iconEnableID = "com.cory.hourcalculator.SplashOrange"
                    iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                    iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                    iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                    iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                    iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                    iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                    changeIcons()
                }
                else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == requireContext().getString(R.string.red)) {
                    iconEnableID = "com.cory.hourcalculator.SplashRed"
                    iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                    iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                    iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                    iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                    iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                    changeIcons()
                }
                else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == requireContext().getString(R.string.blue)) {
                    iconEnableID = "com.cory.hourcalculator.SplashBlue"
                    iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                    iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                    iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                    iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                    iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                    changeIcons()
                }
                else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == requireContext().getString(R.string.og)) {
                    iconEnableID = "com.cory.hourcalculator.SplashOG"
                    iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                    iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                    iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                    iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                    iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                    iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                    changeIcons()
                }
                else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == requireContext().getString(R.string.snow_falling)) {
                    iconEnableID = "com.cory.hourcalculator.SplashSnowFalling"
                    iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                    iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                    iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                    iconDisableArray.add("com.cory.hourcalculator.MaterialYou")
                    iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                    changeIcons()
                }
                else if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == requireContext().getString(R.string.material_you)) {
                    iconEnableID = "com.cory.hourcalculator.MaterialYou"
                    iconDisableArray.add("com.cory.hourcalculator.SplashScreenNoIcon")
                    iconDisableArray.add("com.cory.hourcalculator.SplashPink")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOrange")
                    iconDisableArray.add("com.cory.hourcalculator.SplashRed")
                    iconDisableArray.add("com.cory.hourcalculator.SplashBlue")
                    iconDisableArray.add("com.cory.hourcalculator.SplashOG")
                    iconDisableArray.add("com.cory.hourcalculator.SplashSnowFalling")
                    changeIcons()
                }
                Toast.makeText(requireContext(), "Restore Successful", Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent =
                        requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
                    intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    activity?.finish()
                }, 1500)
            }
            noButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                dialog.dismiss()
                Toast.makeText(requireContext(), "Not restored", Toast.LENGTH_SHORT).show()
            }
            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(),
                "There was some error while restoring backup",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getRestoreFilePath() {
        val intent = Intent()
            .setType("text/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        getFilePathResult.launch(intent)
    }

    private var launcherForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data.also { uri -> filePath = uri.toString() }
                val uri: Uri = result.data!!.data!!

                val (_, needed) = uri.path!!.split(":")
                val directoryPath = "/storage/emulated/0/$needed"
                SavedBackupDirectory(requireContext()).setBackupDirectory(directoryPath)
            }
        }

    private var getFilePathResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data.also { uri -> filePath = uri.toString() }
                val uri: Uri = result.data!!.data!!

                try {
                    // val (_, needed) = uri.path!!.split(":")
                    //val directoryPath = "/storage/emulated/0/$needed"
                    restoreBackup(uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        "There was an error loading file",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private fun changeIcons() {
        for (i in 0 until iconDisableArray.count()) {
            requireContext().packageManager?.setComponentEnabledSetting(
                ComponentName(
                    BuildConfig.APPLICATION_ID,
                    iconDisableArray.elementAt(i)
                ),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
        requireContext().packageManager?.setComponentEnabledSetting(
            ComponentName(
                BuildConfig.APPLICATION_ID,
                iconEnableID
            ),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun getFilePath() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {

            addCategory(Intent.CATEGORY_DEFAULT)
        }
        launcherForResult.launch(intent)
    }
}