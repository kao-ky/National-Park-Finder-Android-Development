package com.example.project_g01.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.project_g01.R
import com.example.project_g01.constants.*
import com.example.project_g01.databinding.FragmentItineraryDetailsBinding
import com.example.project_g01.models.Itinerary
import com.google.firebase.firestore.FirebaseFirestore

class ItineraryDetailsFragment : Fragment(R.layout.fragment_itinerary_details) {

    private var _binding: FragmentItineraryDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ItineraryDetailsFragmentArgs by navArgs()
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItineraryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itinerary: Itinerary = args.itinerary

        // init page info
        binding.tvParkName.text = itinerary.venue
        binding.tvParkAddress.text = itinerary.address
        binding.etTripDate.setText( itinerary.date )
        binding.etNotes.setText( itinerary.notes )

        // when Save Changes button clicked
        binding.btnSave.setOnClickListener {
            // overwrite existing record upon save changes
            var record = db.collection(COLLECTION_ITINERARY)
                        .document( itinerary.parkCode )

            itinerary.date = binding.etTripDate.text.toString()
            itinerary.notes = binding.etNotes.text.toString()

            record.set(itinerary)

            Toast.makeText(requireContext(), ITINERARY_UPDATED, Toast.LENGTH_SHORT)
                .show()

            val action = ItineraryDetailsFragmentDirections.actionItineraryDetailsFragmentToItineraryFragment()
            findNavController().navigate(action)

            Log.d(TAG_DEBUG, "${this::class.java.name} > $ITINERARY_UPDATED")
        }

        // when Delete Itinerary button clicked
        binding.btnDelete.setOnClickListener {
            db.collection(COLLECTION_ITINERARY)
                .document( itinerary.parkCode )
                .delete()

            Toast.makeText(requireContext(), ITINERARY_DELETED, Toast.LENGTH_SHORT)
                .show()

            val action = ItineraryDetailsFragmentDirections.actionItineraryDetailsFragmentToItineraryFragment()
            findNavController().navigate(action)

            Log.d(TAG_DEBUG, "${this::class.java.name} > $ITINERARY_DELETED")
        }
    }
}