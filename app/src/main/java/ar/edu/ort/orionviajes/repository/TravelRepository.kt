package ar.edu.ort.orionviajes.repository

import ar.edu.ort.orionviajes.Resource
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.GetTravelsResponse
import ar.edu.ort.orionviajes.data.TravelX
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

    suspend fun addTravel(travel : TravelX) : Resource<GetTravelsResponse> {
        return safeApiCall { apiService.addTravel(travel) }
    }

    //addTravel
    //updateTravel
    //deleteTravel
    //getExpenses
    //addExpense
    //updateExpense
    //deleteExpense
    //getCategories
    //getPaymentMethods

}