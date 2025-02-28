package com.example.dnmcformsandroid.views

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.dnmcformsandroid.models.Section
import com.example.dnmcformsandroid.controllers.JSONLoader
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionListScreen(navController: NavController) {
    val context = LocalContext.current
    var sections by remember { mutableStateOf(emptyList<Section>()) }

    LaunchedEffect(Unit) {
        sections = JSONLoader.loadSections(context)
        Log.d("SectionListScreen", "📌 Loaded ${sections.size} sections") // ✅ Debug log
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Sections") }) } // ✅ No more warning
    ) { paddingValues ->
        if (sections.isEmpty()) {
            Log.e("SectionListScreen", "❌ No sections loaded") // ✅ Log if empty
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No sections available", style = MaterialTheme.typography.titleLarge)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(sections.size) { index ->
                    SectionItem(section = sections[index], navController)
                }
            }
        }
    }
}

@Composable
fun SectionItem(section: Section, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("sectionDetail/${section.uuid}")
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = section.title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
