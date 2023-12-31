package com.example.airport_flight_project

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var fromDateTextView: TextView
    private lateinit var toDateTextView: TextView
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_page)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        fromDateTextView = findViewById(R.id.from_date)
        toDateTextView = findViewById(R.id.to_date)
        val airportListLiveData = viewModel.getAirportListLiveData()
        val airportList = airportListLiveData.value ?: emptyList()
        val spinner = findViewById<Spinner>(R.id.airport_spinner)
        val airportSwitch = findViewById<Switch>(R.id.airport_switch)

        val searchButton = findViewById<Button>(R.id.button)

        viewModel.getAirportListLiveData().observe(this){
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, airportList)
        spinner.adapter = spinnerAdapter
        }

        searchButton.setOnClickListener{

            // viewModel.requestFlightList(airportSwitch.isChecked, spinner.selectedItemPosition, context = this)
            // val request = viewModel.getFlightListLiveData()
            // Effectuez la requête pour obtenir le JSON, par exemple avec Retrofit ou Volley
            // val jsonResult = request.toString()
            // Log.i("res", jsonResult)
            val airport = viewModel.getAirportListLiveData().value!![spinner.selectedItemPosition].icao
            val depart =( viewModel.getBeginDateLiveData().value!!.timeInMillis / 1000).toString()
            val arrivee =( viewModel.getEndDateLiveData().value!!.timeInMillis / 1000).toString()

            // Créez une intention pour ouvrir la nouvelle activité
            val intent = Intent(this, FlightListActivity::class.java)
            intent.putExtra("airport", airport)
            intent.putExtra("depart",depart)
            intent.putExtra("arrivee", arrivee)
            intent.putExtra("airportSwitch", airportSwitch.isChecked)

            startActivity(intent)
        }

        viewModel.getBeginDateLiveData().observe(this, Observer { calendar ->
            fromDateTextView.text = Utils.formatCalendarDate(calendar)
        })

        viewModel.getEndDateLiveData().observe(this, Observer { calendar ->
            toDateTextView.text = Utils.formatCalendarDate(calendar)
        })

        viewModel.getAirportNamesListLiveData().observe(this, Observer { airportNames ->
            // Mise à jour de votre interface utilisateur avec les noms des aéroports si nécessaire
        })

        viewModel.getAirportListLiveData().observe(this, Observer { airportList ->
            // Mise à jour de votre interface utilisateur avec la liste des aéroports si nécessaire
        })

        fromDateTextView.setOnClickListener {
            showDatePickerDialog(MainViewModel.DateType.BEGIN)
        }

        toDateTextView.setOnClickListener {
            showDatePickerDialog(MainViewModel.DateType.END)
        }
    }

    private fun showDatePickerDialog(dateType: MainViewModel.DateType) {
        val calendarLiveData = if (dateType == MainViewModel.DateType.BEGIN) {
            viewModel.getBeginDateLiveData()
        } else {
            viewModel.getEndDateLiveData()
        }

        val calendar = calendarLiveData.value ?: Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                viewModel.updateCalendarLiveData(dateType, selectedCalendar)
            },
            2022, 1, 6
        )
        datePickerDialog.show()
    }
}