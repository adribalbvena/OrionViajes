package ar.edu.ort.orionviajes

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("travels")
    suspend fun getTravels(): Response<GetTravelsResponse> //se agrego suspend, se cambio Call por Response
}