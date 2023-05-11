package com.example.project_g01.models

import java.io.Serializable

data class Itinerary (var parkCode: String = "",
                      var venue: String = "",
                      val address: String = "",
                      var date: String = "",
                      var notes: String = ""): Serializable