package com.example.airport_flight_project

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel : ViewModel(){
    private val BASE_URL = "https://opensky-network.org/api/"
    private val REQUEST_TRACKS_URL = BASE_URL+ "tracks/all?icao24="
    private val REQUEST_STATE_URL = BASE_URL+"states/all"

    private val _flight = MutableLiveData<FlightModel>()
    private val _travel = MutableLiveData<FlightTravelModel>()
    private val _dataPath = MutableLiveData<ArrayList<Pair<Double, Double>>>()
    private val jsonContent = MutableLiveData<List<StateModel>>()
    private val _plane = MutableLiveData<StateModel>()

    fun setFlightLiveData(flight: FlightModel){
        _flight.value = flight
    }
    fun getFlightLiveData():LiveData<FlightModel>{
        return _flight
    }

    fun getFlightTravelLiveData():LiveData<FlightTravelModel>{
        return _travel
    }

    fun getFlightStateLiveData():LiveData<StateModel>{
        return _plane
    }

    fun setFlightDataPathLiveData(data: ArrayList<Pair<Double, Double>>){
        _dataPath.value = data
    }
    fun getFlightDataPathLiveData():LiveData<ArrayList<Pair<Double, Double>>>{
        return _dataPath
    }

    fun requestFlightList(context: Context){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = RequestManager.getSuspended(
                    "$REQUEST_TRACKS_URL${_flight.value!!.icao24}&time=0",
                    HashMap())

                Log.i("REQUEST", result.toString())
                if (result != null) {
                    Log.i("REQUEST", result)

                    val flightList = ArrayList<FlightModel>()
                    val parser = JsonParser()
                    val jsonElement = parser.parse(result)
                    try{
                        val data: FlightTravelModel =
                            Gson().fromJson(jsonElement, FlightTravelModel::class.java)
                        _travel.postValue(data)
                        Log.i("Res", result)
                    } catch (e: JsonSyntaxException) {
                        Log.e("REQUEST", "Error during deserialization: ${e.localizedMessage}")
                    }
                    requestPlanePosition(context)
                } else {
                    Log.e("REQUEST", "ERROR NO RESULT")
                    val jsonFile = Utils.readJsonFromAssets(context = context, "flight.json")
                    try{
                        val data: FlightTravelModel =
                            Gson().fromJson(jsonFile, FlightTravelModel::class.java)
                        _travel.postValue(data)
                    } catch (e: JsonSyntaxException) {
                        Log.e("REQUEST", "Error during deserialization: ${e.localizedMessage}")
                    }
                    // Afficher un Toast
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Une erreur s'est produite lors de la récupération des données voici le fichier de test", Toast.LENGTH_SHORT).show()
                    }

                    requestPlanePosition(context)
                }
            }
        }
    }

    suspend fun requestPlanePosition(context: Context){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val req = REQUEST_STATE_URL

                val result = RequestManager.getSuspended(
                    "$req?time=${_travel.value!!.startTime}&icao24=${_travel.value!!.icao24}",
                    HashMap())

                Log.i("REQUEST", result.toString())
                if (result != null) {
                    Log.i("REQUEST", result)

                    val flightList = ArrayList<StateModel>()
                    val parser = JsonParser()
                    val jsonElement = parser.parse(result)

                    try{
                        val data: Array<StateModel> =
                            Gson().fromJson(jsonElement, Array<StateModel>::class.java)
                        jsonContent.postValue(data.toList())
                    } catch (e: JsonSyntaxException) {
                        Log.e("REQUEST", "Error during deserialization: ${e.localizedMessage}")
                    }
                    Log.i("Res", result)

                } else {
                    Log.e("REQUEST", "ERROR NO RESULT")
                    val jsonFile = Utils.readJsonFromAssets(context = context, "position.json")

                    try {
                        val data: StateModel = Gson().fromJson(jsonFile, StateModel::class.java)
                        _plane.postValue(data)
                    } catch (e: JsonSyntaxException) {
                        Log.e("REQUEST", "Error during deserialization: ${e.localizedMessage}")
                    }


                }
            }
        }
    }
}