package ar.edu.ort.orionviajes

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("travels")
    fun getTravels(): Call<GetTravelsResponse> //falta importar cuando este la api
}