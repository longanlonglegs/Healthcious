package com.pa1.logan.Healthcious.ui.composables

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources.Theme
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.compose.AppTheme
import com.google.gson.Gson
import com.pa1.logan.Healthcious.VM.Goals
import com.pa1.logan.Healthcious.VM.Purchases
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.VM.Streak
import com.pa1.logan.Healthcious.database.StreakWorker
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.database.writeUserGoals
import com.pa1.logan.Healthcious.database.writeUserStreak
import com.pa1.logan.Healthcious.ui.composables.misc.Account
import com.pa1.logan.Healthcious.ui.composables.misc.Customize
import com.pa1.logan.Healthcious.ui.composables.misc.Info
import com.pa1.logan.Healthcious.ui.composables.misc.Settings
import com.pa1.logan.Healthcious.ui.composables.misc.SignInPage
import com.pa1.logan.Healthcious.ui.composables.misc.SignUpPage
import com.pa1.logan.Healthcious.ui.composables.onboarding.Onboarding
import com.pa1.logan.Healthcious.ui.composables.purchase.Dish
import com.pa1.logan.Healthcious.ui.composables.recipe.Food
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var dark : MutableState<Boolean>
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWorkManager(this)
        enableEdgeToEdge()
        setContent {

            dark = remember { mutableStateOf(false) }

            AppTheme(darkTheme = dark.value) {
                MainApp()
            }
        }
    }

    private fun setupWorkManager(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<StreakWorker>(24, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED) // ✅ Ensures it runs only with the internet
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "StreakWorker",
            ExistingPeriodicWorkPolicy.KEEP, // ✅ Prevents duplicate work requests
            workRequest
        )
    }

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainApp() {
    val navController = rememberNavController()

    val user = getCurrentUser()

    // Set initial destination based on auth state
    val startDestination = if (user != null) "main" else "signin"

    NavHost(navController = navController, startDestination = startDestination) {
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
        composable("dish/{dish}",
            listOf(
                navArgument("dish") { NavType.StringType },
            )
        ) {backStackEntry->
            val dish = backStackEntry.arguments?.getString("dish")
            if (dish != null) {
                Dish(navController, Gson().fromJson(dish, Purchases::class.java))
                //call navController.navigate("customize/${recipe}")
            }
        }
        composable("settings") { Settings(navController) }
        composable("customize") { Customize(navController) }
        composable("signin") { SignInPage(navController) }
        composable("signup") { SignUpPage(navController) }
        composable("info") { Info(navController) }
        composable("account") { Account(navController) }

    }
}
