package com.pa1.logan.Healthcious.ui.composables.recipe

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.pa1.logan.Healthcious.R
import com.pa1.logan.Healthcious.VM.Purchases
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.VM.shoppingCartItem
import com.pa1.logan.Healthcious.database.fetchRecipes
import com.pa1.logan.Healthcious.database.fetchUserEatenFood
import com.pa1.logan.Healthcious.database.showImg
import com.pa1.logan.Healthcious.database.writeUserEatenFood
import com.pa1.logan.Healthcious.database.writeUserRecipe
import com.pa1.logan.Healthcious.ui.composables.purchase.CounterCircleButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Food(navController: NavController?, recipe: Recipe) {

    val context = LocalContext.current
    var newRecipe by remember { mutableStateOf(recipe) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe")},
                navigationIcon = { IconButton(onClick = {
                    navController?.navigate("main")
                }) {Icon(Icons.AutoMirrored.Default.ArrowBack, "go back")} },
            )
        },
        content = {
            paddingValues ->
            ItemScreen(paddingValues, recipe, navController)
        },
    )
}

@Composable
fun ItemScreen(paddingValues: PaddingValues, recipe: Recipe, navController: NavController?) {

    val name = recipe.name.replace("+", " ")
    val instructions = recipe.instructions.replace("+", " ")
    var eatenList by remember { mutableStateOf(listOf<shoppingCartItem>()) }
    val context = LocalContext.current
    var quantity by remember { mutableIntStateOf(1) }
    var allergens by remember { mutableStateOf(listOf<String>()) }

    for (allergen in recipe.allergens) allergens += allergen.replace("+", " ")

    LaunchedEffect (Unit){
        fetchUserEatenFood(

            onDataReceived = {
                eatenList = it
                Log.d("Eaten Food list", "Fetched $it Eaten Food")
            },
            onFailure = {}

        )
    }

    Column (
        Modifier
            .padding(paddingValues)
            .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)
    ){

        Image(
            painter = showImg("images/recipes/${name}.png"),
            "recipe image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
        )

        Column {
            Text(
                recipe.cuisine.replace("+", " "),
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
                    "${recipe.calories.toInt()}kcal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    modifier = (Modifier.padding(5.dp))
                )
            }

            Card {
                Text(
                    "${recipe.salt.toInt()}g salt", fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = (Modifier.padding(5.dp))
                )
            }

            Card {
                Text(
                    "${recipe.sugar.toInt()}g sugar", fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = (Modifier.padding(5.dp))
                )
            }

        }

        Column (Modifier.padding(horizontal = 10.dp), ){
            Text(
                instructions,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
            Text("Allergens: ${ recipe.allergens.toString().replace("+", " ") }", color = Color.Red)
        }

        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center)
        {

            Text("Quantity: ")

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(64.dp))
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer
                    )
            ) {
                CounterCircleButton("-", onClick = {
                    if (quantity > 0) quantity-- else Toast.makeText(context, "You can't eat less than 0!", Toast.LENGTH_SHORT).show()
                })
                Text(
                    text = quantity.toString(),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .animateContentSize()
                        .padding(horizontal = 12.dp)
                )
                CounterCircleButton("+", onClick = { quantity++ })
            }
        }

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom){
            FilledTonalButton(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    for (food in eatenList) {
                        if (food.name == recipe.name && food.purchases == null) {
                            quantity += food.quantity
                        }
                    }

                    writeUserEatenFood(
                        shoppingCartItem(
                            recipe.name,
                            recipe.calories,
                            recipe.salt,
                            recipe.sugar,
                            quantity = quantity,
                            recipes = recipe,
                        ),
                        onResult = { success, message ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Om Nom! Yummy in my tummy!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController?.navigate("main")
                            } else {
                                Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                Log.d("Dish eating button err", message.toString())
                            }
                        }
                    )
                },
            )
            {
                Text("Eat Now!", fontSize = 20.sp)
            }
        }
    }
}