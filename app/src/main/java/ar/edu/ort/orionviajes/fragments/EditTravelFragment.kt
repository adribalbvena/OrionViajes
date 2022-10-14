package ar.edu.ort.orionviajes.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.TravelViewModel
import ar.edu.ort.orionviajes.data.TravelX
import ar.edu.ort.orionviajes.databinding.FragmentCreateTravelBinding
import ar.edu.ort.orionviajes.databinding.FragmentEditTravelBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class EditTravelFragment : Fragment() {

    private var _binding : FragmentEditTravelBinding? = null
    private val binding get() =  _binding!!

    private lateinit var travelViewModel : TravelViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditTravelBinding.inflate(inflater, container, false)
        val view = binding.root

        initTravelViewModel()
        addUpdateObservable()
        //addDeleteObservable()

        val travel = EditTravelFragmentArgs.fromBundle(requireArguments()).travel
       // Toast.makeText(context, dato.id.toString(), Toast.LENGTH_SHORT).show()
        loadTravel(travel)

        binding.btnUpdateTravel.setOnClickListener{
            updateTravel(travel.id)
        }

        binding.btnDeleteTravel.setOnClickListener{
            showAlertDialog(travel.id)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePicker(view)
    }

    fun showAlertDialog(travel_id: String) {
        var builder = AlertDialog.Builder(activity)
        builder.setTitle("Eliminar")
        builder.setMessage("Estas seguro de que quieres eliminar este viaje?")
        builder.setPositiveButton("Si", DialogInterface.OnClickListener {dialog, id ->
            travelViewModel.deleteTravel(travel_id)
            Snackbar.make(binding.root, "Viaje eliminado con éxito!" , Snackbar.LENGTH_LONG).show()
            activity?.supportFragmentManager?.popBackStack()
            dialog.cancel()
        })
        builder.setNegativeButton("No",DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
        var alert = builder.create()
        alert.show()
    }


    private fun addUpdateObservable() {
        travelViewModel.updateTravel.observe(viewLifecycleOwner, Observer{
                //aca tambien falta manejo de errores
                Snackbar.make(binding.root, "Viaje actualizado con éxito!" , Snackbar.LENGTH_LONG).show()
                activity?.supportFragmentManager?.popBackStack()
        })
    }

    fun updateTravel(travel_id : String) {
        val title = binding.editTextTitleTravelEdit.text.toString()
        val budget = binding.editTextBudgetTravelEdit.text.toString()
        val startDate = binding.startDateEditTil.text.toString()
        val endDate = binding.endDateEditTil.text.toString()

        val travel = TravelX(travel_id, title, budget.toFloat(), startDate, endDate)

        travelViewModel.updateTravel(travel_id, travel)
    }

    fun loadTravel(travel : TravelX) {
                binding.editTextTitleTravelEdit.setText(travel.title)
                binding.editTextBudgetTravelEdit.setText(travel.budget.toString())
                binding.startDateEditTil.setText(travel.startDate) //esto estaria bueno editarlo en la api para q aparezca el formato bien
                binding.endDateEditTil.setText(travel.endDate)
    }

    private fun initTravelViewModel() {
        travelViewModel = ViewModelProvider(this).get(TravelViewModel::class.java)
    }

    fun datePicker(view: View){
        _binding = FragmentEditTravelBinding.bind(view)

        binding.apply {
            startDateEditTil.setOnClickListener{
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
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
                    viewLifecycleOwner) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val date2 = bundle.getString("SELECTED_DATE")
                    endDateEditTil.text = date2

                }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }

        }

    }

}