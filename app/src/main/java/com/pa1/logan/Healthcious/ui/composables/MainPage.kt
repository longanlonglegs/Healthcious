package com.pa1.logan.Healthcious.ui.composables

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RunningWithErrors
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.work.WorkManager
import com.example.compose.AppTheme
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.database.signOut
import com.pa1.logan.Healthcious.ui.composables.recipe.RecipeList

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavController?) {

    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(
            title = {

                    Text(
                        "Hi ${getCurrentUser()?.email.toString().substringBefore("@")}!",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.SemiBold
                    )
                
                    },

            navigationIcon = {
                IconButton(
                    onClick = {
                        navController?.navigate("signup")
                    }
                ){
                    Icon(Icons.Default.AccountCircle, "sign up")
                }
            },

            actions = {
                MinimalDropdownMenu(navController)
            }
        )},

        content = {
            paddingValues ->
            RecipeList(navController, paddingValues)
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController?.navigate("customize")
                }
            ) {
                Icon(Icons.Default.Add, "Add own dish")
            }
        },
        bottomBar = {
            NavigationBar (modifier = Modifier.height(80.dp), containerColor = Color.Transparent){
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("main")
                    },
                    icon = { Icon(Icons.Default.Book, "Recipe")},
                    selected = true,
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("purchase")
                    },
                    icon = { Icon(Icons.Default.Storefront, "Purchase")},
                    selected = false,
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("health")
                    },
                    icon = { Icon(Icons.Default.RunningWithErrors, "Health")},
                    selected = false,
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("shoppingcart")
                    },
                    icon = { Icon(Icons.Default.Fastfood, "Shopping Cart")},
                    selected = false,
                )
            }
        }
    )

}

@Composable
fun MinimalDropdownMenu(navController: NavController?) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.Menu, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Settings") },
                onClick = {
                    TODO()
                }
            )
            DropdownMenuItem(
                text = { Text("Logout") },
                onClick = {
                    signOut()
                    Toast.makeText(navController?.context, "Logged out", Toast.LENGTH_SHORT).show()
                    navController?.navigate("signin")
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    AppTheme {
        MainPage(navController = null)
    }
}

