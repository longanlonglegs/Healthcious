package com.pa1.logan.Healthcious.VM

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RecipeVM: ViewModel() {

    var recipeList = mutableStateOf(listOf<Recipe>())

}

data class Recipe(
    val name: String = "",
    val calories: Float = 0f,
    val sugar: Float = 0f,
    val salt: Float = 0f,
    var instructions: String = "",
    val allergens: List<String> = emptyList(),
    val ingredients: List<String> = emptyList(),
    val cuisine: String = "",
    )