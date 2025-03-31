package com.pa1.logan.Healthcious.ui.composables.recipe

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.VM.RecipeVM
import com.pa1.logan.Healthcious.database.fetchRecipes
import com.pa1.logan.Healthcious.ui.composables.MainPage

@Composable
fun RecipeList(navController: NavController?, paddingValues: PaddingValues) {

    var search by remember { mutableStateOf("") }
    val recipeVM = RecipeVM()


    LaunchedEffect(Unit) {
        fetchRecipes(
            onDataReceived = {
                recipeVM.recipeList.value = it
                Log.d("RecipeList", "Fetched ${it.size} recipes")
                             },
            onFailure = { }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues)
    )
    {
        Searchbar(search)

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {

            for (recipe in recipeVM.recipeList.value) {
                item {
                    Recipe(navController, recipe)
                }
            }

        }
    }
}

@Composable
fun Searchbar(search: String) {
    var search by remember { mutableStateOf("") }
    TextField(value = search, onValueChange = {search = it},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search by keyword...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
    )
}

@Preview(showBackground = true)
@Composable
fun RecipeListPreview() {
    AppTheme {
        RecipeList(navController = null, PaddingValues(5.dp))
    }
}