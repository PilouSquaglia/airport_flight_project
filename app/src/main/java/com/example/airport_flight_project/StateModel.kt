package com.example.airport_flight_project

data class StateModel(
    val time: Long,
    val states: List<Any>


)

data class State(
    val icao24: String,
    val callsign: String,
    val origin_country: String,
    val time_position: Long,
    val last_contact: Long,
    val longitude: Double,
    val latitude: Double,
    val altitude: Double,
    val isGrounded: Boolean,
    val speed: Float,
    val true_track: Double?,
    val vertical_rate: Double?,
    val sensors: List<Int>?,
    val geo_altitude: Double?,
    val squawk: String?,
    val spi: Boolean,
    val category: Double,
)

