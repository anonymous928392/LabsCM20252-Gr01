package co.edu.udea.compumovil.gr01_20252.lab1
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ContactDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactDataScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDataScreen() {
    // Estados que se mantienen en cambios de configuración
    var telefono by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var paisSeleccionado by rememberSaveable { mutableStateOf("") }
    var ciudadSeleccionada by rememberSaveable { mutableStateOf("") }

    // Estados para dropdowns
    var expandedPais by remember { mutableStateOf(false) }
    var expandedCiudad by remember { mutableStateOf(false) }


    val paisesLatam = listOf(
        stringResource(R.string.country_colombia),
        stringResource(R.string.country_argentina),
        stringResource(R.string.country_chile),
        stringResource(R.string.country_mexico),
        stringResource(R.string.country_peru),
        stringResource(R.string.country_ecuador),
        stringResource(R.string.country_bolivia),
        stringResource(R.string.country_paraguay),
        stringResource(R.string.country_uruguay),
        stringResource(R.string.country_brasil),
        stringResource(R.string.country_venezuela),
        stringResource(R.string.country_costa_rica),
        stringResource(R.string.country_panama),
        stringResource(R.string.country_el_salvador),
        stringResource(R.string.country_honduras),
        stringResource(R.string.country_nicaragua),
        stringResource(R.string.country_guatemala),
        stringResource(R.string.country_cuba),
        stringResource(R.string.country_dominican_republic)
        )



    // Ciudades principales de Colombia
    val ciudadesColombia = listOf(
        "Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena",
        "Cúcuta", "Bucaramanga", "Pereira", "Santa Marta", "Ibagué",
        "Pasto", "Manizales", "Neiva", "Villavicencio", "Armenia",
        "Valledupar", "Montería", "Sincelejo", "Popayán", "Tunja"
    )

    // Focus management
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Focus requesters para cada campo
    val telefonoFocusRequester = remember { FocusRequester() }
    val direccionFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }

    // Función de validación y logging
    fun validarYMostrarDatos() {
        var datosValidos = true
        val errores = mutableListOf<String>()

        // Validar campos obligatorios
        if (telefono.isBlank()) {
            errores.add("Teléfono es obligatorio")
            datosValidos = false
        }
        if (email.isBlank()) {
            errores.add("Email es obligatorio")
            datosValidos = false
        }
        if (paisSeleccionado.isBlank()) {
            errores.add("País es obligatorio")
            datosValidos = false
        }

        if (datosValidos) {
            // Log de datos válidos
            Log.d("ContactData", "=== DATOS DE CONTACTO ===")
            Log.d("ContactData", "Teléfono: $telefono")
            Log.d("ContactData", "Dirección: ${if (direccion.isBlank()) "No proporcionada" else direccion}")
            Log.d("ContactData", "Email: $email")
            Log.d("ContactData", "País: $paisSeleccionado")
            Log.d("ContactData", "Ciudad: ${if (ciudadSeleccionada.isBlank()) "No proporcionada" else ciudadSeleccionada}")
            Log.d("ContactData", "========================")
        } else {
            // Log de errores
            Log.w("ContactData", "Errores de validación: ${errores.joinToString(", ")}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = stringResource(R.string.contact_data_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 1.dp , top = 20.dp)
        )

        // Campo Teléfono (Obligatorio)
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text(stringResource(R.string.phone_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(telefonoFocusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    direccionFocusRequester.requestFocus()
                }
            ),
            singleLine = true
        )

        // Campo Dirección
        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text(stringResource(R.string.address_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(direccionFocusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    emailFocusRequester.requestFocus()
                }
            ),
            singleLine = true
        )

        // Campo Email (Obligatorio)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.clearFocus()
                    expandedPais = true
                }
            ),
            singleLine = true
        )

        // País (Autocomplete/Dropdown) - Obligatorio
        Text(
            text = stringResource(R.string.country_label),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        ExposedDropdownMenuBox(
            expanded = expandedPais,
            onExpandedChange = { expandedPais = !expandedPais },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = paisSeleccionado,
                onValueChange = {
                    paisSeleccionado = it
                    expandedPais = it.isNotEmpty()
                },
                label = { Text(stringResource(R.string.select_country)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPais)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

            ExposedDropdownMenu(
                expanded = expandedPais,
                onDismissRequest = { expandedPais = false }
            ) {
                paisesLatam.filter {
                    it.contains(paisSeleccionado, ignoreCase = true)
                }.forEach { pais ->
                    DropdownMenuItem(
                        text = { Text(pais) },
                        onClick = {
                            paisSeleccionado = pais
                            expandedPais = false
                        }
                    )
                }
            }
        }

        // Ciudad (Autocomplete/Dropdown)
        Text(
            text = stringResource(R.string.city_label),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        ExposedDropdownMenuBox(
            expanded = expandedCiudad,
            onExpandedChange = { expandedCiudad = !expandedCiudad },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = ciudadSeleccionada,
                onValueChange = {
                    ciudadSeleccionada = it
                    expandedCiudad = it.isNotEmpty()
                },
                label = { Text(stringResource(R.string.select_city)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCiudad)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        validarYMostrarDatos()
                    }
                )
            )

            ExposedDropdownMenu(
                expanded = expandedCiudad,
                onDismissRequest = { expandedCiudad = false }
            ) {
                ciudadesColombia.filter {
                    it.contains(ciudadSeleccionada, ignoreCase = true)
                }.forEach { ciudad ->
                    DropdownMenuItem(
                        text = { Text(ciudad) },
                        onClick = {
                            ciudadSeleccionada = ciudad
                            expandedCiudad = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón Siguiente
        Button(
            onClick = { validarYMostrarDatos() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.next_button))
        }

        // Información para el desarrollador
        Text(
            text = "Nota: Los datos se muestran en Logcat cuando presionas 'Siguiente'",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}



@Preview(showBackground = true)
@Composable
fun ContactDataPreview() {
    ContactDataScreen()
}
