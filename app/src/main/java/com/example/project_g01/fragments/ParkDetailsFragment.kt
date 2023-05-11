package com.example.project_g01.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.project_g01.R
import com.example.project_g01.constants.*
import com.example.project_g01.databinding.FragmentParkDetailsBinding
import com.example.project_g01.models.Itinerary
import com.google.firebase.firestore.FirebaseFirestore

class ParkDetailsFragment : Fragment(R.layout.fragment_park_details) {
    private var _binding: FragmentParkDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ParkDetailsFragmentArgs by navArgs()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding
        _binding = FragmentParkDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvFullName.text=args.selectedPark.parkName
        Glide.with(requireContext()).load(args.selectedPark.parkImage).into(binding.ivImage)
        binding.tvAddress.text=args.selectedPark.parkAddress
        binding.tvUrl.text=args.selectedPark.parkUrl
        binding.tvDescription.text=args.selectedPark.parkDescription

        initAddBtn()

        binding.btnAddToItinerary.setOnClickListener {
            var doc = db.collection(COLLECTION_ITINERARY)
                .document(args.selectedPark.parkCode)
            val itinerary = Itinerary(
                args.selectedPark.parkCode,
                args.selectedPark.parkName,
                args.selectedPark.parkAddress,
                "",
                ""
            )
            doc.set( itinerary )

            Log.d(TAG_DEBUG, "${this::class.java.name} > $ITINERARY_ADDED")

            Toast.makeText(requireContext(), ITINERARY_ADDED, Toast.LENGTH_SHORT)
                .show()

            disableBtn()
        }
    }

    private fun initAddBtn() {
        db.collection(COLLECTION_ITINERARY)
            .document(args.selectedPark.parkCode)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    disableBtn()
                }
                binding.btnAddToItinerary.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                Log.e(TAG_ERROR, ERROR_MSG_DB_FETCH_FAILURE)
            }
    }

    private fun disableBtn() {
        binding.btnAddToItinerary.isEnabled = false
        binding.btnAddToItinerary.text = ITINERARY_ADDED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}