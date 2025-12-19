package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardRoot()
        }
    }
}

@Composable
fun DashboardRoot() {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        // ✅ HEADER ON ALL SCREENS
        AppHeader()

        // ✅ Screen change area
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> LocationScreen()
                2 -> AddScreen()
                3 -> ListScreen()
                4 -> ProfileScreen()
            }
        }

        BottomMenuBar(selectedTab) { selectedTab = it }
    }
}


@Composable
fun BottomMenuBar(selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(containerColor = Color.White) {

        val icons = listOf(
            Icons.Default.Home,
            Icons.Default.LocationOn,
            Icons.Default.AddCircle,
            Icons.Default.List,
            Icons.Default.Person
        )


        icons.forEachIndexed { index, icon ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                icon = { Icon(icon, contentDescription = null) }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardRoot()
}