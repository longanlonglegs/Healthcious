package com.pa1.logan.Healthcious.database

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.VM.RecipeVM

fun writeRecipe(recipe: Recipe) {

    val ref = Firebase.database.reference

    ref.child("recipes").child(recipe.name).setValue(recipe)
}

fun fetchRecipes(onDataReceived: (List<Recipe>) -> Unit, onFailure: (Exception) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("recipes") // Points to "recipes" node in Firebase

    ref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val recipesList = mutableListOf<Recipe>()
            for (recipeSnapshot in snapshot.children) {
                val recipe = recipeSnapshot.getValue(Recipe::class.java)
                recipe?.let { recipesList.add(it) }
            }
            onDataReceived(recipesList)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Firebase", "Error fetching data: ${error.message}")
            onFailure(error.toException())
        }
    })
}

fun writeUserRecipe(recipe: Recipe, onResult: (Boolean, String?) -> Unit) {

    val ref = Firebase.database.reference

    val user = getCurrentUser()

    if (user != null ) ref.child(user.email.toString().substringBefore("@")).child(recipe.name).setValue(recipe)
    else Log.d("writing recipe error", "error with writing recipe")
}

fun fetchUserRecipes(onDataReceived: (List<Recipe>) -> Unit, onFailure: (Exception) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val user = getCurrentUser()
    var ref = database.getReference("recipes")

    if (user != null) ref = database.getReference(user.email.toString().substringBefore("@")) // Points to recipes node in Firebase

    ref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val recipesList = mutableListOf<Recipe>()
            for (recipeSnapshot in snapshot.children) {
                val recipe = recipeSnapshot.getValue(Recipe::class.java)
                recipe?.let { recipesList.add(it) }
            }
            onDataReceived(recipesList)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Firebase", "Error fetching data: ${error.message}")
            onFailure(error.toException())
        }
    })
}

//@Composable
//fun RecipeListScreen() {
//    var recipes by remember { mutableStateOf(RecipeVM().recipeList) }
//
//    LaunchedEffect(Unit) {
//        fetchRecipes(
//            onDataReceived = { recipes = it.toMutableList() },
//            onFailure = { }
//        )
//    }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//
//        if (recipes.isEmpty()) {
//            Text("Loading recipes...", style = MaterialTheme.typography.bodyLarge)
//        } else {
//            LazyColumn {
//                items(recipes) { recipe ->
//                    Card(
//                        modifier = Modifier.fillMaxWidth().padding(8.dp),
//                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text(recipe.name, style = MaterialTheme.typography.titleMedium)
//                            Text("Price: $${recipe.ingredients}", style = MaterialTheme.typography.bodyLarge)
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

