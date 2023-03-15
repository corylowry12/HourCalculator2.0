package com.cory.hourcalculator.fragments

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.adapters.GalleryCustomAdapter
import com.cory.hourcalculator.adapters.TimeCardCustomAdapter
import com.cory.hourcalculator.classes.ColoredTitleBarTextData
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.MenuTintData
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar

class GalleryFragment : Fragment() {

    private val dataList = ArrayList<HashMap<String, String>>()
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var galleryCustomAdapter: GalleryCustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateCustomColor()

        galleryCustomAdapter = GalleryCustomAdapter(requireContext(), dataList)
        gridLayoutManager = GridLayoutManager(requireContext(), 2)

        loadIntoList()
    }

    private fun loadIntoList() {

        val dbHandler = TimeCardDBHelper(requireActivity().applicationContext, null)

        dataList.clear()
        val cursor = dbHandler.getAllImages(requireContext())
        cursor.moveToFirst()

        val noEntriesStoredTextView =
            view?.findViewById<TextView>(R.id.noImagesStoredGallery)

        if (cursor.count > 0) {
            noEntriesStoredTextView?.visibility = View.GONE
        } else {
            noEntriesStoredTextView?.visibility = View.VISIBLE
        }


        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_ID))
            map["image"] =
                cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_IMAGE))
            map["name"] =
                cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_NAME))

            dataList.add(map)

            cursor.moveToNext()
        }


        val galleryRecyclerView = activity?.findViewById<RecyclerView>(R.id.galleryRecyclerView)
        galleryRecyclerView?.layoutManager = gridLayoutManager
        galleryRecyclerView?.adapter = galleryCustomAdapter

    }

    override fun onResume() {
        super.onResume()
        loadIntoList()
    }

    fun updateCustomColor() {
        val collapsingToolbarLayout = requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarGallery)
        collapsingToolbarLayout.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        val topAppBarGallery =
            requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarGallery)

        val navigationDrawable = topAppBarGallery?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
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

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarGallery)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarGallery)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }
    }
}