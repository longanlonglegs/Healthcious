package com.pa1.logan.Healthcious.ui.composables.misc

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.compose.AppTheme
import com.google.android.play.integrity.internal.l
import com.pa1.logan.Healthcious.R
import com.pa1.logan.Healthcious.VM.Purchases
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.database.uploadPurchaseImg
import com.pa1.logan.Healthcious.database.uploadRecipeImg
import com.pa1.logan.Healthcious.database.writeRecipe
import com.pa1.logan.Healthcious.database.writeUserPurchase
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
                        "Your Dish",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

            },

            navigationIcon = { IconButton(
                onClick = {
                    navController?.navigate("main")
                }
            ){
                Icon(Icons.AutoMirrored.Default.ArrowBack, "back")
            }},

            actions = {
                IconButton(
                    onClick = {
                        navController?.navigate("signin")
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Custom(paddingValues: PaddingValues, navController: NavController?) {

    var store by remember { mutableStateOf("") }
    var dish by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var allergen by remember { mutableStateOf("") }
    var allergenList by remember { mutableStateOf(listOf<String>()) }
    var ingredientList by remember { mutableStateOf(listOf<String>()) }
    var ingredients by remember { mutableStateOf("") }
    var cuisine by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var salt by remember { mutableStateOf("") }
    var sugar by remember { mutableStateOf("") }
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }
    var canUpload by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Recipe", "Purchase")

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LazyColumn (
        Modifier
            .padding(paddingValues)
            .padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){

        item {
            Row (Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SingleChoiceSegmentedButtonRow (
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                ){
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = { selectedIndex = index },
                            selected = index == selectedIndex,
                            label = { Text(label) }
                        )
                    }
                }
            }
        }

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
            } ?: Image(painter = painterResource(R.drawable.logo), "add image")
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
            )
        }

        item {

            TextField(
                value = description,
                onValueChange = { description = it },
                Modifier.fillMaxWidth(),
                label = { Text("Description") },
                singleLine = true,
            )
        }

        item {
            TextField(
                value = calories,
                onValueChange = {
                    calories = it
                                },
                Modifier.fillMaxWidth(),
                label = { Text("Calories (kCal)") },
                singleLine = true,
            )
        }

        item {
            TextField(
                value = sugar,
                onValueChange = {
                    sugar = it

                },
                Modifier.fillMaxWidth(),
                label = { Text("Sugar (g)") },
                singleLine = true,
            )
        }

        item {
            TextField(
                value = salt,
                onValueChange = {
                    salt = it
                },
                Modifier.fillMaxWidth(),
                label = { Text("Salt (mg)") },
                singleLine = true,
            )
        }

        if (selectedIndex == 0) {

            item {
                TextField(
                    value = ingredients,
                    onValueChange = { ingredients = it },
                    Modifier.fillMaxWidth(),
                    label = { Text("Ingredients, comma separated") },
                    singleLine = true,
                )
            }

            item {

                TextField(
                    value = allergen,
                    onValueChange = { allergen = it },
                    Modifier.fillMaxWidth(),
                    label = { Text("Allergen, comma separated") },
                    singleLine = true,
                )
            }

            item {
                TextField(
                    value = cuisine,
                    onValueChange = { cuisine = it },
                    Modifier.fillMaxWidth(),
                    label = { Text("Cuisine") },
                    singleLine = true,
                )
            }
        }

        else {
            item {
                TextField(
                    value = store,
                    onValueChange = { store = it },
                    Modifier.fillMaxWidth(),
                    label = { Text("Store") },
                    singleLine = true,
                )
            }
        }

        item {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            )
            {
                Button(
                    modifier = Modifier.width(125.dp),

                    onClick = {

                        if (sugar.length > 8 || salt.length > 8 || calories.length > 8) Toast.makeText(context, "Input Invalid", Toast.LENGTH_SHORT).show()

                        else try {

                            if (canUpload) {

                                if (getCurrentUser() != null) {
                                    if (selectedIndex == 0) {

                                        uploadRecipeImg(dish, imageUri!!, onUploadSuccess = { url ->
                                            uploadedImageUrl = url

                                        })

                                        allergenList = allergen.split("[ ]*,[ ]*".toRegex())
                                        ingredientList = ingredients.split("[ ]*,[ ]*".toRegex())

                                        if (salt.toFloat() < 0 && calories.toFloat() < 0 && sugar.toFloat() < 0) Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()

                                        else {
                                            writeUserRecipe(Recipe(
                                                dish,
                                                calories.toFloat(),
                                                salt.toFloat(),
                                                sugar.toFloat(),
                                                description,
                                                allergenList,
                                                ingredientList,
                                                cuisine
                                            ),
                                                onResult = { success, message ->
                                                    if (success) {
                                                        Toast.makeText(
                                                            context,
                                                            "Upload Successful",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        navController?.navigate("main")
                                                    } else Toast.makeText(
                                                        context,
                                                        message,
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                }
                                            )

                                            Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT)
                                                .show()
                                            navController?.navigate("main")
                                        }


                                    } else {

                                        if (salt.toFloat() < 0 && calories.toFloat() < 0 && sugar.toFloat() < 0) Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()

                                        else {

                                            uploadPurchaseImg(
                                                dish,
                                                imageUri!!,
                                                onUploadSuccess = { url ->
                                                    uploadedImageUrl = url

                                                })

                                            writeUserPurchase(Purchases(
                                                dish,
                                                calories.toFloat(),
                                                salt.toFloat(),
                                                sugar.toFloat(),
                                                store
                                            ),
                                                onResult = { success, message ->
                                                    if (success) {
                                                        Toast.makeText(
                                                            context,
                                                            "Upload Successful",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        navController?.navigate("main")
                                                    } else Toast.makeText(
                                                        context,
                                                        message,
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                }
                                            )

                                            Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT)
                                                .show()
                                            navController?.navigate("main")
                                        }
                                    }
                                } else Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT)
                                    .show()

                            }

                            else Toast.makeText(context, "Please upload a picture first", Toast.LENGTH_SHORT).show()
                        }

                        catch (e: Exception) {Toast.makeText(context, "Please input all the fields, including image correctly", Toast.LENGTH_SHORT).show()}
                        canUpload = false
                    },

                ) {
                    Text("Save")
                }

                Button(
                    onClick = {
                        Toast.makeText(context, "Dish has been deleted!", Toast.LENGTH_SHORT).show()
                        navController?.navigate("main")
                    },

                    modifier = Modifier.width(125.dp)

                    ) {
                    Text("Delete")
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun CustomizePreview() {
    AppTheme {
        Customize(navController = null)}
}

