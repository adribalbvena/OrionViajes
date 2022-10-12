package ar.edu.ort.orionviajes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.ort.orionviajes.api.ApiClient.apiService
import ar.edu.ort.orionviajes.data.GetTravelsResponse
import ar.edu.ort.orionviajes.data.TravelX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TravelViewModel: ViewModel() {
    private var _travels = MutableLiveData<List<TravelX>?>()
    val travels: LiveData<List<TravelX>?> = _travels

    private var _addTravel = MutableLiveData<List<TravelX>?>()
    val addTravel: LiveData<List<TravelX>?> = _addTravel

    fun getTravels(){
        val apiService = apiService
        val call = apiService.getTravels()
        call.enqueue(object : Callback<GetTravelsResponse> {
            override fun onResponse(
                call: Call<GetTravelsResponse>,
                response: Response<GetTravelsResponse>
            ) {
                if(response.isSuccessful) {
                    _travels.postValue(response.body())
                } else {
                    _travels.postValue(null)
                }
            }

            override fun onFailure(call: Call<GetTravelsResponse>, t: Throwable) {
                _travels.postValue(null)
            }

        })
    }

    fun addTravel(travel : TravelX){
        val apiService = apiService
        val call = apiService.getTravels()
        call.enqueue(object : Callback<GetTravelsResponse> {
            override fun onResponse(
                call: Call<GetTravelsResponse>,
                response: Response<GetTravelsResponse>
            ) {
                if(response.isSuccessful) {
                    _addTravel.postValue(response.body())
                } else {
                    _addTravel.postValue(null)
                }
            }

            override fun onFailure(call: Call<GetTravelsResponse>, t: Throwable) {
               _addTravel.postValue(null)
            }

        })
    }

}