package ar.edu.ort.orionviajes.api

import ar.edu.ort.orionviajes.Constants
import ar.edu.ort.orionviajes.data.ExpensesResponse
import ar.edu.ort.orionviajes.data.GetTravelsResponse
import ar.edu.ort.orionviajes.data.SingleTravelResponse
import ar.edu.ort.orionviajes.data.TravelX
import retrofit2.Call
import retrofit2.http.*

interface TravelsApi {
    //TRAVELS
    @GET(Constants.TRAVELS_ENDPOINT)
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun getTravels(): Call<GetTravelsResponse>

    @POST(Constants.TRAVELS_ENDPOINT)
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun addTravel(@Body travel: TravelX): Call<GetTravelsResponse>

    @GET("${Constants.TRAVELS_ENDPOINT}/{travel_id}")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun getSingleTravel(@Path("travel_id") travel_id: String): Call<SingleTravelResponse>

    @PUT("${Constants.TRAVELS_ENDPOINT}/{travel_id}")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun updateTravel(
        @Path("travel_id") travel_id: String,
        @Body params: TravelX
    ): Call<SingleTravelResponse>

    @DELETE("${Constants.TRAVELS_ENDPOINT}/{travel_id}")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun deleteTravel(@Path("travel_id") travel_id: String): Call<SingleTravelResponse>

    //EXPENSES
    @GET("${Constants.TRAVELS_ENDPOINT}/{travel_id}/expenses")
    fun getExpenses(@Path("travel_id") travel_id: String): Call<ExpensesResponse>
}