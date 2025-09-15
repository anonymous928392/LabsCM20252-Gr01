package co.edu.udea.compumovil.gr01_20252.lab1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
    var nombres by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var sexoSeleccionado by rememberSaveable { mutableStateOf("") }
    val defaultFecha = stringResource(R.string.select_date)
    var fechaNacimiento by rememberSaveable { mutableStateOf(defaultFecha) }
    var gradoEscolaridad by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }


    val opcionesSexo = listOf(stringResource(R.string.sex_male),
        stringResource(R.string.sex_female),
        stringResource(R.string.sex_other))

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
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {

        Text(
            text = stringResource(R.string.personal_data_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 1.dp , top = 20.dp)
        )


        OutlinedTextField(
            value = nombres,
            onValueChange = { nombres = it },
            label = { Text(stringResource(R.string.names_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false
            ),
            singleLine = true
        )


        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text(stringResource(R.string.surnames_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false
            ),
            singleLine = true
        )


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
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }


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

        Button(
            onClick = {

                if (nombres.isBlank()) {

                    return@Button
                }
                if (apellidos.isBlank()) {

                    return@Button
                }
                if (fechaNacimiento == "Seleccionar fecha") {

                    return@Button
                }

                // Navegar a ContactDataActivity
                val intent = Intent(context, ContactDataActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.next_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDataPreview() {
    PersonalDataScreen()
}

