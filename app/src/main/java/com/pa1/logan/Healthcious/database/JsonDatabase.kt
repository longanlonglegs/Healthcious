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
import com.pa1.logan.Healthcious.VM.Goals
import com.pa1.logan.Healthcious.VM.Purchases
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.VM.RecipeVM
import com.pa1.logan.Healthcious.VM.shoppingCartItem

fun writeRecipe(recipe: Recipe) {

    val ref = Firebase.database.reference

    ref.child("recipes").child(recipe.name).setValue(recipe)
}

fun writePurchase(purchases: Purchases) {

    val ref = Firebase.database.reference

    ref.child("purchases").child(purchases.name).setValue(purchases)
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

    if (user != null ) ref.child(user.email.toString().substringBefore("@")).child("recipes").child(recipe.name).setValue(recipe)
    else Log.d("writing recipe error", "error with writing recipe")
}

fun fetchUserRecipes(onDataReceived: (List<Recipe>) -> Unit, onFailure: (Exception) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val user = getCurrentUser()
    var ref = database.getReference("recipes")

    if (user != null) ref = database.getReference("${user.email.toString().substringBefore("@")}/recipes") // Points to recipes node in Firebase

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

fun fetchPurchases(onDataReceived: (List<Purchases>) -> Unit, onFailure: (Exception) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("purchases") // Points to "recipes" node in Firebase

    ref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val recipesList = mutableListOf<Purchases>()
            for (recipeSnapshot in snapshot.children) {
                val recipe = recipeSnapshot.getValue(Purchases::class.java)
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


fun writeUserPurchase(purchases: Purchases, onResult: (Boolean, String?) -> Unit) {

    val ref = Firebase.database.reference

    val user = getCurrentUser()

    if (user != null ) {
        ref.child(user.email.toString().substringBefore("@")).child("purchases")
            .child(purchases.name).setValue(purchases)
        onResult(true, "IT WORKS")
    }

    else {
        onResult(false, "error with writing purchase")
    }
}

fun fetchUserPurchases(onDataReceived: (List<Purchases>) -> Unit, onFailure: (Exception) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val user = getCurrentUser()
    var ref = database.getReference("purchases")

    if (user != null) ref = database.getReference("${user.email.toString().substringBefore("@")}/purchases") // Points to recipes node in Firebase

    ref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val purchaseList = mutableListOf<Purchases>()
            for (purchaseSnapshot in snapshot.children) {
                val purchase = purchaseSnapshot.getValue(Purchases::class.java)
                purchase?.let { purchaseList.add(it) }
            }
            onDataReceived(purchaseList)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Firebase", "Error fetching data: ${error.message}")
            onFailure(error.toException())
        }
    })
}

fun writeUserEatenFood(shoppingCartItem: shoppingCartItem, onResult: (Boolean, String?) -> Unit) {

    val ref = Firebase.database.reference

    val user = getCurrentUser()

    if (user != null ) {
        ref.child(user.email.toString().substringBefore("@")).child("eaten food")
            .child(shoppingCartItem.name).setValue(shoppingCartItem)
        onResult(true, "IT WORKS")
    }
    else {
        Log.d("writing purchases error", "error with writing purchases")
        onResult(false, "error with writing purchases")
    }
}

fun fetchUserEatenFood(onDataReceived: (List<shoppingCartItem>) -> Unit, onFailure: (Exception) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val user = getCurrentUser()
    var ref = database.getReference("purchases")

    if (user != null) ref = database.getReference("${user.email.toString().substringBefore("@")}/eaten food") // Points to recipes node in Firebase

    ref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val eatenFoodList = mutableListOf<shoppingCartItem>()
            for (eatenFoodSnapshot in snapshot.children) {
                val eatenFood = eatenFoodSnapshot.getValue(shoppingCartItem::class.java)
                eatenFood?.let { eatenFoodList.add(it) }
            }
            onDataReceived(eatenFoodList)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Firebase", "Error fetching data: ${error.message}")
            onFailure(error.toException())
        }
    })
}

fun writeUserGoals(goals: Goals, onResult: (Boolean, String?) -> Unit) {

    val ref = Firebase.database.reference

    val user = getCurrentUser()

    if (user != null ) {
        ref.child(user.email.toString().substringBefore("@")).child("goals").setValue(goals)
        onResult(true, "IT WORKS")
    }
    else onResult(false, "error with writing goals")
}

fun fetchUserGoals(onDataReceived: (Goals) -> Unit, onFailure: (Exception) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val user = getCurrentUser()
    var ref = database.getReference("goals")

    if (user != null) ref = database.getReference(user.email.toString().substringBefore("@")) // Points to recipes node in Firebase

    ref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var goal = Goals()
            for (goalSnapShot in snapshot.children) {
                val userGoal = goalSnapShot.getValue(Goals::class.java)
                userGoal.let {
                    if (it != null) {
                        goal = it
                    }
                }
            }
            onDataReceived(goal)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Firebase", "Error fetching data: ${error.message}")
            onFailure(error.toException())
        }
    })
}