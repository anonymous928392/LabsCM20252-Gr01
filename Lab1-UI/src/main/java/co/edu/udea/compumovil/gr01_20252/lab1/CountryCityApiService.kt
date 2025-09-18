// app/src/main/java/co/edu/udea/compumovil/gr01_20252/lab1/CountryCityApiService.kt
package co.edu.udea.compumovil.gr01_20252.lab1

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CountryCityApiService {
    @GET("countries")
    suspend fun getCountries(@Header("X-CSCAPI-KEY") apiKey: String): Response<List<Country>>

    @GET("countries/{ciso}/cities")
    suspend fun getCities(
        @Header("X-CSCAPI-KEY") apiKey: String,
        @Path("ciso") countryCode: String
    ): Response<List<City>>
}