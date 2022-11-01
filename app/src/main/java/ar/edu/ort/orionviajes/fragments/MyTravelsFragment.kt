package ar.edu.ort.orionviajes.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.SessionManager
import ar.edu.ort.orionviajes.activities.LoginActivity
import ar.edu.ort.orionviajes.adapters.TravelRecyclerAdapter
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.GetTravelsResponse
import ar.edu.ort.orionviajes.data.Travel
import ar.edu.ort.orionviajes.databinding.FragmentMyTravelsBinding
import ar.edu.ort.orionviajes.factories.TravelViewModelFactory
import ar.edu.ort.orionviajes.listener.OnTravelClickedListener
import ar.edu.ort.orionviajes.viewmodels.TravelViewModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyTravelsFragment : Fragment(), OnTravelClickedListener, MenuProvider {
    companion object {
        fun newInstance() = MyTravelsFragment()
    }

    private lateinit var binding: FragmentMyTravelsBinding

    private lateinit var travelViewModel: TravelViewModel
    private lateinit var travelRecyclerAdapter: TravelRecyclerAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyTravelsBinding.inflate(inflater,container,false)
        val view = binding.root


        initTravelsRecyclerView()
        getTravels()
        //initTravelsViewModel()

        return view

    }


    private fun initTravelsRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.travelsRecyclerView.setHasFixedSize(true)
        binding.travelsRecyclerView.layoutManager = linearLayoutManager
        travelRecyclerAdapter = TravelRecyclerAdapter(this)
        binding.travelsRecyclerView.adapter = travelRecyclerAdapter

    }

    fun getTravels() {
        val apiService = ApiClient.getTravelsApi(requireContext())
        val call = apiService.getTravels()
        call.enqueue(object : Callback<GetTravelsResponse> {
            override fun onResponse(
                call: Call<GetTravelsResponse>,
                response: Response<GetTravelsResponse>
            ) {
                if (response.isSuccessful) {
                   // val travelList = response.body()
                    travelRecyclerAdapter.updateList(response.body()!!)

                } else {
                    //aca voy a hacer en todos if response.code == 401
                    // if this is login fragment credenciales invalidas y sino, logout
                    //if response.errorBody, algo salio mal
                    if(response.code() == 401){
                        //logout
                        Snackbar.make(requireView(), "Tu sesión ha expirado! Vuelve a ingresar", Snackbar.LENGTH_LONG).show()
                        activity?.let {
                            SessionManager(it).deleteAuthToken()
                            activity?.finish()
                        }
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                    }

                }
            }

            override fun onFailure(call: Call<GetTravelsResponse>, t: Throwable) {
                Snackbar.make(requireView(), "Ups! Algo salió mal", Snackbar.LENGTH_LONG).show()
            }

        })
    }

//
//    fun initTravelsViewModel() {
//        activity?.let {
//            travelViewModel =
//                ViewModelProvider(this, TravelViewModelFactory(it)).get(TravelViewModel::class.java)
//        }
//
//        travelViewModel.travels.observe(viewLifecycleOwner, Observer {
//            if (it == null) {
//                Snackbar.make(binding.root, R.string.errorLoadingTravels ,Snackbar.LENGTH_LONG).show()
//            } else {
//                travelRecyclerAdapter.updateList(it)
//            }
//
//        })
//
//        travelViewModel.getTravels()
//    }


    override fun onStart() {
        super.onStart()
        binding.normalFAB.setOnClickListener() {
            val action = MyTravelsFragmentDirections.actionMyTravelsFragmentToCreateTravelFragment()
            findNavController().navigate(action)
        }
    }

    override fun onTravelSelected(travel: Travel) {
        val action = MyTravelsFragmentDirections.actionMyTravelsFragmentToExpensesFragment(travel)
        findNavController().navigate(action)
    }

    override fun onTravelEditClick(travel: Travel) {
        val action = MyTravelsFragmentDirections.actionMyTravelsFragmentToEditTravelFragment(travel)
        findNavController().navigate(action)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.drawer_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.profileFragment) {
            val action = MyTravelsFragmentDirections.actionMyTravelsFragmentToProfileFragment()
            findNavController().navigate(action)

            return true
        }
        return false
    }


}