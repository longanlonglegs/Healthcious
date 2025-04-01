package com.pa1.logan.Healthcious.ui.composables

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.AppTheme
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.gson.Gson
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.database.showImg
import com.pa1.logan.Healthcious.database.uploadImg
import com.pa1.logan.Healthcious.database.writeRecipe
import com.pa1.logan.Healthcious.ui.composables.health.Health
import com.pa1.logan.Healthcious.ui.composables.misc.Customize
import com.pa1.logan.Healthcious.ui.composables.misc.Settings
import com.pa1.logan.Healthcious.ui.composables.misc.ShoppingCart
import com.pa1.logan.Healthcious.ui.composables.misc.SignUpPage
import com.pa1.logan.Healthcious.ui.composables.onboarding.Onboarding
import com.pa1.logan.Healthcious.ui.composables.recipe.Food
import com.pa1.logan.Healthcious.ui.composables.recipe.Recipe
import com.pa1.logan.Healthcious.ui.composables.recipe.RecipeList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainPage(navController) }
        composable("onboarding") { Onboarding(navController) }
        composable("food/{recipe}",
            listOf(
                navArgument("recipe") { NavType.StringType },
            )
        ) {backStackEntry->
            val recipe = backStackEntry.arguments?.getString("recipe")
            if (recipe != null) {
                Food(navController, Gson().fromJson(recipe, Recipe::class.java))
                //call navController.navigate("customize/${recipe}")
            }
        }
        composable("settings") { Settings() }
        composable("customize") { Customize(navController) }
        composable("recipe") { RecipeList(navController, PaddingValues(5.dp)) }
        composable("purchase") { PurchaseList() }
        composable("health") { Health(navController) }
        composable("shoppingcart") { ShoppingCart(navController) }
        composable("signup") { SignUpPage(navController) }

    }
}


