package ar.edu.ort.orionviajes

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.GetTravelsResponse
import ar.edu.ort.orionviajes.data.SingleTravelResponse
import ar.edu.ort.orionviajes.data.TravelX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TravelViewModel(private val context: Context) : ViewModel() {
    //    private var _travels = MutableLiveData<List<TravelX>?>()
//    val travels: LiveData<List<TravelX>?> = _travels
//
//    private var _addTravel = MutableLiveData<List<TravelX>?>()
//    val addTravel: LiveData<List<TravelX>?> = _addTravel

    var travels: MutableLiveData<GetTravelsResponse?>
    var singleTravel: MutableLiveData<SingleTravelResponse?>
    var addTravel: MutableLiveData<GetTravelsResponse?>
    var updateTravel: MutableLiveData<SingleTravelResponse?>
    var deleteTravel: MutableLiveData<SingleTravelResponse?>

    init {
        travels = MutableLiveData()
        singleTravel = MutableLiveData()
        addTravel = MutableLiveData()
        updateTravel = MutableLiveData()
        deleteTravel = MutableLiveData()
    }

    fun getTravels() {
        val apiService = ApiClient.getTravelsApi(context);
        val call = apiService.getTravels()
        call.enqueue(object : Callback<GetTravelsResponse> {
            override fun onResponse(
                call: Call<GetTravelsResponse>,
                response: Response<GetTravelsResponse>
            ) {
                if (response.isSuccessful) {
                    travels.postValue(response.body())
                } else {
                    travels.postValue(null)
                }
            }

            override fun onFailure(call: Call<GetTravelsResponse>, t: Throwable) {
                travels.postValue(null)
            }

        })
    }

    fun addTravel(travel: TravelX) {
        val apiService = ApiClient.getTravelsApi(context);
        val call = apiService.addTravel(travel)
        call.enqueue(object : Callback<GetTravelsResponse> {
            override fun onResponse(
                call: Call<GetTravelsResponse>,
                response: Response<GetTravelsResponse>
            ) {
                if (response.isSuccessful) {
                    addTravel.postValue(response.body())
                } else {
                    addTravel.postValue(null)
                }
            }

            override fun onFailure(call: Call<GetTravelsResponse>, t: Throwable) {
                addTravel.postValue(null)
            }

        })
    }

    fun updateTravel(travel_id: String, travel: TravelX) {
        val apiService = ApiClient.getTravelsApi(context);
        val call = apiService.updateTravel(travel_id, travel)
        call.enqueue(object : Callback<SingleTravelResponse> {
            override fun onResponse(
                call: Call<SingleTravelResponse>,
                response: Response<SingleTravelResponse>
            ) {
                if (response.isSuccessful) {
                    updateTravel.postValue(response.body())
                } else {
                    updateTravel.postValue(null)

                }
            }

            override fun onFailure(call: Call<SingleTravelResponse>, t: Throwable) {
                updateTravel.postValue(null)
            }

        })
    }

    fun deleteTravel(travel_id: String) {
        val apiService = ApiClient.getTravelsApi(context);
        val call = apiService.deleteTravel(travel_id)
        call.enqueue(object : Callback<SingleTravelResponse> {
            override fun onResponse(
                call: Call<SingleTravelResponse>,
                response: Response<SingleTravelResponse>
            ) {
                if (response.isSuccessful) {
                    deleteTravel.postValue(response.body())
                } else {
                    deleteTravel.postValue(null)
                }
            }

            override fun onFailure(call: Call<SingleTravelResponse>, t: Throwable) {
                deleteTravel.postValue(null)
            }

        })
    }

    fun getSingleTravel(travel_id: String) {
        val apiService = ApiClient.getTravelsApi(context);
        val call = apiService.getSingleTravel(travel_id)
        call.enqueue(object : Callback<SingleTravelResponse> {
            override fun onResponse(
                call: Call<SingleTravelResponse>,
                response: Response<SingleTravelResponse>
            ) {
                if (response.isSuccessful) {
                    singleTravel.postValue(response.body())
                } else {
                    singleTravel.postValue(null)
                }
            }

            override fun onFailure(call: Call<SingleTravelResponse>, t: Throwable) {
                singleTravel.postValue(null)

            }
        })
    }
}