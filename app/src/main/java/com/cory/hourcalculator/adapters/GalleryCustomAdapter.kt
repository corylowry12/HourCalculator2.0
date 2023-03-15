package com.cory.hourcalculator.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.intents.ImageViewActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.shape.CornerFamily
import java.math.RoundingMode

class GalleryCustomAdapter( private val context: Context,
    private val dataList: ArrayList<HashMap<String, String>>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var galleryImage: ImageView = itemView.findViewById(R.id.galleryImage)
        var nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        fun bind(position: Int) {

            Glide.with(context)
                .load(dataList[position]["image"])
                .into(galleryImage)

            if (dataList[position]["name"] == null || dataList[position]["name"] == "") {
                nameTextView.text = "Unknown"
            }
            else {
                nameTextView.text = dataList[position]["name"]
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.gallery_list_row, parent, false)
        )
    }
    override fun getItemCount(): Int {
        return dataList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.findViewById<MaterialCardView>(R.id.galleryCard).setOnClickListener {
            Vibrate().vibration(context)
            val intent = Intent(context, ImageViewActivity::class.java)
            intent.putExtra("id", dataList[position]["id"])
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                (context as AppCompatActivity),
                holder.itemView.findViewById(R.id.galleryImage),
                "transition_image"
            )
            context.startActivity(intent, options.toBundle())
        }
        (holder as GalleryCustomAdapter.ViewHolder).bind(position)
    }
}