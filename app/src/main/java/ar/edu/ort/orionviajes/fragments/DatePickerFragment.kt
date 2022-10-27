package ar.edu.ort.orionviajes.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import java.text.SimpleDateFormat
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val picker = DatePickerDialog(requireActivity(), this, year, month, day)
        picker.datePicker.minDate = calendar.timeInMillis
        calendar.add(Calendar.YEAR, +2)
        picker.datePicker.maxDate = calendar.timeInMillis

        return picker
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)


        val selectedDate = SimpleDateFormat("dd-MM-yyyy", Locale("es", "ES")).format(calendar.time)

        val selectedDateBundle = Bundle()
        selectedDateBundle.putString("SELECTED_DATE", selectedDate)

        setFragmentResult("REQUEST_KEY", selectedDateBundle)
    }
}