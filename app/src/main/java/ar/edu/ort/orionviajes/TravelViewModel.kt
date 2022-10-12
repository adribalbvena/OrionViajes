package ar.edu.ort.orionviajes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.ort.orionviajes.data.GetTravelsResponse
import ar.edu.ort.orionviajes.data.TravelX
import ar.edu.ort.orionviajes.repository.TravelRepository
import kotlinx.coroutines.launch

class TravelViewModel: ViewModel() {
    private var _travels = MutableLiveData<Resource<GetTravelsResponse>>() //iba MutableLiveData<List<TravelX>>()
    val travels: LiveData<Resource<GetTravelsResponse>> = _travels

    private var _addTravel = MutableLiveData<Resource<GetTravelsResponse>>()
    val addTravel: LiveData<Resource<GetTravelsResponse>> = _addTravel


    val travelRepository by lazy {
        TravelRepository()
    }

    fun getTravels(){
        viewModelScope.launch(){
//            _travels.postValue(Resource.Loading())

            _travels.postValue(travelRepository.getTravels())
           // var response = travelRepository.getTravels()
           // _travels.postValue(response)
        }
    }

    fun addTravel(travel : TravelX){
        viewModelScope.launch(){
            _addTravel.postValue(travelRepository.addTravel(travel))
        }
    }

}