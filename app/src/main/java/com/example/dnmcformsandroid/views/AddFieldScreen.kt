package com.example.dnmcformsandroid.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dnmcformsandroid.controllers.JSONLoader
import com.example.dnmcformsandroid.controllers.JSONLoader.saveNewField
import com.example.dnmcformsandroid.models.FieldType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFieldScreen(navController: NavController, sectionId: String) {
    val context = LocalContext.current

    var fieldName by remember { mutableStateOf("") }
    var fieldLabel by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(FieldType.TEXT) }
    var isRequired by remember { mutableStateOf(false) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Field") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Field Name")
            BasicTextField(
                value = fieldName,
                onValueChange = { fieldName = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Field Label")
            BasicTextField(
                value = fieldLabel,
                onValueChange = { fieldLabel = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Field Type")
            Box {
                Button(onClick = { dropdownExpanded = true }) {
                    Text(selectedType.name)
                }
                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    FieldType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                selectedType = type
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Checkbox(
                    checked = isRequired,
                    onCheckedChange = { isRequired = it }
                )
                Text("Required")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    println("🛠 Navigating to sectionList...")
                    println("📌 Current routes: ${navController.graph.nodes}")
                    saveNewField(context, sectionId, fieldName, fieldLabel, selectedType, isRequired)
                    navController.popBackStack()
                    navController.navigate("sectionList")  // ✅ Use the correct route
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Field")
            }
        }
    }
}