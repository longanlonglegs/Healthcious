package com.pa1.logan.Healthcious.ui.composables.misc

import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.google.firebase.auth.EmailAuthProvider
import com.pa1.logan.Healthcious.VM.Goals
import com.pa1.logan.Healthcious.database.fetchUserGoals
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.database.writeUserGoals
import com.pa1.logan.Healthcious.ui.composables.MainActivity
import com.pa1.logan.Healthcious.ui.composables.MainPage
import com.pa1.logan.Healthcious.ui.composables.recipe.ItemScreen
import org.checkerframework.checker.units.qual.C

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(navController: NavController?) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = { IconButton(onClick = {
                    navController?.navigate("main")
                }) { Icon(Icons.AutoMirrored.Default.ArrowBack, "go back") } },
            )
        },
        content = {
                paddingValues ->
            AccountInfo(paddingValues)
        },
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AccountInfo(paddingValues: PaddingValues) {

    var userGoals by remember { mutableStateOf(Goals()) }
    var user = getCurrentUser()

    val openAlertDialog = remember { mutableStateOf(false) }

    val openGoalDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit){
        fetchUserGoals(
            onDataReceived = {
                userGoals = it
            },
            onFailure = {}
        )
    }

    LazyColumn(Modifier
        .padding(paddingValues)
        .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {

        stickyHeader {
            Text(text = "Preferences", fontWeight = FontWeight.Bold, fontSize = 25.sp)
        }

        item{
            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                if (MainActivity.dark.value)  Icon(Icons.Default.DarkMode, "dark mode", Modifier.size(75.dp))
                else Icon(Icons.Default.LightMode, "light mode", Modifier.size(75.dp))

                Switch(
                    checked = MainActivity.dark.value,
                    onCheckedChange = {
                        MainActivity.dark.value = it
                    }
                )
            }
        }

        item { HorizontalDivider(thickness = 5.dp, modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)) }

        stickyHeader {
            Text(text = "Goals", fontWeight = FontWeight.Bold, fontSize = 25.sp)
        }

        item{
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                Icon(Icons.Default.LocalFireDepartment, "goal", Modifier.size(75.dp))

                Column() {
                    Text("Click to update Goal", modifier = Modifier.clickable{ openGoalDialog.value = true }, color = Color.Blue)
                }
            }
        }


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

    if (openGoalDialog.value) {
        ChangeGoalDialog(
            onDismiss = { openGoalDialog.value = false },
            onGoalChange = { goal ->
                writeUserGoals(
                    goal, onResult = {success, message ->
                        if (success) {
                            userGoals = goal
                            Toast.makeText(context, "Goal saved! ${goal}", Toast.LENGTH_SHORT).show()
                        }
                        else message?.let { Log.d("err writing goal", it) }
                    }
                )
            }
        ) 
    }
}

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onPasswordChange: (new: String) -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (newPassword != confirmPassword) {
                        errorText = "Passwords do not match"
                    } else if (newPassword.length < 6) {
                        errorText = "Password should be at least 6 characters"
                    } else {
                        errorText = null
                        onPasswordChange(newPassword)
                        onDismiss()
                    }
                }
            ) {
                Text("Change Password")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text("Change Password")
        },
        text = {
            Column {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )
                if (errorText != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(errorText!!, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    )
}


@Composable
fun ChangeGoalDialog(
    onDismiss: () -> Unit,
    onGoalChange: (goals: Goals) -> Unit
) {
    var newGoal by remember { mutableStateOf(Goals()) }
    var errorText by remember { mutableStateOf<String?>(null) }

    var cal by remember { mutableStateOf("Enter Calories (kCal)") }
    var salt by remember { mutableStateOf("Enter salt (mg)") }
    var sugar by remember { mutableStateOf("Enter sugar (g)") }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    try {

                        if (cal.toFloat() < 0 || salt.toFloat() < 0 || sugar.toFloat() < 0) Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
                        else newGoal = Goals(cal.toFloat(), sugar.toFloat(), salt.toFloat())
                    }
                    catch(e: Exception) {Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()}
                    onGoalChange(newGoal)
                    onDismiss()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text("Change Goal")
        },
        text = {
            Column {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = cal,
                    onValueChange = { cal = it },
                    label = { Text("Calories") },
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = salt,
                    onValueChange = { salt = it },
                    label = { Text("Salt") },
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = sugar,
                    onValueChange = { sugar = it },
                    label = { Text("Sugar") },
                    singleLine = true
                )
                if (errorText != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(errorText!!, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    AppTheme {
        Settings(navController = null)
    }
}
