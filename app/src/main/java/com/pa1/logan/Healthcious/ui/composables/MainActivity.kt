package com.pa1.logan.Healthcious.ui.composables

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.compose.AppTheme
import com.google.gson.Gson
import com.pa1.logan.Healthcious.VM.Purchases
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.database.StreakWorker
import com.pa1.logan.Healthcious.ui.composables.health.HealthPage
import com.pa1.logan.Healthcious.ui.composables.misc.Customize
import com.pa1.logan.Healthcious.ui.composables.misc.Settings
import com.pa1.logan.Healthcious.ui.composables.misc.ShoppingCart
import com.pa1.logan.Healthcious.ui.composables.misc.SignInPage
import com.pa1.logan.Healthcious.ui.composables.misc.SignUpPage
import com.pa1.logan.Healthcious.ui.composables.onboarding.Onboarding
import com.pa1.logan.Healthcious.ui.composables.purchase.Dish
import com.pa1.logan.Healthcious.ui.composables.purchase.PurchasePage
import com.pa1.logan.Healthcious.ui.composables.recipe.Food
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainApp() {
    val navController = rememberNavController()

    val context = LocalContext.current

    val streakManager = WorkManager.getInstance(context)

    val workRequest = PeriodicWorkRequestBuilder<StreakWorker>(
        repeatInterval = 15,
        TimeUnit.MINUTES
    )
        .build()

    streakManager.enqueueUniquePeriodicWork(
        "streak worker",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest)

    NavHost(navController = navController, startDestination = "signin") {
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
        composable("settings") { Settings() }
        composable("customize") { Customize(navController) }
        composable("purchase") { PurchasePage(navController) }
        composable("health") { HealthPage(navController) }
        composable("shoppingcart") { ShoppingCart(navController) }
        composable("signin") { SignInPage(navController) }
        composable("signup") { SignUpPage(navController) }

    }
}

@Composable
fun RuntimePermissionsDialog(
    permission: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        if (ContextCompat.checkSelfPermission(
                LocalContext.current,
                permission) != PackageManager.PERMISSION_GRANTED) {

            val requestLocationPermissionLauncher =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->

                    if (isGranted) {
                        onPermissionGranted()
                    } else {
                        onPermissionDenied()
                    }
                }

            SideEffect {
                requestLocationPermissionLauncher.launch(permission)
            }
        }
    }
}
