// app/src/main/java/co/edu/udea/compumovil/gr01_20252/lab1/ContactDataViewModel.kt
package co.edu.udea.compumovil.gr01_20252.lab1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactDataViewModel : ViewModel() {
    private val apiKey = "UVpNZVRpdVd0UmFGZXV3ak9tZHR0bks1REdodnQ1a0w2MWpDajA4Ng=="

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    private val _isLoadingCountries = MutableStateFlow(false)
    val isLoadingCountries: StateFlow<Boolean> = _isLoadingCountries

    private val _isLoadingCities = MutableStateFlow(false)
    val isLoadingCities: StateFlow<Boolean> = _isLoadingCities

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadCountries()
    }

    fun loadCountries() {
        viewModelScope.launch {
            _isLoadingCountries.value = true
            _errorMessage.value = null

            try {
                val response = ApiClient.apiService.getCountries(apiKey)
                if (response.isSuccessful) {
                    _countries.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Error al cargar países: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoadingCountries.value = false
            }
        }
    }

    fun loadCities(countryCode: String) {
        if (countryCode.isBlank()) {
            _cities.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isLoadingCities.value = true
            _errorMessage.value = null

            try {
                val response = ApiClient.apiService.getCities(apiKey, countryCode)
                if (response.isSuccessful) {
                    _cities.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Error al cargar ciudades: ${response.code()}"
                    _cities.value = emptyList()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
                _cities.value = emptyList()
            } finally {
                _isLoadingCities.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}