package com.example.dnmcformsandroid.views

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormListScreen(navController: NavController) {
    val availableForms = listOf("200-form.json", "all-fields.json")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Available Forms") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Select a form:", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(availableForms) { formName ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                val encodedFormName = Uri.encode(formName) // ✅ Encode special characters
                                navController.navigate("sectionList/$encodedFormName") // ✅ Ensure correct routing
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = formName.removeSuffix(".json"), // ✅ Remove .json for cleaner UI
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}