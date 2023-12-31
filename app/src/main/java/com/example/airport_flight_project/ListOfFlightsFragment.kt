package com.example.airport_flight_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass.
 * Use the [ListOfFlightsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListOfFlightsFragment : Fragment(), OnFlightClickListener{
    private lateinit var viewModel: FlightViewModel

    private lateinit var loadingIndicator: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)

        // Récupérez le JSON transmis depuis l'activité précédente
        val view = inflater.inflate(R.layout.fragment_list_of_flights, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        loadingIndicator = view.findViewById(R.id.loading_indicator_flight_list)
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, LinearLayoutManager(requireContext()).orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Créez un Adapter avec une liste vide pour commencer
        val adapter = FlightAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        // Observez les données dans le ViewModel et mettez à jour l'Adapter chaque fois qu'elles changent
        viewModel.getFlightListLiveData().observe(viewLifecycleOwner, Observer { flightList ->

            adapter.updateFlights(flightList.toList())
            loadingIndicator.visibility = View.INVISIBLE
        })

        return view

    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListOfFlightsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onFlightClick(flight: FlightModel) {
        viewModel.setFlightLiveData(flight)
//        mapViewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
//        mapViewModel.setFlightLiveData(flight)
//        val newFragment = MapFragment()
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainerView, newFragment)
//            .addToBackStack(null)
//            .commit()
    }


}