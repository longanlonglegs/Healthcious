package com.pa1.logan.Healthcious.ui.composables.recipe

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.database.showImg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Food(navController: NavController?, recipe: Recipe) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())},
                navigationIcon = { IconButton(onClick = {}) {Icon(Icons.AutoMirrored.Default.ArrowBack, "go back")} },
                actions = {
                    IconButton(onClick = {
                        TODO("star a recipe")
                    }) {

                        Icon(Icons.Default.Favorite, "save this recipe")

                    }
                }
            )
        },
        content = {
            paddingValues ->
            ItemScreen(paddingValues, recipe)
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    onClick = {
                        TODO()
                    },
                    icon = { Icon(Icons.Default.ShoppingCart, "eat")},
                    selected = true,
                )
            }
        }
    )
}

@Composable
fun ItemScreen(paddingValues: PaddingValues, recipe: Recipe) {
    Column (
        Modifier
            .padding(paddingValues)
            .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)
    ){

        Image(
            painter = showImg("images/${recipe.name}.png"),
            "recipe image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
        )

        Column {
            Text(
                recipe.cuisine,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            Text(
                recipe.name,
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
                recipe.instructions,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
            Text("Allergens: ${ recipe.allergens }", color = Color.Red)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FoodPreview() {
    AppTheme {
        Food(navController = null, recipe = Recipe("CheeseBurger", 1200f, 12f, 20f, "add cheese to burger", listOf("egg", "beef"), listOf("cheese", "burger"), "american"))
    }
}