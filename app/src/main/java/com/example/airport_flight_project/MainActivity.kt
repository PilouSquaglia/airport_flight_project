package com.example.airport_flight_project

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var fromDateTextView: TextView
    private lateinit var toDateTextView:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_page)
        val airportList = Utils.generateAirportList()

        val spinner = findViewById<Spinner>(R.id.airport_spinner)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, airportList)
        spinner.adapter = spinnerAdapter

        fromDateTextView = findViewById(R.id.from_date)
        toDateTextView = findViewById(R.id.to_date)

        fromDateTextView.setOnClickListener { showDatePickerDialog(fromDateTextView) }
        toDateTextView.setOnClickListener { showDatePickerDialog(toDateTextView) }
    }

    private fun showDatePickerDialog(textView: TextView) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            textView.text = selectedDate
        }

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            2024,
            1,
            6
        ) // Définissez la date initiale ici
        datePickerDialog.show()
    }
}