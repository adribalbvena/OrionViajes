package ar.edu.ort.orionviajes

object Constants {
    //Endpoints
    const val BASE_URL = "http://46.17.108.34:3001/"
    const val USERS_ENDPOINT = "users/"
    const val LOGIN_CONTROLLER = "${USERS_ENDPOINT}login/"
    const val REGISTER_CONTROLLER = "${USERS_ENDPOINT}register/"
    const val TRAVELS_ENDPOINT = "orion/travels"


    val CATEGORIES = listOf<String>("Comida", "Bebida", "Transporte", "Alojamiento", "Entretenimiento", "Otros")
    val PAYMENT_METHOD = listOf<String>("Tarjeta", "Efectivo")
    val CURRENCIES = listOf<String>("ARS", "EUR", "USD", "CHF")

}