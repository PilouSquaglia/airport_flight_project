package com.example.airport_flight_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airport_flight_project.Utils.Companion.readJsonFromAssets

class FlightListActivity : AppCompatActivity() {

    //private lateinit var viewModel: FlightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flight_list_view)
/*
        viewModel = ViewModelProvider(this).get(FlightViewModel::class.java)

        // Récupérez le JSON transmis depuis l'activité précédente
        val jsonContent = readJsonFromAssets(this, "mock.json")

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Ajouter un séparateur
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, LinearLayoutManager(this).orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Créez un Adapter avec une liste vide pour commencer
        val adapter = FlightAdapter(emptyList())
        recyclerView.adapter = adapter

        // Mettez à jour le modèle avec le contenu JSON
        viewModel.updateJsonContent(jsonContent)

        // Observez les données dans le ViewModel et mettez à jour l'Adapter chaque fois qu'elles changent
        viewModel.jsonContent.observe(this, { flightList ->

            adapter.updateFlights(flightList)

        })
*/
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = ListOfFlightsFragment()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
}