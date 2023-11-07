package com.example.airport_flight_project

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlightViewModel : ViewModel() {
    // LiveData pour stocker le contenu JSON
    private val jsonContent = MutableLiveData<List<FlightModel>>()
    private val BASE_URL = "https://opensky-network.org/api"
    private val REQUEST_DEPART_URL = BASE_URL+"/flights/departure"
    private val REQUEST_ARRIVE_URL = BASE_URL+"/flights/arrival"

    //val jsonContent: LiveData<List<FlightModel>>
      //  get() = _jsonContent

    fun getFlightListLiveData():LiveData<List<FlightModel>>{
        return jsonContent
    }
    // Méthode pour mettre à jour le contenu JSON
    fun updateJsonContent(content: String) {
        try {
            val flightArray: Array<FlightModel> = Gson().fromJson(content, Array<FlightModel>::class.java)
            val flightList: List<FlightModel> = flightArray.toList()

            //val flightInfo: List<FlightModel> = gson.fromJson(content, Array<FlightModel>::class.java)
            //jsonContent.value = flightList
        } catch (e: JsonSyntaxException) {
            Log.e("FlightViewModel", "Erreur lors de la désérialisation du JSON", e)
        }
    }

    fun requestFlightList(airport: String,depart: String, arrivee: String,airportSwitch: Boolean, context: Context){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                //TODO faire requête
                val req = if(airportSwitch) REQUEST_ARRIVE_URL else REQUEST_DEPART_URL
                val result = RequestManager.getSuspended(
                    "$req?airport=$airport&begin=$depart&end=$arrivee",
                    HashMap())
//                val url =
//                    if (airportSwitch) RequestManager.FLIGHT_ARRIVAL_ENDPOINT else RequestManager.FLIGHT_DEPARTURE_ENDPOINT
//                val params = HashMap<String, String>().apply {
//                    put("end", arrivee)
//                    put("begin", depart)
//                    put("airport", airport)
//                }
//                val result = RequestManager.getSuspended(url, params)

                Log.i("REQUEST", result.toString())
                if (result != null) {
                    Log.i("REQUEST", result)

                    val flightList = ArrayList<FlightModel>()
                    val parser = JsonParser()
                    val jsonElement = parser.parse(result)

                    /*for (flightObject in jsonElement.asJsonArray) {
                        flightList.add(
                            Gson().fromJson(
                                flightObject.asJsonObject,
                                FlightModel::class.java
                            )
                        )
                    }*/
                    val data: Array<FlightModel> =
                        Gson().fromJson(jsonElement, Array<FlightModel>::class.java)
                    jsonContent.postValue(data.toList())
                    // setFlightListLiveData(flightList)
                    // Equivalent à
                    // flightListLiveData.value = flightList
                    Log.i("Res", result)

                } else {
                    Log.e("REQUEST", "ERROR NO RESULT")
                    val jsonFile = Utils.readJsonFromAssets(context = context, "mock.json")
                    //val reader = FileReader(jsonFile)

                    val data: Array<FlightModel> =
                        Gson().fromJson(jsonFile, Array<FlightModel>::class.java)
                    jsonContent.postValue(data.toList())
                    Log.d("REQUEST", data.toList().toString())
                }
            }
        }
    }
}
