package ar.edu.ort.orionviajes.data

import com.google.gson.annotations.SerializedName

data class Expense(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("currency")
    val currency: String,
    @field:SerializedName("amount")
    val amount: Float,
    @field:SerializedName("category")
    val category: String,
    @field:SerializedName("paymentMethod")
    val paymentMethod: String,
    @field:SerializedName("date")
    val date: String,
)
