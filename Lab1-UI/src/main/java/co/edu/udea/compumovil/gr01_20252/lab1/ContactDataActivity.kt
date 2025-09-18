package co.edu.udea.compumovil.gr01_20252.lab1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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
import android.util.Patterns

class ContactDataActivity : ComponentActivity() {
    private val viewModel: ContactDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactDataScreen(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDataScreen(viewModel: ContactDataViewModel? = null) {
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
    var paisSeleccionadoIso by rememberSaveable { mutableStateOf("") }

    // Estados para dropdowns
    var expandedPais by remember { mutableStateOf(false) }
    var expandedCiudad by remember { mutableStateOf(false) }

    // Campos obligatorios para el usuario o sino muestra advertencia
    var telefonoError by remember { mutableStateOf(false) }
    var emailRequiredError by remember { mutableStateOf(false) }
    var emailFormatError by remember { mutableStateOf(false) }
    var paisError by remember { mutableStateOf(false) }

    // Estados del ViewModel
    val countries by viewModel?.countries?.collectAsState() ?: remember { mutableStateOf(emptyList<Country>()) }
    val cities by viewModel?.cities?.collectAsState() ?: remember { mutableStateOf(emptyList<City>()) }
    val isLoadingCountries by viewModel?.isLoadingCountries?.collectAsState() ?: remember { mutableStateOf(false) }
    val isLoadingCities by viewModel?.isLoadingCities?.collectAsState() ?: remember { mutableStateOf(false) }
    val errorMessage by viewModel?.errorMessage?.collectAsState() ?: remember { mutableStateOf<String?>(null) }


    // Focus management
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Focus requesters para cada campo
    val telefonoFocusRequester = remember { FocusRequester() }
    val direccionFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }

    // Efecto para cargar ciudades cuando cambia el país
    LaunchedEffect(paisSeleccionadoIso) {
        if (paisSeleccionadoIso.isNotEmpty()) {
            ciudadSeleccionada = "" // Limpiar ciudad seleccionada
            viewModel?.loadCities(paisSeleccionadoIso)
        }
    }

    fun isValidEmail(email: String): Boolean {
        // true si coincide con el patrón de email estándar de Android
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

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
        when {
            email.isBlank() -> {
                errores.add("Email es obligatorio")
                emailRequiredError = true
                emailFormatError = false
                datosValidos = false
            }
            !isValidEmail(email) -> {
                errores.add("Email no es válido")
                emailRequiredError = false
                emailFormatError = true
                datosValidos = false
            }
            else -> {
                emailRequiredError = false
                emailFormatError = false
            }
        }

        if (paisSeleccionado.isBlank()) {
            errores.add("País es obligatorio")
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

        // Mostrar mensaje de error si existe
        errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { viewModel?.clearError() }) {
                        Text("OK")
                    }
                }
            }
        }

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
            onValueChange = { newValue ->
                email = newValue
                when {
                    newValue.isBlank() -> {
                        emailRequiredError = true
                        emailFormatError = false
                    }
                    !isValidEmail(newValue) -> {
                        emailRequiredError = false
                        emailFormatError = true
                    }
                    else -> {
                        emailRequiredError = false
                        emailFormatError = false
                    }
                }
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
            isError = emailRequiredError || emailFormatError
        )


        if (emailRequiredError || emailFormatError) {
            val message = when {
                emailRequiredError -> stringResource(R.string.required_field_error)
                emailFormatError -> stringResource(R.string.validation_email_error)
                else -> stringResource(R.string.required_field_error)
            }
            Text(
                text = message,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // País (Dropdown con API) - Obligatorio
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.country_label),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            if (isLoadingCountries) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            }
        }

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
                    paisError = false
                },
                label = { Text(stringResource(R.string.select_country)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPais)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                isError = paisError,
                readOnly = false
            )

            if (expandedPais && countries.isNotEmpty()) {
                val paisesFiltrados = if (paisSeleccionado.isBlank()) {
                    countries
                } else {
                    countries.filter {
                        it.name.contains(paisSeleccionado, ignoreCase = true)
                    }
                }

                ExposedDropdownMenu(
                    expanded = expandedPais && paisesFiltrados.isNotEmpty(),
                    onDismissRequest = {
                        expandedPais = false
                        if (paisSeleccionado.isNotBlank() &&
                            !countries.any { it.name.equals(paisSeleccionado, ignoreCase = true) }
                        ) {
                            paisError = true
                        } else {
                            paisError = false
                        }
                    }
                ) {
                    paisesFiltrados.forEach { pais ->
                        DropdownMenuItem(
                            text = { Text(pais.name) },
                            onClick = {
                                paisSeleccionado = pais.name
                                paisSeleccionadoIso = pais.iso2
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

        // Ciudad (Dropdown con API)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.city_label),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            if (isLoadingCities) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            }
        }

        ExposedDropdownMenuBox(
            expanded = expandedCiudad,
            onExpandedChange = {
                if (cities.isNotEmpty()) {
                    expandedCiudad = it
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = ciudadSeleccionada,
                onValueChange = { newValue ->
                    ciudadSeleccionada = newValue
                    if (cities.isNotEmpty()) {
                        expandedCiudad = true
                    }
                },
                label = {
                    Text(
                        if (paisSeleccionadoIso.isEmpty()) stringResource(R.string.country_not_selected)
                        else if (cities.isEmpty() && !isLoadingCities) stringResource(R.string.cities_empty)
                        else stringResource(R.string.select_city)
                    )
                },
                trailingIcon = {
                    if (cities.isNotEmpty()) {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCiudad)
                    }
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
                ),
                enabled = cities.isNotEmpty() && !isLoadingCities,
                readOnly = false
            )

            if (expandedCiudad && cities.isNotEmpty()) {
                val ciudadesFiltradas = if (ciudadSeleccionada.isBlank()) {
                    cities
                } else {
                    cities.filter {
                        it.name.contains(ciudadSeleccionada, ignoreCase = true)
                    }
                }

                ExposedDropdownMenu(
                    expanded = expandedCiudad && ciudadesFiltradas.isNotEmpty(),
                    onDismissRequest = { expandedCiudad = false }
                ) {
                    ciudadesFiltradas.forEach { ciudad ->
                        DropdownMenuItem(
                            text = { Text(ciudad.name) },
                            onClick = {
                                ciudadSeleccionada = ciudad.name
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
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoadingCountries && !isLoadingCities
        ) {
            Text(stringResource(R.string.next_button))
        }

        // Información para el desarrollador
        Text(
            text = "Nota: Los datos se muestran en Logcat cuando presionas 'Siguiente'. Los países y ciudades se cargan desde la API.",
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