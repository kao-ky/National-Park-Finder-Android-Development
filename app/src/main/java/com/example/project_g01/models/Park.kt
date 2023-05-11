package com.example.project_g01.models

data class Park(
    val parkCode:String,
    val url:String,
    val fullName:String,
    val description:String,
    val latitude:String,
    val longitude:String,
    val states:String,
    val addresses:List<AddressesInfo>,
    val images:List<ImagesInfo>
){}

data class AddressesInfo(
    val postalCode:String,
    val city:String,
    val stateCode:String,
    val line1:String,
    val type:String,
    val line3:String,
    val line2:String
){}

data class ImagesInfo(
    val url:String
)