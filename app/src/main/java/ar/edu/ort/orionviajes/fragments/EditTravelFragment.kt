package ar.edu.ort.orionviajes.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import ar.edu.ort.orionviajes.Constants
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.CreateTravelDto
import ar.edu.ort.orionviajes.data.SingleTravelResponse
import ar.edu.ort.orionviajes.data.Travel
import ar.edu.ort.orionviajes.databinding.FragmentEditTravelBinding
import ar.edu.ort.orionviajes.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditTravelFragment : Fragment() {

    private lateinit var binding: FragmentEditTravelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditTravelBinding.inflate(inflater, container, false)
        val view = binding.root

        val travel = EditTravelFragmentArgs.fromBundle(requireArguments()).travel
        loadTravel(travel)

        binding.btnUpdateTravel.setOnClickListener {
            it.hideKeyboard()
            if (!validateInputs()){
                return@setOnClickListener
            }
            updateTravel(travel.id)
        }

        binding.btnDeleteTravel.setOnClickListener {
            showAlertDialog(travel.id)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePicker(view)
        currencyPicker()
    }

    fun showAlertDialog(travel_id: String) {
        var builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.deteleTavelLabel)
        builder.setMessage(R.string.areYouShureDeleteTravel)
        builder.setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, id ->
              deleteTravel(travel_id)
            dialog.cancel()
        })
        builder.setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
        var alert = builder.create()
        alert.show()
    }

    private fun deleteTravel(travel_id : String){
        val apiService = ApiClient.getTravelsApi(requireContext())
        val call = apiService.deleteTravel(travel_id)
        call.enqueue(object : Callback<SingleTravelResponse>{
            override fun onResponse(
                call: Call<SingleTravelResponse>,
                response: Response<SingleTravelResponse>
            ) {
                if (response.isSuccessful){
                    Snackbar.make(requireView(), response.body()!!.message.toString(), Snackbar.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }

            override fun onFailure(call: Call<SingleTravelResponse>, t: Throwable) {
                Snackbar.make(requireView(), R.string.somethingWentWrong, Snackbar.LENGTH_LONG).show()
                Log.d("ERROR AL ELIMINAR VIAJE", t.toString())
            }

        })

    }

    private fun updateTravel(travelId: String) {
        val title = binding.editTextTitleTravelEdit.text.toString()
        val currency = binding.autoCompleteTxtViewCurrencyEdit.text.toString()
        val budget = binding.editTextBudgetTravelEdit.text.toString()
        val startDate = binding.startDateEditTil.text.toString()
        val endDate = binding.endDateEditTil.text.toString()

        val travel = CreateTravelDto(title, currency, budget.toFloat(), startDate, endDate)

        val apiService = ApiClient.getTravelsApi(requireContext())
        val call = apiService.updateTravel(travelId, travel)
        call.enqueue(object : Callback<SingleTravelResponse>{
            override fun onResponse(
                call: Call<SingleTravelResponse>,
                response: Response<SingleTravelResponse>
            ) {
                if (response.isSuccessful){
                    Snackbar.make(requireView(), response.body()!!.message.toString(), Snackbar.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }

            override fun onFailure(call: Call<SingleTravelResponse>, t: Throwable) {
                Snackbar.make(requireView(), R.string.somethingWentWrong, Snackbar.LENGTH_LONG).show()
                Log.d("ERROR AL ACTUALIZAR VIAJE", t.toString())
            }

        })
    }

    private fun loadTravel(travel: Travel) {
        binding.editTextTitleTravelEdit.setText(travel.title)
        binding.autoCompleteTxtViewCurrencyEdit.setText(travel.currency)
        binding.editTextBudgetTravelEdit.setText(travel.budget.toString())
        binding.startDateEditTil.setText(travel.startDate)
        binding.endDateEditTil.setText(travel.endDate)
    }

    private fun validateInputs() : Boolean{
        //validar los campos vacios
        if(TextUtils.isEmpty(binding.editTextTitleTravelEdit.text) || TextUtils.isEmpty(binding.autoCompleteTxtViewCurrencyEdit.text)
            || TextUtils.isEmpty(binding.editTextBudgetTravelEdit.text) || TextUtils.isEmpty(binding.startDateEditTil.text) || TextUtils.isEmpty(binding.endDateEditTil.text)) {
            Snackbar.make(requireView(), R.string.emptyFields, Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun currencyPicker() {
        val currency = Constants.CURRENCIES
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, currency)
        with(binding.autoCompleteTxtViewCurrencyEdit){
            setAdapter(adapter)
        }
    }

    fun datePicker(view: View) {
        binding = FragmentEditTravelBinding.bind(view)

        binding.apply {
            startDateEditTil.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        startDateEditTil.text = date
                    }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }

            endDateEditTil.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date2 = bundle.getString("SELECTED_DATE")
                        endDateEditTil.text = date2

                    }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }

        }

    }

}