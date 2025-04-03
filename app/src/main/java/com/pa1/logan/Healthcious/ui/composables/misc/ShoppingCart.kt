package com.pa1.logan.Healthcious.ui.composables.misc

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.pa1.logan.Healthcious.VM.ShoppingCartVM
import com.pa1.logan.Healthcious.database.showImg
import com.pa1.logan.Healthcious.ui.composables.MinimalDropdownMenu
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RunningWithErrors
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.google.gson.Gson
import com.pa1.logan.Healthcious.R
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.database.fetchPurchases
import com.pa1.logan.Healthcious.database.fetchUserEatenFood
import com.pa1.logan.Healthcious.database.fetchUserPurchases
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.ui.composables.health.Health
import com.pa1.logan.Healthcious.ui.composables.recipe.Recipe
import com.pa1.logan.Healthcious.ui.composables.recipe.RecipeList
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCart(navController: NavController?) {

    Scaffold(
        topBar = { TopAppBar(
            title = {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Image(painter = painterResource(R.drawable.logo_withoutword),
                        "title",
                        modifier = Modifier.size(80.dp))
                    Text(
                        "Healthcious",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

            },

            navigationIcon = {
                MinimalDropdownMenu(navController)
            },

            actions = {
                IconButton(
                    onClick = {
                        if (getCurrentUser() == null) navController?.navigate("signin")
                        else Toast.makeText(navController?.context, "Already logged in", Toast.LENGTH_SHORT).show()
                    }
                ){
                    Icon(Icons.Default.AccountCircle, "menu")
                }
            }
        )},

        content = {
                paddingValues ->
            MyCart(navController, paddingValues)
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController?.navigate("customize")
                }
            ) {
                Icon(Icons.Default.Add, "Add own dish")
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("main")
                    },
                    icon = { Icon(Icons.Default.Book, "Recipe")},
                    selected = false,
                    label = { Text("Recipe") }
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("purchase")
                    },
                    icon = { Icon(Icons.Default.Storefront, "Purchase")},
                    selected = false,
                    label = { Text("Purchase") }
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("health")
                    },
                    icon = { Icon(Icons.Filled.RunningWithErrors, "Health")},
                    selected = false,
                    label = { Text("Health") }
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("shoppingcart")
                    },
                    icon = { Icon(Icons.Filled.Fastfood, "Shopping Cart")},
                    selected = true,
                    label = { Text("Eaten Food") }
                )
            }
        }
    )

}

@Composable
fun MyCart(navController: NavController?, paddingValues: PaddingValues) {

    val cartVM = remember { ShoppingCartVM() }
    var type by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (getCurrentUser() != null) fetchUserEatenFood(
            onDataReceived = {
                cartVM.shoppingCartList.value = it
                Log.d("Eaten Food list", "Fetched ${it.size} Eaten Food")
            },
            onFailure = { }
        )
    }

    LazyColumn(Modifier
        .fillMaxSize()
        .padding(paddingValues), horizontalAlignment = Alignment.CenterHorizontally){
        for (cartItem in cartVM.shoppingCartList.value) {
            item {
                Card(modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .clickable {
                        if (cartItem.recipes != null) {
                            val jsonRecipe = Gson().toJson(cartItem.recipes)
                            val encodedRecipe = URLEncoder.encode(jsonRecipe, StandardCharsets.UTF_8.toString())

                            navController?.navigate("food/$encodedRecipe")
                        }

                        else {
                            val jsonPurchase = Gson().toJson(cartItem.purchases)
                            val encodedPurchase = URLEncoder.encode(jsonPurchase, StandardCharsets.UTF_8.toString())

                            navController?.navigate("dish/$encodedPurchase")
                        }
                    }
                ) {
                    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                        type = if (cartItem.recipes != null) "recipes" else "purchases"

                        Image(
                            painter = showImg("images/${type}/${cartItem.name}.png"), "cart item",
                            modifier = Modifier.size(100.dp),
                            contentScale = ContentScale.Crop,
                            )

                        Column (verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.padding(5.dp)){
                            Text(cartItem.name, fontWeight = FontWeight.Bold)
                            Text("${cartItem.calories.toInt()}kcal")
                            Text("Quantity: ${cartItem.quantity}")
                        }
                    }
                }
            }
        }
    }
}
