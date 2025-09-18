// app/src/main/java/co/edu/udea/compumovil/gr01_20252/lab1/Country.kt
package co.edu.udea.compumovil.gr01_20252.lab1

data class Country(
    val id: Int = 0,
    val name: String = "",
    val iso2: String = "",
    val iso3: String = ""
) {
    override fun toString(): String = name
}