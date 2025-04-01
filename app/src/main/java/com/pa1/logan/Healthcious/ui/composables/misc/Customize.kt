package com.pa1.logan.Healthcious.ui.composables.misc

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.compose.AppTheme
import com.pa1.logan.Healthcious.R
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.database.uploadImg
import com.pa1.logan.Healthcious.database.writeRecipe
import com.pa1.logan.Healthcious.database.writeUserRecipe

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

@Composable
fun Custom(paddingValues: PaddingValues, navController: NavController?) {

    var dish by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var salt by remember { mutableStateOf("") }
    var sugar by remember { mutableStateOf("") }
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }
    var canUpload by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LazyColumn (
        Modifier
            .padding(paddingValues)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){

        item {
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Uploaded Image",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                canUpload = true
            } ?: Text("No image uploaded yet", style = MaterialTheme.typography.bodyLarge)
        }

        item {
            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Pick an Image")
            }
        }

        item {
            TextField(
                value = dish,
                onValueChange = { dish = it },
                Modifier.fillMaxWidth(),
                label = { Text("Dish") },
                singleLine = true,
                supportingText = { Text("Enter your dish name") }
            )

            TextField(
                value = description,
                onValueChange = { description = it },
                Modifier.fillMaxWidth(),
                label = { Text("Description") },
                singleLine = true,
                supportingText = { Text("Enter a brief description") }
            )

            TextField(
                value = calories,
                onValueChange = { calories = it },
                Modifier.fillMaxWidth(),
                label = { Text("Calories") },
                singleLine = true,
                supportingText = { Text("Enter the total caloric amount") }
            )

            TextField(
                value = sugar,
                onValueChange = { sugar = it },
                Modifier.fillMaxWidth(),
                label = { Text("Sugar") },
                singleLine = true,
                supportingText = { Text("Enter the total sugar content") }
            )

            TextField(
                value = salt,
                onValueChange = { salt = it },
                Modifier.fillMaxWidth(),
                label = { Text("Salt") },
                singleLine = true,
                supportingText = { Text("Enter the total salt content") }
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            )
            {
                Button(
                    onClick = {
                        if (canUpload) {

                            uploadImg(dish, imageUri!!, onUploadSuccess = { url ->
                                uploadedImageUrl = url

                            })

                            if (getCurrentUser() != null) {
                                writeUserRecipe(Recipe(
                                    dish,
                                    calories.toFloat(),
                                    salt.toFloat(),
                                    sugar.toFloat(),
                                    description,
                                    listOf(),
                                    listOf(),
                                    ""
                                ),
                                    onResult = { success, message ->
                                        if (success) {
                                            Toast.makeText(
                                                context,
                                                "Upload Successful",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController?.navigate("main")
                                        } else Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                )

                                Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT)
                                    .show()
                                navController?.navigate("main")
                            }

                            else Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()

                        }
                        canUpload = false
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
}

fun DeleteDish(navController: NavController?){
    navController?.navigate("main")
    }

@Preview(showBackground = true)
@Composable
fun CustomizePreview() {
    AppTheme {
        Customize(navController = null)}
}

