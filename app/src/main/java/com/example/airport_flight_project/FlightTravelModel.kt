package com.example.airport_flight_project

data class FlightTravelModel(

        val icao24: String,
        val callsign: String?,
        val startTime: Long,
        val endTime: Long,
        val path: List<List<Any>>

)