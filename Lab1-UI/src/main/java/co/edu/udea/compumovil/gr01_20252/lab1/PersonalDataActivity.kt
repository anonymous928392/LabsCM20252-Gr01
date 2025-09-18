package co.edu.udea.compumovil.gr01_20252.lab1

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.*

class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonalDataScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen() {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Estados para los campos
    var nombres by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var sexoSeleccionado by rememberSaveable { mutableStateOf("") }
    val defaultFecha = stringResource(R.string.select_date)
    var fechaNacimiento by rememberSaveable { mutableStateOf(defaultFecha) }
    var gradoEscolaridad by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Validaciones
    var nombresError by remember { mutableStateOf(false) }
    var apellidosError by remember { mutableStateOf(false) }
    var birthError by remember { mutableStateOf(false) }
    var missingFields by remember { mutableStateOf(listOf<String>()) }
    var showMissingDialog by remember { mutableStateOf(false) }

    val opcionesSexo = listOf(
        stringResource(R.string.sex_male),
        stringResource(R.string.sex_female),
        stringResource(R.string.sex_other)
    )

    val opcionesEscolaridad = listOf(
        stringResource(R.string.education_primary),
        stringResource(R.string.education_secondary),
        stringResource(R.string.education_high_school),
        stringResource(R.string.education_technical),
        stringResource(R.string.education_technological),
        stringResource(R.string.education_university),
        stringResource(R.string.education_specialization),
        stringResource(R.string.education_masters),
        stringResource(R.string.education_doctorate)
    )

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val nombresFocusRequester = remember { FocusRequester() }
    val apellidosFocusRequester = remember { FocusRequester() }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fechaNacimiento = "$dayOfMonth/${month + 1}/$year"
            birthError = false
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Función de validación
    fun validarYContinuar() {
        val missing = mutableListOf<String>()

        nombresError = nombres.isBlank()
        if (nombresError) missing.add(context.getString(R.string.names_label))

        apellidosError = apellidos.isBlank()
        if (apellidosError) missing.add(context.getString(R.string.surnames_label))

        birthError = (fechaNacimiento == defaultFecha)
        if (birthError) missing.add(context.getString(R.string.birth_date_label))

        if (missing.isNotEmpty()) {
            missingFields = missing
            showMissingDialog = true
            return
        }

        // Log de información personal
        Log.d("PersonalData", "=== INFORMACIÓN PERSONAL ===")
        Log.d("PersonalData", "$nombres $apellidos")
        if (sexoSeleccionado.isNotEmpty()) {
            Log.d("PersonalData", sexoSeleccionado)
        }
        Log.d("PersonalData", "Nació el $fechaNacimiento")
        if (gradoEscolaridad.isNotEmpty()) {
            Log.d("PersonalData", gradoEscolaridad)
        }
        Log.d("PersonalData", "==============================")

        // Pasar datos a ContactDataActivity
        val intent = Intent(context, ContactDataActivity::class.java)
        intent.putExtra("nombres", nombres)
        intent.putExtra("apellidos", apellidos)
        intent.putExtra("sexo", sexoSeleccionado)
        intent.putExtra("fechaNacimiento", fechaNacimiento)
        intent.putExtra("gradoEscolaridad", gradoEscolaridad)
        context.startActivity(intent)
    }

