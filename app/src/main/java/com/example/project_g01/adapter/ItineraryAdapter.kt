package com.example.project_g01.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.project_g01.R
import com.example.project_g01.models.Itinerary

class ItineraryAdapter(context: Context, itineraryList: List<Itinerary>)
    : ArrayAdapter<Itinerary> (context, 0, itineraryList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_itinerary, parent, false)

        val itinerary = getItem(position)

        itinerary?.let {
            val tvParkName: TextView = view.findViewById(R.id.tvParkName)
            val tvParkAddress: TextView = view.findViewById(R.id.tvParkAddress)
            val tvTripDate: TextView = view.findViewById(R.id.tvTripDate)
            
            tvParkName.text = it.venue
            tvParkAddress.text = it.address
            tvTripDate.text = "Trip Date: ${it.date}"
        }

        return view
    }
}