package com.pa1.logan.Healthcious

import androidx.lifecycle.ViewModel

class RecipeVIewModel: ViewModel() {

    var recipeList = mutableListOf<Recipe>()

}

data class Recipe(val name: String, val calories: Float, val sugar: Float, val salt: Float, var instructions: String)