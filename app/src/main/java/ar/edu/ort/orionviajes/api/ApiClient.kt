package ar.edu.ort.orionviajes.api

import android.content.Context
import ar.edu.ort.orionviajes.Constants
import ar.edu.ort.orionviajes.interceptors.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private lateinit var apiService: TravelsApi
    private lateinit var usersApi: UsersApi

    fun getUsersApi(): UsersApi {
        if (!::usersApi.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            usersApi = retrofit.create(UsersApi::class.java)
        }

        return usersApi
    }

    fun getTravelsApi(context: Context): TravelsApi {
        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okhttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(TravelsApi::class.java)
        }

        return apiService
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }

//    val httpClient by lazy {
//        OkHttpClient()
//            .newBuilder()
//            .build()
//    }
//
//    val retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl("http://192.168.1.21:3002/orion/")
//            .client(httpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    val apiService by lazy {
//        retrofit.create(ApiService::class.java)
//    }


}