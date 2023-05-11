package com.example.project_g01.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project_g01.R
import com.example.project_g01.constants.*
import com.example.project_g01.databinding.FragmentFindParksBinding
import com.example.project_g01.models.AllPark
import com.example.project_g01.models.ShowingPark
import com.example.project_g01.networking.ApiService
import com.example.project_g01.networking.RetrofitInstance
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import retrofit2.Response


class FindParksFragment : Fragment(R.layout.fragment_find_parks), OnMapReadyCallback {
    //binding variables
    private var _binding: FragmentFindParksBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap

    private var firestore = FirebaseFirestore.getInstance()
    private val statesCollectionRef = firestore.collection(COLLECTION_STATES)

    var showingParkList: MutableList<ShowingPark> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding
        _binding = FragmentFindParksBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(binding.fragmentMap.id) as? SupportMapFragment
        mapFragment?.getMapAsync {
            Log.d(TAG_DEBUG, "${this::class.java.name} > $MAP_FRAGMENT_FOUND")
        }
        if (mapFragment == null) {
            Log.d(TAG_DEBUG, "${this::class.java.name} > $MAP_FRAGMENT_NULL")
        } else {
            Log.d(TAG_DEBUG, "${this::class.java.name} > $MAP_FRAGMENT_NOT_NULL")
            mapFragment?.getMapAsync(this)
        }

        binding.btnConfirm.setOnClickListener {
            val selectedStateFromUI = binding.spState.selectedItem.toString()
            mMap.clear()
            findDocument(selectedStateFromUI)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        mMap.isTrafficEnabled = true
        val uiSettings = googleMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isCompassEnabled = true
        val intialLocation = LatLng(45.8877, -101.1205)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(intialLocation, 3.0f))
    }


    private fun findDocument(selectedStateFromUI: String) {
        val stateDocumentRef = statesCollectionRef.document(selectedStateFromUI)
        stateDocumentRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val foundStateAbbreviation = document.get("abbreviation").toString()
                lifecycleScope.launch {
                    var responseFromAPI: AllPark? = getAllParkFromAPI(foundStateAbbreviation)
                    if (responseFromAPI == null) {
                        Log.e(TAG_ERROR, "${this::class.java.name} > $MAP_API_ERROR")
                        return@launch
                    }
                    Log.d(TAG_DEBUG, "${this::class.java.name} > $MAP_API_SUCCESS")
                    Log.d(TAG_DEBUG, "${this::class.java.name} > fullName=${responseFromAPI.data[1].fullName}")


                    for (currPark in responseFromAPI.data) {
                        val currParkLatLng =
                            LatLng(currPark.latitude.toDouble(), currPark.longitude.toDouble())
                        mMap.addMarker(
                            MarkerOptions().position(currParkLatLng)
                                .title(currPark.fullName)
                                .snippet(currPark.addresses[0].line1)
                        )
                        val instance = ShowingPark(
                            currPark.parkCode,
                            currPark.fullName,
                            currPark.images[0].url,
                            currPark.addresses[0].line1,
                            currPark.url,
                            currPark.description
                        )
                        showingParkList.add(instance)
                    }
                    val firstParkLatLng = LatLng(
                        responseFromAPI.data[0].latitude.toDouble(),
                        responseFromAPI.data[0].longitude.toDouble()
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstParkLatLng, 5.0f))
                    mMap.setOnInfoWindowClickListener { marker ->
                        val selectedParkId = marker.id.substring(1).toInt()
                        val action =
                            FindParksFragmentDirections.actionFindParksFragmentToParkDetailsFragment(
                                showingParkList[selectedParkId]
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun getAllParkFromAPI(foundStateAbbreviation: String): AllPark? {
        val apiKey = API_KEY
        var apiService: ApiService = RetrofitInstance.retrofitService
        Log.d(TAG_DEBUG, "${this::class.java.name} > foundStateAbbreviation=$foundStateAbbreviation")
        val response: Response<AllPark> = apiService.getAllPark(foundStateAbbreviation, apiKey)
        if (response.isSuccessful) {
            val dataFromAPI = response.body()   /// myresponseobject
            if (dataFromAPI == null) {
                Log.e(TAG_ERROR, API_NO_DATA_ERROR)
                return null
            }
            Log.d(TAG_DEBUG, "${this::class.java.name} > data=$dataFromAPI")
            return dataFromAPI
        } else {
            Log.d(TAG_ERROR, "${this::class.java.name} > $ERROR_MSG_API_FAILURE")
            return null
        }
    }
}
