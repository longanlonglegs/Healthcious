package com.pa1.logan.Healthcious

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Customize(navController: NavController?) {

    Scaffold(
        topBar = { TopAppBar(
            title = {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        "Your Recipe",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

            },

            navigationIcon = { IconButton(
                onClick = {
                    TODO("dropdown menu")
                }
            ){
                Icon(Icons.Default.Menu, "menu")
            }},

            actions = {
                IconButton(
                    onClick = {
                        TODO("sign into account")
                    }
                ){
                    Icon(Icons.Default.AccountCircle, "account")
                }
            }
        )},

        content = {
                paddingValues ->
            Custom(paddingValues, navController)
        },

    )
}

fun checkFloat(string: String) : Float{

    if (string.toFloatOrNull() != null && string.toFloat() >= 0f) {
        return string.toFloat()
    }

    return -1f
}

@Composable
fun Custom(paddingValues: PaddingValues, navController: NavController?) {

    var dish by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var salt by remember { mutableStateOf("") }
    var sugar by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column (
        Modifier
            .padding(paddingValues)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){

        Image(painter = painterResource(R.drawable.logo), "insert img")
        TextField(
            value = dish,
            onValueChange = {dish = it},
            Modifier.fillMaxWidth(),
            label = { Text("Dish") },
            singleLine = true,
            supportingText = { Text("Enter your dish name")}
            )

        TextField(
            value = description,
            onValueChange = {description = it},
            Modifier.fillMaxWidth(),
            label = { Text("Description") },
            singleLine = true,
            supportingText = { Text("Enter a brief description")}
        )

        TextField(
            value = calories,
            onValueChange = {calories = it},
            Modifier.fillMaxWidth(),
            label = { Text("Calories") },
            singleLine = true,
            supportingText = { Text("Enter the total caloric amount")}
        )

        TextField(
            value = sugar,
            onValueChange = {sugar = it},
            Modifier.fillMaxWidth(),
            label = { Text("Sugar") },
            singleLine = true,
            supportingText = { Text("Enter the total sugar content")}
        )

        TextField(
            value = salt,
            onValueChange = {salt = it},
            Modifier.fillMaxWidth(),
            label = { Text("Salt") },
            singleLine = true,
            supportingText = { Text("Enter the total salt content")}
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        )
        {
            Button(
                onClick = {
                    SaveDish(navController)
                    Toast.makeText(context, "Dish has been saved!", Toast.LENGTH_SHORT).show()
                          },
                ) {
                Text("Save")
            }

            Button(
                onClick = {
                    DeleteDish(navController)
                    Toast.makeText(context, "Dish has been deleted!", Toast.LENGTH_SHORT).show()
                },

            ) {
                Text("Delete")
            }
        }

    }
}

fun SaveDish(navController: NavController?) {
    navController?.navigate("main")
    }

fun DeleteDish(navController: NavController?){
    navController?.navigate("main")
    }

@Preview(showBackground = true)
@Composable
fun CustomizePreview() {
    AppTheme {
        Customize(navController = null)
    }
}

