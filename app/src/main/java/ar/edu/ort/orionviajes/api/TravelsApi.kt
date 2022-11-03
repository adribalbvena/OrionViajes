package ar.edu.ort.orionviajes.api

import ar.edu.ort.orionviajes.Constants
import ar.edu.ort.orionviajes.data.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface TravelsApi {
    //TRAVELS
    @GET(Constants.TRAVELS_ENDPOINT)
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun getTravels(): Call<GetTravelsResponse>

    @POST(Constants.TRAVELS_ENDPOINT)
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun addTravel(@Body travel: CreateTravelDto): Call<SingleTravelResponse>

    @GET("${Constants.TRAVELS_ENDPOINT}/{travel_id}")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun getSingleTravel(@Path("travel_id") travel_id: String): Call<SingleTravelResponse>

    @PUT("${Constants.TRAVELS_ENDPOINT}/{travel_id}")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun updateTravel(
        @Path("travel_id") travel_id: String,
        @Body params: CreateTravelDto
    ): Call<SingleTravelResponse>

    @DELETE("${Constants.TRAVELS_ENDPOINT}/{travel_id}")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun deleteTravel(@Path("travel_id") travel_id: String): Call<SingleTravelResponse>

    //EXPENSES
    @GET("${Constants.TRAVELS_ENDPOINT}/{travel_id}/expenses")
    fun getExpenses(@Path("travel_id") travel_id: String): Call<ExpensesResponse>

    @POST("${Constants.TRAVELS_ENDPOINT}/{travel_id}/expenses")
    fun addExpense(@Path("travel_id") travel_id: String, @Body expense: CreateExpenseDto): Call<SingleExpenseResponse>

    @PUT("${Constants.TRAVELS_ENDPOINT}/{travel_id}/expenses/{expense_id}")
    fun updateExpense(@Path("travel_id") travel_id:String, @Path("expense_id",) expense_id : String, @Body expense: CreateExpenseDto): Call<SingleExpenseResponse>

    @DELETE("${Constants.TRAVELS_ENDPOINT}/{travel_id}/expenses/{expense_id}")
    fun deleteExpense(@Path("travel_id") travel_id:String, @Path("expense_id",) expense_id : String) : Call<SingleExpenseResponse>
}