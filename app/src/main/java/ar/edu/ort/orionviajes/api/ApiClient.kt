package ar.edu.ort.orionviajes.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val httpClient = OkHttpClient()
        .newBuilder()
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.10.10.173:3002/orion/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)


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