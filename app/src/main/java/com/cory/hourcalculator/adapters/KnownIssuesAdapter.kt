package com.cory.hourcalculator.adapters

import android.animation.LayoutTransition
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R

class KnownIssuesAdapter(val context: Context,
                         private val dataList:  ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title = itemView.findViewById<TextView>(R.id.tvTitle)

        fun bind(position: Int) {

            val dataItem = dataList[position]

            title.text = dataItem["title"]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.known_issues_list_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val layout = holder.itemView.findViewById<LinearLayout>(R.id.linearLayout)
        val layoutTransition = layout.layoutTransition
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        val layout2 = holder.itemView.findViewById<RelativeLayout>(R.id.relativeLayoutUpdate)
        val layoutTransition2 = layout2.layoutTransition
        layoutTransition2.enableTransitionType(LayoutTransition.CHANGING)

        holder.itemView.setOnClickListener {
            if (holder.itemView.findViewById<TextView>(R.id.tvBody).visibility == View.VISIBLE) {
                holder.itemView.findViewById<TextView>(R.id.tvBody).visibility = View.GONE
                holder.itemView.findViewById<ImageView>(R.id.updateChevronImage)
                    .setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            } else {
                holder.itemView.findViewById<TextView>(R.id.tvBody).visibility = View.VISIBLE
                holder.itemView.findViewById<ImageView>(R.id.updateChevronImage)
                    .setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
        }

        (holder as KnownIssuesAdapter.ViewHolder).bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
