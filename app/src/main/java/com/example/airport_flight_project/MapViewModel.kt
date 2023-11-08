package com.example.airport_flight_project

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel : ViewModel(){
    private val BASE_URL = "https://opensky-network.org/api/tracks/all?icao24="

    private val _flight = MutableLiveData<FlightModel>()
    private val _travel = MutableLiveData<FlightTravelModel>()

    fun setFlightLiveData(flight: FlightModel){
         _flight.value = flight
    }
    fun getFlightLiveData():LiveData<FlightModel>{
        return _flight
    }

    fun getFlightTravelLiveData():LiveData<FlightTravelModel>{
        return _travel
    }

    fun requestFlightList(context: Context){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = RequestManager.getSuspended(
                    "$BASE_URL${_flight.value!!.icao24}&time=0",
                    HashMap())

                Log.i("REQUEST", result.toString())
                if (result != null) {
                    Log.i("REQUEST", result)

                    val flightList = ArrayList<FlightModel>()
                    val parser = JsonParser()
                    val jsonElement = parser.parse(result)
                    val data: FlightTravelModel =
                        Gson().fromJson(jsonElement, FlightTravelModel::class.java)
                    _travel.postValue(data)
                    Log.i("Res", result)


                } else {
                    Log.e("REQUEST", "ERROR NO RESULT")
                    val jsonFile = Utils.readJsonFromAssets(context = context, "flight.json")
                    val data: FlightTravelModel =
                        Gson().fromJson(jsonFile, FlightTravelModel::class.java)
                    _travel.postValue(data)

                }
            }
        }
    }
}