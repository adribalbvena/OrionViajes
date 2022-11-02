package ar.edu.ort.orionviajes.fragments

import android.content.Intent
import android.os.Bundle
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
import ar.edu.ort.orionviajes.data.CreateExpenseDto
import ar.edu.ort.orionviajes.data.Receipt
import ar.edu.ort.orionviajes.data.SingleExpenseResponse
import ar.edu.ort.orionviajes.databinding.FragmentEditScanedExpenseBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EditScanedExpenseFragment : Fragment() {
    private lateinit var binding: FragmentEditScanedExpenseBinding
    private val calendar = Calendar.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditScanedExpenseBinding.inflate(inflater, container, false)

        val receipt = EditScanedExpenseFragmentArgs.fromBundle(requireArguments()).receipt
        val travelId = EditScanedExpenseFragmentArgs.fromBundle(requireArguments()).travelId

        loadReceipt(receipt)



        binding.btnSaveScanedExpense.setOnClickListener{
            val expense = getScanedExpense()
            Log.d("GASTO:", expense.toString())
             addExpense(travelId, expense)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePicker(view)
        currencyPicker()
        categoryPicker()
        paymentMethodPicker()
    }

    private fun getScanedExpense(): CreateExpenseDto {
        val title = binding.editTextTitleExpenseEditScaned.text.toString()
        val currency = binding.autoCompleteTxtViewEditScanedCurrency.text.toString()
        val amount = binding.editTextAmountExpenseEditScaned.text.toString()
        val category = binding.autoCompleteTxtViewEditScanedCategory.text.toString()
        val paymentMethod = binding.autoCompleteTxtViewEditScanedPaymentMethod.text.toString()
        val date = binding.dateScanedExpenseEditTil.text.toString()

        val expense = CreateExpenseDto(title, currency, amount.toFloat(), category, paymentMethod, date)
        return expense
    }

    private fun addExpense(travelId : String, expense: CreateExpenseDto) {
        val apiService = ApiClient.getTravelsApi(requireContext())
        val call = apiService.addExpense(travelId, expense)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                if (response.isSuccessful){
                    Snackbar.make(requireView(), "Gasto agregado con éxito!", Snackbar.LENGTH_LONG).show()
                    //val action = EditScanedExpenseFragmentDirections.actionEditScanedExpenseFragmentToExpensesFragment()
                    //findNavController().navigate(action)
                    //falta poder navegar desde aca a expenses
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Snackbar.make(requireView(), R.string.somethingWentWrong, Snackbar.LENGTH_LONG).show()
                Log.d("ERROR AL CARGAR GASTO", t.toString())
            }

        })
    }


    private fun loadReceipt(receipt: Receipt){
        val date = SimpleDateFormat("dd-MM-yyyy", Locale("es", "ES")).format(calendar.time)

        binding.editTextAmountExpenseEditScaned.setText(receipt.total.toString())
        binding.dateScanedExpenseEditTil.setText(date)
    }

    fun paymentMethodPicker() {
        val paymentMethod = Constants.PAYMENT_METHOD
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, paymentMethod)
        with(binding.autoCompleteTxtViewEditScanedPaymentMethod){
            setAdapter(adapter)
        }
    }

    fun categoryPicker() {
        val category = Constants.CATEGORIES
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, category)
        // (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        with(binding.autoCompleteTxtViewEditScanedCategory){
            setAdapter(adapter)
        }
    }

    fun currencyPicker() {
        val currency = Constants.CURRENCIES
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, currency)
        with(binding.autoCompleteTxtViewEditScanedCurrency){
            setAdapter(adapter)
        }
    }


    fun datePicker(view: View){
        binding = FragmentEditScanedExpenseBinding.bind(view)

        binding.apply {
            dateScanedExpenseEditTil.setOnClickListener{
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    dateScanedExpenseEditTil.text = date
                }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }
        }

    }

}