package ar.edu.ort.orionviajes

import com.google.gson.annotations.SerializedName

data class TravelX(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("budget")
    val budget: Float,
    @field:SerializedName("startDate")
    val startDate: String,
    @field:SerializedName("endDate")
    val endDate: String,
    //val expenses: List<Any>,

)