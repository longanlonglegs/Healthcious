package com.pa1.logan.Healthcious.ui.composables.misc

import android.accounts.Account
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pa1.logan.Healthcious.VM.Goals
import com.pa1.logan.Healthcious.database.fetchUserGoals
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.database.writeUserGoals
import com.pa1.logan.Healthcious.ui.composables.MainActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Account(navController: NavController?) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account") },
                navigationIcon = { IconButton(onClick = {
                    navController?.navigate("main")
                }) { Icon(Icons.AutoMirrored.Default.ArrowBack, "go back") } },
            )
        },
        content = {
                paddingValues ->
            AccountPage(paddingValues)
        },
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AccountPage(paddingValues: PaddingValues) {

    var user = getCurrentUser()

    val openAlertDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column (
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(10.dp)
        ,
    verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Icon(Icons.Default.AccountBox, "account", Modifier.size(200.dp))

        Text(text = "Email: ${user?.email}")

        Text("Click to update password", modifier = Modifier.clickable{ openAlertDialog.value = true }, color = Color.Blue)

    }

    if (openAlertDialog.value) {
        ChangePasswordDialog(
            onDismiss = { openAlertDialog.value = false },
            onPasswordChange = { new ->
                // Handle Firebase password update here
                user?.updatePassword(new)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Password updated!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, task.exception?.message ?: "Failed to update password", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        )
    }
}
