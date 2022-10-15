package ar.edu.ort.orionviajes.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.TravelViewModel
import ar.edu.ort.orionviajes.data.GetTravelsResponse
import ar.edu.ort.orionviajes.data.SingleTravelResponse
import ar.edu.ort.orionviajes.data.TravelX
import ar.edu.ort.orionviajes.databinding.FragmentCreateTravelBinding
import com.google.android.material.snackbar.Snackbar

class CreateTravelFragment : Fragment() {
    private var _binding : FragmentCreateTravelBinding? = null
    private val binding get() = _binding!!

    private lateinit var travelViewModel : TravelViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateTravelBinding.inflate(inflater,container,false)
        val view = binding.root

        initTravelViewModel()
        addTravelObservable()

            binding.btnAddTravel.setOnClickListener {
                addTravel()
        }

        //Lista de las monedas disponibles
//        val currency = listOf("ARS", "EUR", "USD", "CHF")
//        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, currency)
//       // (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
//        with(binding.autoCompleteTxtView){
//            setAdapter(adapter)
//        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePicker(view)

    }


    private fun initTravelViewModel() {
        travelViewModel = ViewModelProvider(this).get(TravelViewModel::class.java)
    }

    private fun addTravel() {
        val title = binding.editTextTitleTravel.text.toString()
        val budget = binding.editTextBudgetTravel.text.toString()
        val startDate = binding.startDateTil.text.toString()
        val endDate = binding.endDateTil.text.toString()

        val travel = TravelX("", title, budget.toFloat(), startDate, endDate)

        travelViewModel.addTravel(travel)
    }


    private fun addTravelObservable() {
        travelViewModel.addTravel.observe(viewLifecycleOwner, Observer {
            //aca falta hacer algun manejo de errores
            Log.d("TRAVELS", it.toString())
            //llegan en null los viajes
                Snackbar.make(binding.root, "Viaje creado con exito", Snackbar.LENGTH_LONG).show()
                activity?.supportFragmentManager?.popBackStack()
        })
    }

    fun datePicker(view: View){
        _binding = FragmentCreateTravelBinding.bind(view)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}