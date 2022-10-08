package ar.edu.ort.orionviajes

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {
    val httpClient by lazy {
        OkHttpClient()
            .newBuilder()
            .build()
    }

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.21:3002/orion/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create()) //aca iba moshi converter factory
            .build()
    }

    val apiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}