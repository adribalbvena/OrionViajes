package ar.edu.ort.orionviajes.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.ort.orionviajes.data.Expense

class ExpensesListViewModel(private val context: Context) : ViewModel(){

    private val mutableExpensesList = MutableLiveData<List<Expense>>()
    val expensesList: LiveData<List<Expense>> get() = mutableExpensesList

    fun expensesList(expenses : List<Expense>) {
        mutableExpensesList.value = expenses
    }
}