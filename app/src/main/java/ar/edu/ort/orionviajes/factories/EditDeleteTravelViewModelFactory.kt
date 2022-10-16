package ar.edu.ort.orionviajes.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ar.edu.ort.orionviajes.viewmodels.EditDeleteTravelViewModel

class EditDeleteTravelViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditDeleteTravelViewModel(context) as T
    }
}