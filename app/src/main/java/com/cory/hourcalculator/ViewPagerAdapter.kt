package com.cory.hourcalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter

/*class ViewPagerAdapter(val context: Context) : PagerAdapter() {

    var layoutInflater : LayoutInflater? = null

    val layouts: Array<Int> = arrayOf(R.layout.view_pager_layout, R.layout.view_pager_layout_2,
        R.layout.view_pager_layout)

    override fun getCount(): Int {
        return layouts.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any =
        LayoutInflater.from(container.context).inflate(layouts[position], container, false).also {
            container.addView(it)
        }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}*/