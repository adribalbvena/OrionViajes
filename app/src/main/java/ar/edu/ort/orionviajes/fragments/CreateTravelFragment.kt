package ar.edu.ort.orionviajes.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.CreateTravelDto
import ar.edu.ort.orionviajes.data.SingleTravelResponse
import ar.edu.ort.orionviajes.databinding.FragmentCreateTravelBinding
import ar.edu.ort.orionviajes.factories.CreateTravelViewModelFactory
import ar.edu.ort.orionviajes.viewmodels.CreateTravelViewModel
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
                addTravel()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePicker(view)

    }


    private fun addTravel() {
        val title = binding.editTextTitleTravel.text.toString()
        val budget = binding.editTextBudgetTravel.text.toString()
        val startDate = binding.startDateTil.text.toString()
        val endDate = binding.endDateTil.text.toString()

        val travel = CreateTravelDto(title, budget.toFloat(), startDate, endDate)

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