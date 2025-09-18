// app/src/main/java/co/edu/udea/compumovil/gr01_20252/lab1/City.kt
package co.edu.udea.compumovil.gr01_20252.lab1

data class City(
    val id: Int = 0,
    val name: String = ""
) {
    override fun toString(): String = name
}