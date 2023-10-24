package com.example.airport_flight_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airport_flight_project.Utils.Companion.readJsonFromAssets

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListOfFlightsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListOfFlightsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: FlightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(FlightViewModel::class.java)

        // Récupérez le JSON transmis depuis l'activité précédente
        val jsonContent = readJsonFromAssets(requireContext(), "mock.json")

        val view = inflater.inflate(R.layout.fragment_list_of_flights, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, LinearLayoutManager(requireContext()).orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Créez un Adapter avec une liste vide pour commencer
        val adapter = FlightAdapter(emptyList())
        recyclerView.adapter = adapter

        // Mettez à jour le modèle avec le contenu JSON
        viewModel.updateJsonContent(jsonContent)

        // Observez les données dans le ViewModel et mettez à jour l'Adapter chaque fois qu'elles changent
        viewModel.jsonContent.observe(viewLifecycleOwner, { flightList ->

            adapter.updateFlights(flightList)

        })
        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListOfFlightsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListOfFlightsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}