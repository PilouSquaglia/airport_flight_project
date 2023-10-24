package com.example.airport_flight_project

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class FlightViewModel : ViewModel() {
    // LiveData pour stocker le contenu JSON
    private val _jsonContent = MutableLiveData<List<FlightModel>>()
    val jsonContent: LiveData<List<FlightModel>>
        get() = _jsonContent

    // Méthode pour mettre à jour le contenu JSON
    fun updateJsonContent(content: String) {
        val gson = Gson()
        try {

            val flightInfo: List<FlightModel> = gson.fromJson(content, object : TypeToken<List<FlightModel>>() {}.type)

            _jsonContent.value = flightInfo
        } catch (e: JsonSyntaxException) {
            Log.e("FlightViewModel", "Erreur lors de la désérialisation du JSON", e)
        }
    }
}
