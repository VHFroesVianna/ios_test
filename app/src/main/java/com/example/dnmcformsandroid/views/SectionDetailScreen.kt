package com.example.dnmcformsandroid.views


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dnmcformsandroid.controllers.JSONLoader
import com.example.dnmcformsandroid.models.Section


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionDetailScreen(navController: NavController, sectionId: String) {
    val section = remember { mutableStateOf<Section?>(null) }

    LaunchedEffect(Unit) {
        val sections = JSONLoader.loadSections(navController.context)
        section.value = sections.find { it.uuid == sectionId }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(section.value?.title ?: "Loading...") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("addField/${section.value?.uuid}")
            }) {
                Text("+") // ✅ Floating button stays visible
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            section.value?.let { sec ->
                items(sec.fields) { field ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = "${field.label}: ${field.type}",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}