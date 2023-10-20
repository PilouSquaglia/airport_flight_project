package com.example.airport_flight_project

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap

class MainViewModel : ViewModel() {

    private val beginDateLiveData = MutableLiveData<Calendar>(Calendar.getInstance())
    private val endDateLiveData = MutableLiveData<Calendar>(Calendar.getInstance())

    private val airportListLiveData = MutableLiveData<List<Airport>>()
    private val airportListNamesLiveData = MutableLiveData<List<String>>()

    init{
        val airportList = Utils.generateAirportList()
        airportListLiveData.value = airportList
        val airportNamesList = ArrayList<String>()
        //Populate the list of airport names
        for (airport in airportList) {
            airportNamesList.add(airport.getFormattedName())
        }
        airportListNamesLiveData.value = airportNamesList
    }

    enum class DateType {
        BEGIN, END
    }

    fun getBeginDateLiveData(): LiveData<Calendar>{
        return beginDateLiveData
    }

    fun getEndDateLiveData(): LiveData<Calendar>{
        return endDateLiveData
    }

    fun updateCalendarLiveData(dateType: DateType, calendar: Calendar){
        if(dateType == DateType.BEGIN) beginDateLiveData.value = calendar else endDateLiveData.value = calendar
    }

    fun getAirportNamesListLiveData():LiveData<List<String>>{
        return airportListNamesLiveData
    }

    fun getAirportListLiveData():LiveData<List<Airport>>{
        return airportListLiveData
    }



    fun requestFlightList(isArrival: Boolean, selectedAirportIndex: Int){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                //TODO faire requête
                val url = if(isArrival) RequestManager.FLIGHT_ARRIVAL_ENDPOINT else RequestManager.FLIGHT_DEPARTURE_ENDPOINT
                val params = HashMap<String, String>().apply {
                    put("airport",airportListLiveData.value!![selectedAirportIndex].icao)
                    put("begin", (beginDateLiveData.value!!.timeInMillis / 1000).toString())
                    put("end", (endDateLiveData.value!!.timeInMillis / 1000).toString())
                }
                RequestManager.getSuspended(url, params)
            }
            if (result != null) {
                Log.i("REQUEST", result)

                val flightList = ArrayList<FlightModel>()
                val parser = JsonParser()
                val jsonElement = parser.parse(result)

                for (flightObject in jsonElement.asJsonArray) {
                    flightList.add(Gson().fromJson(flightObject.asJsonObject, FlightModel::class.java))
                }

                //setFlightListLiveData(flightList)
                // Equivalent à
                //flightListLiveData.value =  flightList
                Log.i("Res", result)

            } else {
                Log.e("REQUEST", "ERROR NO RESULT")
            }

            result?.let{
                Log.i("Result", result)
            }
        }
    }

//    suspend fun requestFlightList(isArrival: Boolean, selectedAirportIndex: Int): String? {
//        return viewModelScope.async {
//            val url = if (isArrival) RequestManager.FLIGHT_ARRIVAL_ENDPOINT else RequestManager.FLIGHT_DEPARTURE_ENDPOINT
//            val params = HashMap<String, String>().apply {
//                put("airport", airportListLiveData.value!![selectedAirportIndex].icao)
//                put("begin", (beginDateLiveData.value!!.timeInMillis / 1000).toString())
//                put("end", (endDateLiveData.value!!.timeInMillis / 1000).toString())
//            }
//            val result = RequestManager.getSuspended(url, params)
//            result
//        }.await()
//    }

}