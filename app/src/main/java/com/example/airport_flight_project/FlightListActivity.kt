package com.example.airport_flight_project

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airport_flight_project.Utils.Companion.readJsonFromAssets

class FlightListActivity : AppCompatActivity() {

    private lateinit var viewModel: FlightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flight_list_view)

        val airport = intent.getStringExtra("airport").toString()
        val depart = intent.getStringExtra("depart").toString()
        val arrivee = intent.getStringExtra("arrivee").toString()
        val airportSwitch = intent.getBooleanExtra("airportSwitch",false)

        val jsonData = intent.getStringExtra("json_data").toString()
        Log.i("res", jsonData)

        viewModel = ViewModelProvider(this).get(FlightViewModel::class.java)

        viewModel.requestFlightList(airport = airport,depart = depart,arrivee= arrivee, airportSwitch= airportSwitch, context = this )

        val transaction = supportFragmentManager.beginTransaction()
        val fragment = ListOfFlightsFragment()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
}