package ar.edu.ort.orionviajes.repository

import ar.edu.ort.orionviajes.Resource
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.GetTravelsResponse

//no extendia de base repo
class TravelRepository : BaseRepo() {

    val apiService by lazy{
        ApiClient.apiService
    }

    suspend fun getTravels() : Resource<GetTravelsResponse> { //iba : List<TravelX>
        return safeApiCall { apiService.getTravels() }
    //return withContext(Dispatchers.IO) {
           // val response = apiService.getTravels().execute()
            //response.body()!!
      //  }
    }


}