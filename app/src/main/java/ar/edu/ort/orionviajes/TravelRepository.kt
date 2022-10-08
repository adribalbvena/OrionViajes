package ar.edu.ort.orionviajes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TravelRepository {

    val apiService by lazy{
        ApiClient.apiService
    }

    suspend fun getTravels() : List<TravelX> {
        return withContext(Dispatchers.IO) {

            val response = apiService.getTravels().execute()
            response.body()!!
        }
    }


}