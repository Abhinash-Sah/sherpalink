package com.example.sherpalink.screens.admin

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

// ✅ CORRECT IMPORT (matches your SignInActivity)
import com.example.sherpalink.auth.SignInActivity

@Composable
fun AdminDashboardScreen(
    navController: NavHostController
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ---------- HEADER ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Admin Dashboard",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(context, SignInActivity::class.java).apply {
                        addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    }
                    context.startActivity(intent)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.Red
                )
            }
        }

        Divider()

        // ---------- TOUR MANAGEMENT ----------
        Text(
            text = "Tour Packages",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("admin_upload") },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Add New Tour")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("admin_list") },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Manage Tours")
        }

        Divider(modifier = Modifier.padding(vertical = 12.dp))

        // ---------- GUIDE MANAGEMENT ----------
        Text(
            text = "Guide Management",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("admin_guide_upload") },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Add New Guide")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("admin_guide_list") },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Manage Guides")
        }

        Spacer(modifier = Modifier.weight(1f))

        // ---------- BOTTOM LOGOUT ----------
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(context, SignInActivity::class.java).apply {
                    addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                }
                context.startActivity(intent)
            },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.Red),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Red
            )
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Logout Admin Session")
        }
    }
}
