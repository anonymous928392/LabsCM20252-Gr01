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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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

    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    // Recibir datos de PersonalDataActivity
    val nombres = activity?.intent?.getStringExtra("nombres") ?: ""
    val apellidos = activity?.intent?.getStringExtra("apellidos") ?: ""
    val sexo = activity?.intent?.getStringExtra("sexo") ?: ""
    val fechaNacimiento = activity?.intent?.getStringExtra("fechaNacimiento") ?: ""
    val gradoEscolaridad = activity?.intent?.getStringExtra("gradoEscolaridad") ?: ""

    // Estados que se mantienen en cambios de configuración
    var telefono by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var paisSeleccionado by rememberSaveable { mutableStateOf("") }
    var ciudadSeleccionada by rememberSaveable { mutableStateOf("") }

    // Estados para dropdowns
    var expandedPais by remember { mutableStateOf(false) }
    var expandedCiudad by remember { mutableStateOf(false) }

    // Campos obligatorios para el usuario o sino muestra advertencia
    var telefonoError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var paisError by remember { mutableStateOf(false) }

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
            telefonoError = true
            datosValidos = false
        }
        if (email.isBlank()) {
            errores.add("Email es obligatorio")
            emailError = true
            datosValidos = false
        }
        if (paisSeleccionado.isBlank()) {
            errores.add("País es obligatorio")
            paisError = true
            datosValidos = false
        }

        if (!paisesLatam.any { it.equals(paisSeleccionado, ignoreCase = true) }) {
            errores.add("País seleccionado no es válido")
            paisError = true
            datosValidos = false
        }



        if (datosValidos) {
            // Log COMPLETO - Personal + Contacto
            Log.d("CompleteData", "=========================================")
            Log.d("CompleteData", "**Información personal:**")
            Log.d("CompleteData", "$nombres $apellidos")
            if (sexo.isNotEmpty()) {
                Log.d("CompleteData", sexo)
            }
            Log.d("CompleteData", "Nació el $fechaNacimiento")
            if (gradoEscolaridad.isNotEmpty()) {
                Log.d("CompleteData", gradoEscolaridad)
            }
            Log.d("CompleteData", "**Información de contacto:**")
            Log.d("CompleteData", "Teléfono: $telefono")
            if (direccion.isNotEmpty()) {
                Log.d("CompleteData", "Dirección: $direccion")
            }
            Log.d("CompleteData", "Email: $email")
            Log.d("CompleteData", "País: $paisSeleccionado")
            if (ciudadSeleccionada.isNotEmpty()) {
                Log.d("CompleteData", "Ciudad: $ciudadSeleccionada")
            }
            Log.d("CompleteData", "=========================================")
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
            modifier = Modifier.padding(bottom = 1.dp, top = 20.dp)
        )

        // Campo Teléfono (Obligatorio)
        OutlinedTextField(
            value = telefono,
            onValueChange = {
                val filtered = it.filter { char -> char.isDigit() }
                if (filtered.length <= 10) telefono = filtered
                telefonoError = telefono.isBlank()
            },
            label = { Text(stringResource(R.string.phone_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(telefonoFocusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { direccionFocusRequester.requestFocus() }
            ),
            singleLine = true,
            isError = telefonoError
        )

        if (telefonoError) {
            Text(
                text = stringResource(R.string.required_field_error),
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

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
            onValueChange = {
                email = it
                emailError = email.isBlank()
            },
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
            singleLine = true,
            isError = emailError
        )

        if (emailError) {
            Text(
                text = stringResource(R.string.required_field_error),
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // País (Autocomplete/Dropdown) - Obligatorio
        Text(
            text = stringResource(R.string.country_label),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        ExposedDropdownMenuBox(
            expanded = expandedPais,
            onExpandedChange = { expandedPais = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = paisSeleccionado,
                onValueChange = { newValue ->
                    paisSeleccionado = newValue
                    expandedPais = true
                    paisError = false // Reset error while typing
                },
                label = { Text(stringResource(R.string.select_country)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPais)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                isError = paisError
            )

            // Solo mostrar el dropdown si está expandido
            if (expandedPais) {
                val paisesFiltrados = if (paisSeleccionado.isBlank()) {
                    paisesLatam
                } else {
                    paisesLatam.filter {
                        it.contains(paisSeleccionado, ignoreCase = true)
                    }
                }

                ExposedDropdownMenu(
                    expanded = expandedPais && paisesFiltrados.isNotEmpty(),
                    onDismissRequest = {
                        expandedPais = false
                        // Validar solo si hay texto y no coincide con ningún país exactamente
                        if (paisSeleccionado.isNotBlank() &&
                            !paisesLatam.any { it.equals(paisSeleccionado, ignoreCase = true) }
                        ) {
                            paisError = true
                        } else {
                            paisError = false
                        }
                    }
                ) {
                    paisesFiltrados.forEach { pais ->
                        DropdownMenuItem(
                            text = { Text(pais) },
                            onClick = {
                                paisSeleccionado = pais
                                expandedPais = false
                                paisError = false
                            }
                        )
                    }
                }
            }
        }

        if (paisError) {
            Text(
                text = stringResource(R.string.required_field_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Ciudad (Autocomplete/Dropdown) - CORREGIDO IGUAL QUE PAÍS
        Text(
            text = stringResource(R.string.city_label),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        ExposedDropdownMenuBox(
            expanded = expandedCiudad,
            onExpandedChange = { expandedCiudad = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = ciudadSeleccionada,
                onValueChange = { newValue ->
                    ciudadSeleccionada = newValue
                    expandedCiudad = true
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

            // Solo mostrar el dropdown si está expandido
            if (expandedCiudad) {
                val ciudadesFiltradas = if (ciudadSeleccionada.isBlank()) {
                    ciudadesColombia
                } else {
                    ciudadesColombia.filter {
                        it.contains(ciudadSeleccionada, ignoreCase = true)
                    }
                }

                ExposedDropdownMenu(
                    expanded = expandedCiudad && ciudadesFiltradas.isNotEmpty(),
                    onDismissRequest = { expandedCiudad = false }
                ) {
                    ciudadesFiltradas.forEach { ciudad ->
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