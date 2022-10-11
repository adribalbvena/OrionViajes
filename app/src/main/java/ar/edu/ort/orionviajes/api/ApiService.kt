package ar.edu.ort.orionviajes.api

import ar.edu.ort.orionviajes.data.GetTravelsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("travels")
    suspend fun getTravels(): Response<GetTravelsResponse> //se agrego suspend, se cambio Call por Response
    
}