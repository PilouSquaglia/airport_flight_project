package com.example.airport_flight_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_page)
        val airportList = Utils.generateAirportList()

        val spinner = findViewById<Spinner>(R.id.airport_spinner)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, airportList)
        spinner.adapter = spinnerAdapter
    }
}