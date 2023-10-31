package com.masar.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.libraries.places.api.model.AutocompletePrediction
class AutocompleteAdapter(context: Context) :
    ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line) {

    private val placeNames: MutableList<String> = mutableListOf()

    fun updatePredictions(newPlaceNames: List<String>) {
        placeNames.clear()
        placeNames.addAll(newPlaceNames)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return placeNames.size
    }

    override fun getItem(position: Int): String? {
        return if (position < placeNames.size) placeNames[position] else null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_dropdown_item_1line, parent, false)

        val placeNameTextView = view.findViewById<TextView>(android.R.id.text1)
        val placeName = getItem(position)

        placeNameTextView.text = placeName ?: ""

        return view
    }
}
