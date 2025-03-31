package com.pa1.logan.Healthcious.ui.composables.recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.google.gson.Gson
import com.pa1.logan.Healthcious.R
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.database.showImg
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun Recipe(navController: NavController?, recipe: Recipe) {

    Card (
        modifier = Modifier
            .fillMaxSize(),

        onClick = {
            val jsonRecipe = Gson().toJson(recipe)
            val encodedRecipe = URLEncoder.encode(jsonRecipe, StandardCharsets.UTF_8.toString())

            navController?.navigate("food/$encodedRecipe")
        }
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Image(
                 painter = showImg("images/${recipe.name}.png"),
                 contentDescription = "Firebase Image",
                 modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .drawWithContent {
                       drawContent()
                       drawRect(
                          brush = Brush.verticalGradient(
                             colors = listOf(
                                Color.Transparent, // Fully transparent at the bottom
                                Color.Black // Light transparency at the top
                             )
                          )
                       )
                    },
                 contentScale = ContentScale.Crop,

              )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 5.dp, bottom = 5.dp)
            ) {
                Text(
                    recipe.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 5.dp)){
                    Text(
                        "${recipe.calories.toInt()}kcal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 20.sp,
                        color = Color.White
                    )


                    Text(recipe.allergens.toString(), modifier = Modifier.padding(5.dp), color = Color.White)

                    Text(recipe.cuisine, color = Color.White)

                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun RecipePreview() {
    AppTheme {
        Recipe(navController = null, Recipe("CheeseBurger", 1200f, 12f, 20f, "add cheese to burger", listOf("egg", "beef"), listOf("cheese", "burger"), "american"))
    }
}

