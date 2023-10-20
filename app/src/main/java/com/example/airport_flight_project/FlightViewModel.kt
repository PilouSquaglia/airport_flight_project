package com.example.airport_flight_project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class FlightViewModel : ViewModel() {
    // LiveData pour stocker le contenu JSON
    private val _jsonContent = MutableLiveData<String>()
    val jsonContent: LiveData<String>
        get() = _jsonContent

    // Méthode pour mettre à jour le contenu JSON
    fun updateJsonContent(content: String) {
        _jsonContent.value = content
    }
}
