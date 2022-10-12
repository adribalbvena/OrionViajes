package ar.edu.ort.orionviajes.api

import ar.edu.ort.orionviajes.data.GetTravelsResponse
import ar.edu.ort.orionviajes.data.TravelX
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @GET("travels")
    suspend fun getTravels(): Response<GetTravelsResponse> //se agrego suspend, se cambio Call por Response

    @POST("travels")
    @Headers("Accept:application/json","Content-Type:application/json" )
    suspend fun addTravel(@Body travel: TravelX): Response<GetTravelsResponse>
}