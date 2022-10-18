package ar.edu.ort.orionviajes.fragments

import android.app.AlertDialog
import android.content.DialogInterface
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
import androidx.navigation.fragment.findNavController
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.data.CreateExpenseDto
import ar.edu.ort.orionviajes.data.Expense
import ar.edu.ort.orionviajes.databinding.FragmentCreateExpenseBinding
import ar.edu.ort.orionviajes.databinding.FragmentCreateTravelBinding
import ar.edu.ort.orionviajes.databinding.FragmentEditExpenseBinding
import ar.edu.ort.orionviajes.databinding.FragmentEditTravelBinding
import ar.edu.ort.orionviajes.factories.EditDeleteExpenseViewModelFactory
import ar.edu.ort.orionviajes.viewmodels.EditDeleteExpenseViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.math.exp

class EditExpenseFragment : Fragment() {

    private lateinit var binding: FragmentEditExpenseBinding
    private lateinit var editDeleteExpenseViewModel: EditDeleteExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditExpenseBinding.inflate(inflater, container, false)
        val view = binding.root

        initEditExpenseViewModel()
        updateExpenseObservable()

        val expense = EditExpenseFragmentArgs.fromBundle(requireArguments()).expense
        val travel = EditExpenseFragmentArgs.fromBundle(requireArguments()).travel
        loadExpense(expense)

        binding.btnUpdateExpense.setOnClickListener{
            updateExpense(travel.id, expense.id)
        }

        binding.btnDeleteExpense.setOnClickListener{
            showAlertDialog(travel.id, expense.id)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePicker(view)
        currencyPicker()
        categoryPicker()
        paymentMethodPicker()
    }

    fun showAlertDialog(travel_id: String, expense_id: String) {
        var builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.deteleTavelLabel)
        builder.setMessage(R.string.areYouShureDeleteExpense)
        builder.setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, id ->
            editDeleteExpenseViewModel.deleteExpense(travel_id, expense_id)
            Snackbar.make(binding.root, R.string.successDeletedExpense, Snackbar.LENGTH_LONG).show()
            findNavController().navigateUp()
            dialog.cancel()
        })
        builder.setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
        var alert = builder.create()
        alert.show()
    }


    private fun updateExpenseObservable(){
        editDeleteExpenseViewModel.updateExpense.observe(viewLifecycleOwner, Observer{
            //manejo de errores falta
            Snackbar.make(binding.root, R.string.successUpdateExpense, Snackbar.LENGTH_LONG).show()
            findNavController().navigateUp()

        })
    }

    private fun updateExpense(travel_id : String, expense_id : String){
        val title = binding.editTextTitleExpenseEdit.text.toString()
        val currency = binding.autoCompleteTxtViewEditCurrency.text.toString()
        val amount = binding.editTextAmountExpenseEdit.text.toString()
        val category =  binding.autoCompleteTxtViewEditCategory.text.toString()
        val paymentMethod = binding.autoCompleteTxtViewEditPaymentMethod.text.toString()
        val date = binding.dateExpenseEditTil.text.toString()

        val expense = CreateExpenseDto(title, currency, amount.toFloat(), category, paymentMethod, date)

        editDeleteExpenseViewModel.updateExpense(travel_id, expense_id, expense)

    }

    private fun loadExpense(expense: Expense){
        binding.editTextTitleExpenseEdit.setText(expense.title)
        binding.autoCompleteTxtViewEditCurrency.setText(expense.currency)
        binding.editTextAmountExpenseEdit.setText(expense.amount.toString())
        binding.autoCompleteTxtViewEditCategory.setText(expense.category)
        binding.autoCompleteTxtViewEditPaymentMethod.setText(expense.paymentMethod)
        binding.dateExpenseEditTil.text = expense.date
    }


    private fun initEditExpenseViewModel(){
        activity?.let {
            editDeleteExpenseViewModel =
                ViewModelProvider(this, EditDeleteExpenseViewModelFactory(it)).get(EditDeleteExpenseViewModel::class.java)

        }
    }

    fun paymentMethodPicker() {
        val paymentMethod = listOf("Efectivo", "Tarjeta")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, paymentMethod)
        with(binding.autoCompleteTxtViewEditPaymentMethod){
            setAdapter(adapter)
        }
    }

    fun categoryPicker() {
        val category = listOf("Comida", "Bebida", "Transporte", "Alojamiento")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, category)
        // (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        with(binding.autoCompleteTxtViewEditCategory){
            setAdapter(adapter)
        }
    }

    fun currencyPicker() {
        val currency = listOf("ARS", "EUR", "USD", "CHF")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, currency)
        with(binding.autoCompleteTxtViewEditCurrency){
            setAdapter(adapter)
        }
    }


    fun datePicker(view: View){
        binding = FragmentEditExpenseBinding.bind(view)

        binding.apply {
            dateExpenseEditTil.setOnClickListener{
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    dateExpenseEditTil.text = date
                }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }
        }

    }
}