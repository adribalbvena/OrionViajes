package ar.edu.ort.orionviajes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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