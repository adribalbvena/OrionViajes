package ar.edu.ort.orionviajes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.adapters.TravelRecyclerAdapter
import ar.edu.ort.orionviajes.TravelViewModel
import ar.edu.ort.orionviajes.data.TravelX
import ar.edu.ort.orionviajes.databinding.FragmentMyTravelsBinding
import ar.edu.ort.orionviajes.listener.OnTravelClickedListener
import com.google.android.material.snackbar.Snackbar


class MyTravelsFragment : Fragment(), OnTravelClickedListener{
    companion object {
        fun newInstance() = MyTravelsFragment()
    }

    private var _binding: FragmentMyTravelsBinding? = null
    private val binding get() = _binding!!

    private lateinit var travelViewModel : TravelViewModel
    private lateinit var travelRecyclerAdapter : TravelRecyclerAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyTravelsBinding.inflate(inflater,container,false)
        val view = binding.root


        initTravelsRecyclerView()
        initTravelsViewModel()

        return view

    }


    private fun initTravelsRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        //travelViewModel.getTravels()

        binding.travelsRecyclerView.setHasFixedSize(true)
        binding.travelsRecyclerView.layoutManager = linearLayoutManager
        travelRecyclerAdapter = TravelRecyclerAdapter(this)
        binding.travelsRecyclerView.adapter = travelRecyclerAdapter

        /*binding.travelsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            travelViewModel.getTravels()
            travelRecyclerAdapter = TravelRecyclerAdapter(this)
            adapter = travelRecyclerAdapter
        }*/
    }

//    private fun onItemSelected(it: TravelX) {
//        //Toast.makeText(context, it.id, Toast.LENGTH_SHORT).show()
//        val travelBundle = Bundle()
//        travelBundle.putString("data", it.id)
//        val fragment = CreateTravelFragment()
//        fragment.arguments = travelBundle
//        //esto no funciona, da null, tal vez parcelando?
//        //funciono parcelando asi q esto no tiene sentido
//
//    }


    fun initTravelsViewModel() {
        travelViewModel = ViewModelProvider(this).get(TravelViewModel::class.java)
        travelViewModel.travels.observe(viewLifecycleOwner, Observer{
            if (it == null) {
                Snackbar.make(binding.root, "Error al cargar los viajes" ,Snackbar.LENGTH_LONG).show()
            } else {
                travelRecyclerAdapter.updateList(it)
            }

        })
        travelViewModel.getTravels()
    }


    override fun onStart() {
        super.onStart()
        binding.normalFAB.setOnClickListener() {
            val action = MyTravelsFragmentDirections.actionMyTravelsFragmentToCreateTravelFragment()
            findNavController().navigate(action)
        }

    }

    override fun onTravelSelected(travel: TravelX) {
        val action = MyTravelsFragmentDirections.actionMyTravelsFragmentToExpensesFragment(travel)
        findNavController().navigate(action)
    }

    override fun onTravelEditClick(travel: TravelX) {
        val action = MyTravelsFragmentDirections.actionMyTravelsFragmentToEditTravelFragment(travel)
        findNavController().navigate(action)
    }




}