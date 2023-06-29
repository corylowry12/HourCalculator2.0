package com.cory.hourcalculator.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.fragments.SavedColorsFragment
import com.cory.hourcalculator.sharedprefs.GenerateARandomColorData
import com.cory.hourcalculator.sharedprefs.GenerateARandomColorMethodData
import com.cory.hourcalculator.sharedprefs.MaterialYouData
import com.cory.hourcalculator.sharedprefs.UserAddedColorsData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputEditText

class SavedColorsAdapter(private val context: Context,
private val dataList: ArrayList<HashMap<String, String>>, fragment: SavedColorsFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val newFragment = fragment

    lateinit var savedColorCardView: MaterialCardView

    fun updateCardColor() {
        notifyDataSetChanged()
    }

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title = itemView.findViewById<TextView>(R.id.savedColorTitle)!!
        var previewCardView = itemView.findViewById<MaterialCardView>(R.id.savedColorPreviewCardView)!!

        fun bind(position: Int) {
            savedColorCardView = itemView.findViewById(R.id.savedColorItemCardView)

            val imageViewRename = itemView.findViewById<ImageButton>(R.id.imageViewRename)
            val imageViewRenameCardView = itemView.findViewById<MaterialCardView>(R.id.imageViewRenameCardView)

            imageViewRenameCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateTopAppBarColor()))

            val color = Color.parseColor(CustomColorGenerator(context).generateMenuTintColor())
            imageViewRename.setColorFilter(color)

            if (dataList[position]["name"] == "") {
                title.text = "#${dataList[position]["hex"]}"
            }
            else {
                title.text = dataList[position]["name"]
            }
            savedColorCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))
            previewCardView.setCardBackgroundColor(Color.parseColor("#${dataList[position]["hex"]}"))

            if (dataList.count() == 1) {
                savedColorCardView.shapeAppearanceModel = savedColorCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                    .setBottomRightCornerSize(28f)
                    .setBottomLeftCornerSize(28f)
                    .build()
            } else if (dataList.count() > 1) {
                if (position == 0) {
                    savedColorCardView.shapeAppearanceModel = savedColorCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                        .setBottomRightCornerSize(0f)
                        .setBottomLeftCornerSize(0f)
                        .build()
                } else if (position > 0 && position < dataList.count() - 1) {
                    savedColorCardView.shapeAppearanceModel = savedColorCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                        .setBottomRightCornerSize(0f)
                        .setBottomLeftCornerSize(0f)
                        .build()
                } else if (position == dataList.count() - 1) {
                    savedColorCardView.shapeAppearanceModel = savedColorCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                        .setBottomRightCornerSize(28f)
                        .setBottomLeftCornerSize(28f)
                        .build()
                }
            }

            if (!MaterialYouData(context).loadMaterialYou()) {
                if (CustomColorGenerator(context).loadCustomHex() == "#${dataList[position]["hex"]}") {
                    savedColorCardView.strokeWidth = 7
                } else {
                    savedColorCardView.strokeWidth = 0
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.savedcolorlistrow, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        savedColorCardView = holder.itemView.findViewById(R.id.savedColorItemCardView)

        holder.itemView.findViewById<MaterialCardView>(R.id.imageViewRenameCardView).setOnClickListener {
            Vibrate().vibration(context)
            val dialog = BottomSheetDialog(context)
            val renameLayout = LayoutInflater.from(context)
                .inflate(R.layout.rename_bottom_sheet, null)
            dialog.setContentView(renameLayout)
            dialog.setCancelable(true)

            if (context.resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }

            val editText = renameLayout.findViewById<TextInputEditText>(R.id.renameTextInputEditText)
            val renameButton = renameLayout.findViewById<Button>(R.id.renameButton)
            val cancelButton = renameLayout.findViewById<Button>(R.id.cancelButton)
            val renameCardView = renameLayout.findViewById<MaterialCardView>(R.id.renameCardView)

            editText.textCursorDrawable = null

            renameCardView.setCardBackgroundColor(
                Color.parseColor(
                    CustomColorGenerator(context).generateCardColor()
                )
            )
            renameButton.setBackgroundColor(
                Color.parseColor(
                    CustomColorGenerator(
                        context
                    ).generateCustomColorPrimary()
                )
            )
            cancelButton.setTextColor(Color.parseColor(CustomColorGenerator(context).generateCustomColorPrimary()))

            editText.setText(dataList[holder.adapterPosition]["name"].toString().trim())

            renameButton.setOnClickListener {
                Vibrate().vibration(context)
                dialog.dismiss()
                val addedColors = UserAddedColorsData(context).read()
                dataList.clear()

                for (i in 0 until UserAddedColorsData(context).read().count()) {
                    val allColors = HashMap<String, String>()
                    if (i == holder.adapterPosition) {
                        try {
                            allColors["name"] = editText.text.toString().trim()
                        } catch (e: java.lang.Exception) {
                            allColors["name"] = ""
                        }
                    }
                    else {

                        try {
                            allColors["name"] = addedColors[i]["name"].toString().trim()
                        } catch (e: java.lang.Exception) {
                            allColors["name"] = ""
                        }

                    }
                    allColors["hex"] = addedColors[i]["hex"].toString().trim()
                    dataList.add(allColors)
                }
                UserAddedColorsData(context).clearHash()
                UserAddedColorsData(context).insert(dataList)
                notifyDataSetChanged()
            }
            cancelButton.setOnClickListener {
                Vibrate().vibration(context)
                dialog.dismiss()
            }
            dialog.show()
        }

        holder.itemView.findViewById<MaterialCardView>(R.id.savedColorItemCardView).setOnClickListener {
            Vibrate().vibration(context)
            CustomColorGenerator(context).setCustomHex("#${dataList[holder.adapterPosition]["hex"].toString()}")
            holder.itemView.findViewById<MaterialCardView>(R.id.savedColorItemCardView).strokeWidth = 7
            notifyDataSetChanged()

            GenerateARandomColorData(context).setGenerateARandomColorOnAppLaunch(false)
            MaterialYouData(context).setMaterialYouState(false)
            newFragment.updateCustomColor()
        }

        savedColorCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(context)

            if (GenerateARandomColorMethodData(context).loadGenerateARandomColorMethod() == 1) {
                if (CustomColorGenerator(context).loadRandomHex() == "#${dataList[holder.adapterPosition]["hex"]}") {
                    CustomColorGenerator(context).generateARandomColor()
                    newFragment.updateCustomColor()
                }
            }

            val addedColors = UserAddedColorsData(context).read()
            dataList.clear()

            for (i in 0 until UserAddedColorsData(context).read().count()) {
                val allColors = HashMap<String, String>()
                try {
                    allColors["name"] = addedColors[i]["name"].toString()
                } catch (e: java.lang.Exception) {
                    allColors["name"] = ""
                }
                allColors["hex"] = addedColors[i]["hex"].toString()
                dataList.add(allColors)
            }
            dataList.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            UserAddedColorsData(context).insert(dataList)
            Toast.makeText(context, "Color Deleted", Toast.LENGTH_SHORT).show()

            if (dataList.count() < 2 && GenerateARandomColorMethodData(context).loadGenerateARandomColorMethod() == 1) {
                Toast.makeText(context, "Generating a random color from saved colors is now disabled", Toast.LENGTH_SHORT).show()
                GenerateARandomColorMethodData(context).setGenerateARandomColorMethod(0)
            }

            if (dataList.isEmpty()) {
                newFragment.itemCountZero()
            }
            else {
                notifyItemRangeChanged(0, dataList.count())
            }
            return@setOnLongClickListener true
        }
        (holder as SavedColorsAdapter.ViewHolder).bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}