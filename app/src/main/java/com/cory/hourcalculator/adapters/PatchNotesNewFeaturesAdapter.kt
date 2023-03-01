package com.cory.hourcalculator.adapters

import android.animation.LayoutTransition
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily

class PatchNotesNewFeaturesAdapter(
    val context: Context,
    private val dataList: Array<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title = itemView.findViewById<TextView>(R.id.tvTitle)!!

        fun bind(position: Int) {

            title.text = dataList.elementAt(position).toString()

            val patchNotesItemCardView = itemView.findViewById<MaterialCardView>(R.id.patchNotesItemCardView)

            if (dataList.count() == 1) {
                patchNotesItemCardView.shapeAppearanceModel = patchNotesItemCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                    .setBottomRightCornerSize(28f)
                    .setBottomLeftCornerSize(28f)
                    .build()
            }
            else if (dataList.count() > 1) {
                if (position == 0) {
                    patchNotesItemCardView.shapeAppearanceModel = patchNotesItemCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                        .setBottomRightCornerSize(0f)
                        .setBottomLeftCornerSize(0f)
                        .build()
                }
                else if (position > 0 && position < dataList.count() - 1) {
                    patchNotesItemCardView.shapeAppearanceModel = patchNotesItemCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                        .setBottomRightCornerSize(0f)
                        .setBottomLeftCornerSize(0f)
                        .build()
                }
                else if (position == dataList.count() - 1) {
                    patchNotesItemCardView.shapeAppearanceModel = patchNotesItemCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                        .setBottomRightCornerSize(28f)
                        .setBottomLeftCornerSize(28f)
                        .build()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.patch_notes_list_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val layout2 = holder.itemView.findViewById<RelativeLayout>(R.id.relativeLayoutPatchNotes)
        val layoutTransition2 = layout2.layoutTransition
        layoutTransition2.enableTransitionType(LayoutTransition.CHANGING)

        (holder as PatchNotesNewFeaturesAdapter.ViewHolder).bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}