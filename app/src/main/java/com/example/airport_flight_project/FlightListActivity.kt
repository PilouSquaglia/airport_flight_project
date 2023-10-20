package com.example.airport_flight_project

import android.os.Bundle
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
        viewModel.jsonContent.observe(this, { json ->
            jsonTextView.text = json
        })
    }
}