    if (isLandscape) {
        // LAYOUT LANDSCAPE: DOS COLUMNAS
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Text(
                text = stringResource(R.string.personal_data_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // COLUMNA IZQUIERDA
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Nombres
                    OutlinedTextField(
                        value = nombres,
                        onValueChange = {
                            nombres = it
                            nombresError = nombres.isBlank()
                        },
                        label = { Text(stringResource(R.string.names_label)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(nombresFocusRequester),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words,
                            autoCorrect = false,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { apellidosFocusRequester.requestFocus() }
                        ),
                        singleLine = true,
                        isError = nombresError
                    )

                    if (nombresError) {
                        Text(
                            text = stringResource(R.string.required_field_error),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Sexo
                    Text(
                        text = stringResource(R.string.sex_label),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Column {
                        opcionesSexo.forEach { opcion ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (sexoSeleccionado == opcion),
                                        onClick = { sexoSeleccionado = opcion }
                                    )
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (sexoSeleccionado == opcion),
                                    onClick = { sexoSeleccionado = opcion }
                                )
                                Text(
                                    text = opcion,
                                    modifier = Modifier.padding(start = 5.dp),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                // COLUMNA DERECHA
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Apellidos
                    OutlinedTextField(
                        value = apellidos,
                        onValueChange = {
                            apellidos = it
                            apellidosError = apellidos.isBlank()
                        },
                        label = { Text(stringResource(R.string.surnames_label)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(apellidosFocusRequester),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words,
                            autoCorrect = false,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        ),
                        singleLine = true,
                        isError = apellidosError
                    )

                    if (apellidosError) {
                        Text(
                            text = stringResource(R.string.required_field_error),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Fecha de nacimiento
                    Text(
                        text = stringResource(R.string.birth_date_label),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Button(
                        onClick = { datePickerDialog.show() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = fechaNacimiento, fontSize = 14.sp)
                    }

                    if (birthError) {
                        Text(
                            text = stringResource(R.string.required_field_error),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Grado de escolaridad
                    Text(
                        text = stringResource(R.string.select_education),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = gradoEscolaridad,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.education_level_label)) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            opcionesEscolaridad.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion, fontSize = 14.sp) },
                                    onClick = {
                                        gradoEscolaridad = opcion
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Siguiente
            Button(
                onClick = { validarYContinuar() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.next_button))
            }
        }
    } else {
        // LAYOUT PORTRAIT: UNA COLUMNA
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.personal_data_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 1.dp, top = 20.dp)
            )

            // Nombres
            OutlinedTextField(
                value = nombres,
                onValueChange = {
                    nombres = it
                    nombresError = nombres.isBlank()
                },
                label = { Text(stringResource(R.string.names_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(nombresFocusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { apellidosFocusRequester.requestFocus() }
                ),
                singleLine = true,
                isError = nombresError
            )

            if (nombresError) {
                Text(
                    text = stringResource(R.string.required_field_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Apellidos
            OutlinedTextField(
                value = apellidos,
                onValueChange = {
                    apellidos = it
                    apellidosError = apellidos.isBlank()
                },
                label = { Text(stringResource(R.string.surnames_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(apellidosFocusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
                singleLine = true,
                isError = apellidosError
            )

            if (apellidosError) {
                Text(
                    text = stringResource(R.string.required_field_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Sexo
            Text(
                text = stringResource(R.string.sex_label),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Column {
                opcionesSexo.forEach { opcion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (sexoSeleccionado == opcion),
                                onClick = { sexoSeleccionado = opcion }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (sexoSeleccionado == opcion),
                            onClick = { sexoSeleccionado = opcion }
                        )
                        Text(
                            text = opcion,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
            }

            // Fecha de nacimiento
            Text(
                text = stringResource(R.string.birth_date_label),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Button(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = fechaNacimiento)
            }

            if (birthError) {
                Text(
                    text = stringResource(R.string.required_field_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Grado de escolaridad
            Text(
                text = stringResource(R.string.select_education),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = gradoEscolaridad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.education_level_label)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    opcionesEscolaridad.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                gradoEscolaridad = opcion
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón Siguiente
            Button(
                onClick = { validarYContinuar() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.next_button))
            }
        }
    }

    // Diálogo de campos faltantes
    if (showMissingDialog) {
        Dialog(onDismissRequest = { showMissingDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.missing_fields_error),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )



                    missingFields.forEach { field ->
                        Text(
                            text = "• $field",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { showMissingDialog = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.next_button))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDataPreview() {
    PersonalDataScreen()
}