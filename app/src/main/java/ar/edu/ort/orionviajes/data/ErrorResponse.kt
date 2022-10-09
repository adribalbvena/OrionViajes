package ar.edu.ort.orionviajes.data

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("success")
    val succes: Boolean
)
