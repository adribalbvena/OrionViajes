package ar.edu.ort.orionviajes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TravelViewModel: ViewModel() {
    private var _travels = MutableLiveData<List<TravelX>>()
    val travels: LiveData<List<TravelX>> = _travels

    val travelRepository by lazy {
        TravelRepository()
    }

    fun getTravels(){
        viewModelScope.launch(){
            var response = travelRepository.getTravels()
            _travels.postValue(response)
        }
//        val list = mutableListOf<Travel>()
//        list.add(
//            Travel(
//                1,
//                "España 2022",
//                3000F,
//                "2022-10-06",
//                "2022-10-26"
//            )
//        )
//        list.add(
//            Travel(
//                2,
//                "Ibiza 2022",
//                1000F,
//                "2022-10-27",
//                "2022-11-02"
//            )
//        )
//        list.add(
//            Travel(
//                3,
//                "Francia 2022",
//                2000F,
//                "2022-11-02",
//                "2022-10-07"
//            )
//        )
        //_travels.value = list
    }
}