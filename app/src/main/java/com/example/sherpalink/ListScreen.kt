package com.example.sherpalink.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ListScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("List Screen", fontSize = 24.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListScreenPreview() {
    ListScreen()
}
