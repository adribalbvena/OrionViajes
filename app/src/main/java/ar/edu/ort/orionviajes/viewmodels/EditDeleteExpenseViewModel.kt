package ar.edu.ort.orionviajes.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.CreateExpenseDto
import ar.edu.ort.orionviajes.data.SingleExpenseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditDeleteExpenseViewModel(private val context: Context) : ViewModel() {
    var updateExpense : MutableLiveData<SingleExpenseResponse?>
    var deleteExpense : MutableLiveData<SingleExpenseResponse?>

    init {
        updateExpense = MutableLiveData()
        deleteExpense = MutableLiveData()
    }

    fun updateExpense(travel_id: String, expense_id : String, expense: CreateExpenseDto) {
        val apiService = ApiClient.getTravelsApi(context)
        val call = apiService.updateExpense(travel_id,expense_id,expense)
        call.enqueue(object : Callback<SingleExpenseResponse> {
            override fun onResponse(
                call: Call<SingleExpenseResponse>,
                response: Response<SingleExpenseResponse>
            ) {
                if (response.isSuccessful){
                    updateExpense.postValue(response.body())
                } else {
                    updateExpense.postValue(null)
                }
            }

            override fun onFailure(call: Call<SingleExpenseResponse>, t: Throwable) {
                updateExpense.postValue(null)
            }

        })

    }

    fun deleteExpense(travel_id: String, expense_id : String) {
        val apiService = ApiClient.getTravelsApi(context)
        val call = apiService.deleteExpense(travel_id, expense_id)
        call.enqueue(object : Callback<SingleExpenseResponse> {
            override fun onResponse(
                call: Call<SingleExpenseResponse>,
                response: Response<SingleExpenseResponse>
            ) {
                if (response.isSuccessful){
                    deleteExpense.postValue(response.body())
                } else {
                    deleteExpense.postValue(null)
                }
            }

            override fun onFailure(call: Call<SingleExpenseResponse>, t: Throwable) {
                deleteExpense.postValue(null)
            }

        })
        }
    }
