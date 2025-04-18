package com.pa1.logan.Healthcious.ui.composables.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.work.WorkManager
import com.example.compose.AppTheme
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.VM.RecipeVM
import com.pa1.logan.Healthcious.database.fetchRecipes
import com.pa1.logan.Healthcious.database.fetchUserRecipes
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.ui.composables.MainPage
import com.pa1.logan.Healthcious.ui.composables.misc.FilterChip
import com.pa1.logan.Healthcious.ui.composables.misc.Searchbar
import com.pa1.logan.Healthcious.ui.composables.misc.StylishSearchBar

@Composable
fun RecipeList(navController: NavController?) {

    var search by remember { mutableStateOf("") }
    val recipeVM = remember { RecipeVM() }

    var filteredList by remember { mutableStateOf(listOf<Recipe>()) }

    LaunchedEffect(Unit) {

        fetchUserRecipes(
            onDataReceived = {
                recipeVM.recipeList.value = it + recipeVM.recipeList.value
                Log.d("RecipeList", "Fetched ${it.size} recipes")
            },
            onFailure = { }
        )

        fetchRecipes(
            onDataReceived = {
                recipeVM.recipeList.value = it + recipeVM.recipeList.value
                Log.d("RecipeList", "Fetched ${it.size} recipes")
            },
            onFailure = { }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    )
    {

        StylishSearchBar(
            query = search,
            onQueryChange = { search = it }
        )


        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {

            filteredList = recipeVM.recipeList.value.filter { element ->
                element.name.contains(search, ignoreCase = true)
            }

            for (recipe in filteredList) {
                item {
                    Recipe(navController, recipe)
                }
            }
        }
    }
}

@Composable
fun ObserveWorkManager(context: Context) {
    val workInfo by WorkManager.getInstance(context)
        .getWorkInfosForUniqueWorkLiveData("StreakWorker")
        .observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        workInfo.firstOrNull()?.let {
            Log.d("WorkManager", "Worker state: ${it.state}")
        }
    }

    Text(text = "WorkManager State: ${workInfo.firstOrNull()?.state ?: "Unknown"}")
}