package ar.edu.ort.orionviajes.fragments

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
import ar.edu.ort.orionviajes.databinding.FragmentCreateTravelBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateTravelFragment : Fragment() {
    private lateinit var binding: FragmentCreateTravelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateTravelBinding.inflate(inflater,container,false)
        val view = binding.root


            binding.btnAddTravel.setOnClickListener {
                if (!validateInputs()){
                    return@setOnClickListener
                }
                addTravel()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePicker(view)
        currencyPicker()
    }

    private fun validateInputs() : Boolean{
        //validar los campos vacios
        if(TextUtils.isEmpty(binding.editTextTitleTravel.text) || TextUtils.isEmpty(binding.autoCompleteTxtViewCurrency.text)
            || TextUtils.isEmpty(binding.editTextBudgetTravel.text) || TextUtils.isEmpty(binding.startDateTil.text) || TextUtils.isEmpty(binding.endDateTil.text)) {
            Snackbar.make(requireView(), R.string.emptyFields, Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }


    private fun addTravel() {
        val title = binding.editTextTitleTravel.text.toString()
        val currency = binding.autoCompleteTxtViewCurrency.text.toString()
        val budget = binding.editTextBudgetTravel.text.toString()
        val startDate = binding.startDateTil.text.toString()
        val endDate = binding.endDateTil.text.toString()

        val travel = CreateTravelDto(title, currency, budget.toFloat(), startDate, endDate)

        val apiService = ApiClient.getTravelsApi(requireContext())
        val call = apiService.addTravel(travel)
        call.enqueue(object : Callback<SingleTravelResponse>{
            override fun onResponse(
                call: Call<SingleTravelResponse>,
                response: Response<SingleTravelResponse>
            ) {
                if(response.isSuccessful){
                    Snackbar.make(requireView(), response.body()!!.message.toString(), Snackbar.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }
            override fun onFailure(call: Call<SingleTravelResponse>, t: Throwable) {
                Snackbar.make(requireView(), R.string.somethingWentWrong, Snackbar.LENGTH_LONG).show()
                Log.d("ERROR AL CREAR VIAJE", t.toString())
            }

        })

    }

    fun currencyPicker() {
        val currency = Constants.CURRENCIES
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, currency)
        with(binding.autoCompleteTxtViewCurrency){
            setAdapter(adapter)
        }
    }

    fun datePicker(view: View){
        binding = FragmentCreateTravelBinding.bind(view)

        binding.apply {
            startDateTil.setOnClickListener{
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    startDateTil.text = date
                }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }

            endDateTil.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val date2 = bundle.getString("SELECTED_DATE")
                    endDateTil.text = date2

                }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }

        }

    }

}