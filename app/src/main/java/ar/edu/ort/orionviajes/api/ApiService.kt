package ar.edu.ort.orionviajes.api

import ar.edu.ort.orionviajes.data.GetTravelsResponse
import ar.edu.ort.orionviajes.data.SingleTravelResponse
import ar.edu.ort.orionviajes.data.TravelX
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("travels")
    @Headers("Accept:application/json","Content-Type:application/json" )
    fun getTravels(): Call<GetTravelsResponse>

    @POST("travels")
    @Headers("Accept:application/json","Content-Type:application/json" )
     fun addTravel(@Body travel: TravelX): Call<GetTravelsResponse>

    @GET("travels/{travel_id}")
    @Headers("Accept:application/json","Content-Type:application/json" )
    fun getSingleTravel(@Path("travel_id") travel_id: String): Call<SingleTravelResponse>

    @PUT("travels/{travel_id}")
    @Headers("Accept:application/json","Content-Type:application/json" )
    fun updateTravel(@Path("travel_id") travel_id: String, @Body params: TravelX): Call<SingleTravelResponse>
}