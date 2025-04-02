package com.pa1.logan.Healthcious.ui.composables.purchase

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pa1.logan.Healthcious.VM.Purchases
import com.pa1.logan.Healthcious.database.showImg
import com.pa1.logan.Healthcious.database.writeUserPurchase
import com.pa1.logan.Healthcious.database.writeUserRecipe
import com.pa1.logan.Healthcious.ui.composables.recipe.ItemScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dish(navController: NavController?, purchases: Purchases) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Purchase", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                navigationIcon = { IconButton(onClick = {
                    navController?.navigate("purchase")
                }) { Icon(Icons.AutoMirrored.Default.ArrowBack, "go back") } },
                actions = {
                    IconButton(onClick = {
                        TODO("star a recipe")
                    }) {

                        Icon(Icons.Default.Favorite, "save this purchase")

                    }
                }
            )
        },
        content = {
                paddingValues ->
            DishScreen(paddingValues, purchases)
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    onClick = {
                        writeUserPurchase(purchases,
                            onResult = { success, message ->
                                if (success) {
                                    Toast.makeText(
                                        context,
                                        "Om Nom! Yummy in my tummy!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController?.navigate("purchase")
                                } else Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        )
                    },
                    icon = { Icon(Icons.Default.ShoppingCart, "eat") },
                    selected = true,
                )
            }
        }
    )
}


@Composable
fun DishScreen(paddingValues: PaddingValues, purchases: Purchases) {

    val name = purchases.name.replace("+", " ")

    Column (
        Modifier
            .padding(paddingValues)
            .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)
    ){

        Image(
            painter = showImg("images/purchases/${name}.png"),
            "recipe image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
        )

        Column {
            Text(
                "Purchase",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            Text(
                name,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 25.sp,
                lineHeight = 25.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card{
                Text(
                    "${purchases.calories.toInt()}kcal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    modifier = (Modifier.padding(5.dp))
                )
            }

            Card {
                Text(
                    "${purchases.salt.toInt()}g salt", fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = (Modifier.padding(5.dp))
                )
            }

            Card {
                Text(
                    "${purchases.sugar.toInt()}g sugar", fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = (Modifier.padding(5.dp))
                )
            }

        }
    }
}