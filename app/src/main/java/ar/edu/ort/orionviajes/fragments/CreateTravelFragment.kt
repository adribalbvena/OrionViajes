package ar.edu.ort.orionviajes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.databinding.FragmentCreateTravelBinding

class CreateTravelFragment : Fragment() {
    private var _binding : FragmentCreateTravelBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateTravelBinding.inflate(inflater,container,false)
        val view = binding.root

        //Lista de las monedas disponibles
        val currency = listOf("ARS", "EUR", "USD", "CHF")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, currency)
       // (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        with(binding.autoCompleteTxtView){
            setAdapter(adapter)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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