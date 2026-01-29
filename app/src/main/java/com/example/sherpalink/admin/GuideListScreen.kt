package com.example.sherpalink.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.sherpalink.model.GuideModel
import com.example.sherpalink.viewmodel.GuideViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminGuideListScreen(navController: NavHostController, guideViewModel: GuideViewModel) {
    val guides by guideViewModel.guides.observeAsState(emptyList())
    var guideToDelete by remember { mutableStateOf<GuideModel?>(null) }

    LaunchedEffect(Unit) { guideViewModel.loadGuides() }

    // Delete Confirmation
    if (guideToDelete != null) {
        AlertDialog(
            onDismissRequest = { guideToDelete = null },
            title = { Text("Confirm Delete") },
            text = { Text("Delete ${guideToDelete?.name} permanently?") },
            confirmButton = {
                TextButton(onClick = {
                    guideToDelete?.let { guideViewModel.deleteGuide(it.guideId) }
                    guideToDelete = null
                }) { Text("Delete", color = Color.Red) }
            },
            dismissButton = { TextButton(onClick = { guideToDelete = null }) { Text("Cancel") } }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Manage Guides") }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(guides) { guide ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = guide.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp).clip(CircleShape).background(Color.Gray),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(guide.name, style = MaterialTheme.typography.titleMedium)
                            Text(guide.specialty, style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { guideToDelete = guide }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}