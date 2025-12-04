package com.example.sherpalink

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListScreen() {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        Text("This is the List Screen", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(12.dp))

    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListScreenPreview() {
    ListScreen()
}
