package com.cory.hourcalculator.adapters

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.fragments.SavedColorsFragment
import com.cory.hourcalculator.intents.MainActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import java.util.ArrayList
import java.util.HashMap

class SavedColorsAdapter(private val context: Context,
private val dataList: MutableSet<String>, fragment: SavedColorsFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val newFragment = fragment

    lateinit var savedColorCardView: MaterialCardView

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title = itemView.findViewById<TextView>(R.id.savedColorTitle)!!
        var previewCardView = itemView.findViewById<MaterialCardView>(R.id.savedColorPreviewCardView)!!

        fun bind(position: Int) {
            savedColorCardView = itemView.findViewById(R.id.savedColorItemCardView)

            title.text = "#${dataList.elementAt(position).toString()}"

            savedColorCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))
            previewCardView.setCardBackgroundColor(Color.parseColor(title.text.toString()))

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

            if (!MaterialYouEnabled(context).loadMaterialYou()) {
                if (CustomColorGenerator(context).loadCustomHex() == title.text) {
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

        holder.itemView.findViewById<MaterialCardView>(R.id.savedColorItemCardView).setOnClickListener {
            Vibrate().vibration(context)
            CustomColorGenerator(context).setCustomHex(holder.itemView.findViewById<TextView>(R.id.savedColorTitle).text.toString())
            holder.itemView.findViewById<MaterialCardView>(R.id.savedColorItemCardView).strokeWidth = 7
            notifyDataSetChanged()

            GenerateARandomColorData(context).setGenerateARandomColorOnAppLaunch(false)
            MaterialYouEnabled(context).setMaterialYouState(false)
            newFragment.updateCustomColor()
        }

        savedColorCardView.setOnLongClickListener {
            Vibrate().vibration(context)
            val allColors = mutableSetOf<String>()
            val colorsSet = UserAddedColors(context).loadColors()
            for (i in 0 until UserAddedColors(context).loadColors()!!.count()) {
                allColors.add(colorsSet!!.elementAt(i))
            }
            allColors.remove(holder.itemView.findViewById<TextView>(R.id.savedColorTitle).text.toString().drop(1))
            UserAddedColors(context).clear()
            UserAddedColors(context).addColor(allColors)
            dataList.clear()
            for (i in 0 until UserAddedColors(context).loadColors()!!.count()) {
                dataList.add(UserAddedColors(context).loadColors()!!.elementAt(i))
            }
            notifyItemRemoved(holder.adapterPosition)
            Toast.makeText(context, "Color Deleted", Toast.LENGTH_SHORT).show()

            if (dataList.isEmpty()) {
                newFragment.itemCountZero()
            }
            else {
                for (i in 0 until dataList.count()) {
                    if (dataList.count() == 1) {
                        Toast.makeText(context, "1", Toast.LENGTH_SHORT).show()
                        savedColorCardView.shapeAppearanceModel = savedColorCardView.shapeAppearanceModel
                            .toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                            .setBottomRightCornerSize(28f)
                            .setBottomLeftCornerSize(28f)
                            .build()
                    } else if (dataList.count() > 1) {
                        Toast.makeText(context, "2", Toast.LENGTH_SHORT).show()
                        if (i == 0) {
                            savedColorCardView.shapeAppearanceModel = savedColorCardView.shapeAppearanceModel
                                .toBuilder()
                                .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                                .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                                .setBottomRightCornerSize(0f)
                                .setBottomLeftCornerSize(0f)
                                .build()
                        } else if (i > 0 && i < dataList.count() - 1) {
                            savedColorCardView.shapeAppearanceModel = savedColorCardView.shapeAppearanceModel
                                .toBuilder()
                                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                .setBottomRightCornerSize(0f)
                                .setBottomLeftCornerSize(0f)
                                .build()
                        } else if (i == dataList.count() - 1) {
                            savedColorCardView.shapeAppearanceModel = savedColorCardView.shapeAppearanceModel
                                .toBuilder()
                                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                .setBottomRightCornerSize(28f)
                                .setBottomLeftCornerSize(28f)
                                .build()
                        }
                    }
                }
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