package com.example.airport_flight_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date

interface OnFlightClickListener {
    fun onFlightClick(flight: FlightModel)
}
class FlightAdapter(private var flights: List<FlightModel>, private val listener: OnFlightClickListener) : RecyclerView.Adapter<FlightAdapter.FlightViewHolder>() {

    class FlightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val departureDate: TextView
        val departureAirport: TextView
        val timeToTravel: TextView
        val arrivedDate: TextView
        val arrivedAirport: TextView
        val callSign: TextView

        init {
            // Define click listener for the ViewHolder's View
            departureDate = view.findViewById(R.id.departure_date_cell_flight)
            departureAirport = view.findViewById(R.id.depature_airport_cell_flight)
            timeToTravel = view.findViewById(R.id.time_travel_cell_flight)
            arrivedDate = view.findViewById(R.id.arrived_date_cell_flight)
            arrivedAirport = view.findViewById(R.id.arrived_airport_cell_flight)
            callSign = view.findViewById(R.id.callSign)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_flight, parent, false)

        return FlightViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        val flight = flights[position]

        val departureAt = Date(flight.firstSeen * 1000)
        val arrivedAt = Date(flight.lastSeen * 1000)
        val diffInSeconds = flight.lastSeen - flight.firstSeen
        val hours = diffInSeconds / 3600
        val minutes = (diffInSeconds % 3600) / 60
        val time = "$hours h: $minutes m"

        val format = SimpleDateFormat("dd/MM/yyyy\n HH:mm:ss") // Définir le format de la date

        val formattedDateDeparture = format.format(departureAt)
        val formattedDateArrived = format.format(arrivedAt)

        // Affectez ici les autres données à vos vues
        holder.departureDate.text = formattedDateDeparture
        holder.departureAirport.text = flight.estDepartureAirport
        holder.timeToTravel.text = time
        holder.arrivedDate.text = formattedDateArrived
        holder.arrivedAirport.text = flight.estArrivalAirport
        holder.callSign.text = flight.callsign

        holder.itemView.setOnClickListener(){
            listener.onFlightClick(flight)
        }
    }

    override fun getItemCount(): Int {
        return flights.size
    }

    fun updateFlights(newFlights: List<FlightModel>) {
        flights = newFlights
        notifyDataSetChanged()
    }

}
