package com.example.airport_flight_project

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.airport_flight_project.Utils.Companion.readJsonFromAssets

class FlightListActivity : AppCompatActivity() {

    private lateinit var viewModel: FlightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flight_list_view)

        viewModel = ViewModelProvider(this).get(FlightViewModel::class.java)

        // Récupérez le JSON transmis depuis l'activité précédente
        val jsonContent = readJsonFromAssets(this, "mock.json")

        // Mettez à jour le modèle avec le contenu JSON
        viewModel.updateJsonContent(jsonContent)

        val jsonTextView = findViewById<TextView>(R.id.jsonTextView)

        // Affichez le JSON depuis le modèle dans le TextView
        viewModel.jsonContent.observe(this, { flightList ->
            flightList.forEach { flightModel ->
                Log.e(TAG, flightModel.callsign)
                jsonTextView.append(flightModel.callsign + "\n")
            }
        })

    }
}
