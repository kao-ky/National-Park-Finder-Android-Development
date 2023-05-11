package com.example.project_g01.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.project_g01.R
import com.example.project_g01.adapter.ItineraryAdapter
import com.example.project_g01.constants.*
import com.example.project_g01.databinding.FragmentItineraryBinding
import com.example.project_g01.models.Itinerary
import com.google.firebase.firestore.FirebaseFirestore

class ItineraryFragment : Fragment(R.layout.fragment_itinerary) {

    private var _binding: FragmentItineraryBinding? = null
    private val binding get() = _binding!!
    private val itineraryList = mutableListOf<Itinerary>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentItineraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_ITINERARY)
            .get()
            .addOnSuccessListener { docs ->
                // set up list of itineraries
                for (doc in docs) {
                    itineraryList.add( doc.toObject(Itinerary::class.java) )
                }

                val adapter = ItineraryAdapter(requireContext(), itineraryList)
                binding.lvItineraries.adapter = adapter

                Log.d(TAG_DEBUG, "${this::class.java.name} > $ITINERARY_LIST_LOADED")
            }
            .addOnFailureListener {
                Log.e(TAG_ERROR, "${this::class.java.name} > $ERROR_MSG_DB_FETCH_FAILURE")
            }

        // switch screen on clicks
        binding.lvItineraries.setOnItemClickListener { adapterView, view, position, id ->
            val itinerary = itineraryList[position]
            val action = ItineraryFragmentDirections.actionItineraryFragmentToItineraryDetailsFragment(itinerary)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}