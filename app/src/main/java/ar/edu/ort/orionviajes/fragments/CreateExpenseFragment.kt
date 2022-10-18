package ar.edu.ort.orionviajes.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.data.CreateExpenseDto
import ar.edu.ort.orionviajes.data.Expense
import ar.edu.ort.orionviajes.databinding.FragmentCreateExpenseBinding
import ar.edu.ort.orionviajes.factories.CreateExpenseViewModelFactory
import ar.edu.ort.orionviajes.factories.TravelViewModelFactory
import ar.edu.ort.orionviajes.viewmodels.CreateExpenseViewModel
import ar.edu.ort.orionviajes.viewmodels.TravelViewModel
import com.google.android.material.snackbar.Snackbar

class CreateExpenseFragment : Fragment() {
    private lateinit var binding: FragmentCreateExpenseBinding

    private lateinit var createExpenseViewModel: CreateExpenseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateExpenseBinding.inflate(inflater, container, false)
        val view = binding.root
        initCreateExpenseViewModel()
        addExpenseObservable()

        //Lista de las monedas disponibles
//        val currency = listOf("ARS", "EUR", "USD", "CHF")
//        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, currency)
//       // (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
//        with(binding.autoCompleteTxtViewCurrency){
//            setAdapter(adapter)
//        }

        //tengo q traer el viaje
        val travel = CreateExpenseFragmentArgs.fromBundle(requireArguments()).travelId
        binding.btnAddExpense.setOnClickListener{
            addExpense(travel.id)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePicker(view)
        currencyPicker()
        categoryPicker()
        paymetMethodPicker()
    }

    private fun initCreateExpenseViewModel() {
        activity?.let {
            createExpenseViewModel =
                ViewModelProvider(this, CreateExpenseViewModelFactory(it)).get(CreateExpenseViewModel::class.java)
        }
    }

    private fun addExpense(travel_id: String) {
        val title = binding.editTextTitleExpense.text.toString()
        val currency = binding.autoCompleteTxtViewCurrency.text.toString()
        val amount = binding.editTextAmountExpense.text.toString()
        val category = binding.autoCompleteTxtViewCategory.text.toString()
        val paymentMethod = binding.autoCompleteTxtViewPaymentMethod.text.toString()
        val date = binding.dateExpenseTil.text.toString()

        //NO PONE EL ID Q VIENE EN LA API
        val expense = CreateExpenseDto(title, currency, amount.toFloat(), category,paymentMethod,date)

        createExpenseViewModel.addExpense(travel_id, expense)
    }

    private fun addExpenseObservable() {
        createExpenseViewModel.addExpense.observe(viewLifecycleOwner, Observer {
            //aca falta hacer algun manejo de errores

            Snackbar.make(binding.root, R.string.successCreateExpense, Snackbar.LENGTH_LONG).show()
            findNavController().navigateUp()
        })
    }

    fun paymetMethodPicker() {
        val paymentMethod = listOf("Efectivo", "Tarjeta")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, paymentMethod)
        with(binding.autoCompleteTxtViewPaymentMethod){
            setAdapter(adapter)
        }
    }

    fun categoryPicker() {
        val category = listOf("Comida", "Bebida", "Transporte", "Alojamiento")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, category)
        // (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        with(binding.autoCompleteTxtViewCategory){
            setAdapter(adapter)
        }
    }

    fun currencyPicker() {
        val currency = listOf("ARS", "EUR", "USD", "CHF")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, currency)
        with(binding.autoCompleteTxtViewCurrency){
            setAdapter(adapter)
        }
    }

    fun datePicker(view: View){
        binding = FragmentCreateExpenseBinding.bind(view)

        binding.apply {
            dateExpenseTil.setOnClickListener{
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    dateExpenseTil.text = date
                }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }
        }

    }



}