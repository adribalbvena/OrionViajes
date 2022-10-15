package ar.edu.ort.orionviajes.data

data class Expense(
    val id: String,
    val title: String,
    val currency: String,
    val amount: Float,
    val category: String,
    val paymentMethod: String,
    val date: String,
)
