package ar.edu.ort.orionviajes.api

import ar.edu.ort.orionviajes.Constants
import ar.edu.ort.orionviajes.data.UserDto
import retrofit2.Call
import retrofit2.http.*

interface UsersApi {
    @POST(Constants.LOGIN_CONTROLLER)
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun login(@Body user: UserDto): Call<String>

    @POST(Constants.REGISTER_CONTROLLER)
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun register(@Body user: UserDto): Call<Unit>
}