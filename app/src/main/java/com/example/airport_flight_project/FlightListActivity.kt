package com.example.airport_flight_project

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.airport_flight_project.Utils.Companion.readJsonFromAssets

class FlightListActivity : AppCompatActivity() {

    private lateinit var viewModel : FlightModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flight_list_view)

//        viewModel = ViewModelProvider(this).get(FlightModel::class.java)

        val jsonTextView = findViewById<TextView>(R.id.jsonTextView)

        // Récupérez le JSON transmis depuis l'activité précédente
//        val jsonResult = intent.getStringExtra("json_data")
        val jsonContent = readJsonFromAssets(this, "mock.json")

        // Affichez le JSON dans le TextView
        jsonTextView.text = jsonContent
    }
}