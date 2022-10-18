package ar.edu.ort.orionviajes.data

import com.google.gson.annotations.SerializedName

data class CreateTravelDto(
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("budget")
    val budget: Float,
    @field:SerializedName("startDate")
    val startDate: String,
    @field:SerializedName("endDate")
    val endDate: String,
)
