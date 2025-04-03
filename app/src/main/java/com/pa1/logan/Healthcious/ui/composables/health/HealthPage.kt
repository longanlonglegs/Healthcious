package com.pa1.logan.Healthcious.ui.composables.health

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.RunningWithErrors
import androidx.compose.material.icons.filled.Storefront
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.ui.composables.MinimalDropdownMenu
import com.pa1.logan.Healthcious.ui.composables.recipe.RecipeList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthPage(navController: NavController?) {

    Scaffold(
        topBar = { TopAppBar(
            title = {

                Column(Modifier.fillMaxWidth()) {

                    Text(
                        "Health Progress",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            },

            navigationIcon = {
                MinimalDropdownMenu(navController)
            },

            actions = {
                IconButton(
                    onClick = {
                        navController?.navigate("signup")
                    }
                ){
                    Icon(Icons.Default.AccountCircle, "sign up")
                }
            }
        )
        },

        content = {
                paddingValues ->
            Health(navController, paddingValues)
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
                    icon = { Icon(Icons.Default.Book, "Recipe") },
                    selected = true,
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("purchase")
                    },
                    icon = { Icon(Icons.Default.Storefront, "Purchase") },
                    selected = false,
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("health")
                    },
                    icon = { Icon(Icons.Default.RunningWithErrors, "Health") },
                    selected = false,
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("shoppingcart")
                    },
                    icon = { Icon(Icons.Default.Fastfood, "Shopping Cart") },
                    selected = false,
                )
            }
        }
    )

}
