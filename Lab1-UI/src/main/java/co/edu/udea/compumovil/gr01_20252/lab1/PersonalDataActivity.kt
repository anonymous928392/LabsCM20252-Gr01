package co.edu.udea.compumovil.gr01_20252.lab1

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    // Estados para los campos
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var sexoSeleccionado by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("Seleccionar fecha") }
    var gradoEscolaridad by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Opciones para los campos
    val opcionesSexo = listOf("Masculino", "Femenino", "Otro")
    val opcionesEscolaridad = listOf(
        "Primaria",
        "Secundaria",
        "Bachillerato",
        "Técnico",
        "Tecnológico",
        "Universitario",
        "Especialización",
        "Maestría",
        "Doctorado"
    )

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // DatePickerDialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fechaNacimiento = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Datos Personales",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo Nombres (Obligatorio)
        OutlinedTextField(
            value = nombres,
            onValueChange = { nombres = it },
            label = { Text("*Nombres") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false
            ),
            singleLine = true
        )

        // Campo Apellidos (Obligatorio)
        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("*Apellidos") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false
            ),
            singleLine = true
        )

        // Sexo (RadioButtons)
        Text(
            text = "Sexo",
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
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }


        Text(
            text = "*Fecha de nacimiento",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Button(
            onClick = { datePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = fechaNacimiento)
        }

        // Grado de escolaridad
        Text(
            text = "Grado de escolaridad",
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
                label = { Text("Seleccionar grado") },
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

        Button(
            onClick = {
                // Validar campos obligatorios
                if (nombres.isBlank()) {

                    return@Button
                }
                if (apellidos.isBlank()) {

                    return@Button
                }
                if (fechaNacimiento == "Seleccionar fecha") {

                    return@Button
                }


            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Datos")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDataPreview() {
    PersonalDataScreen()
}