package com.pa1.logan.Healthcious.ui.composables.purchase

import com.pa1.logan.Healthcious.ui.composables.MainPage
import com.pa1.logan.Healthcious.ui.composables.MinimalDropdownMenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.pa1.logan.Healthcious.R
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.ui.composables.health.Health
import com.pa1.logan.Healthcious.ui.composables.recipe.Recipe
import com.pa1.logan.Healthcious.ui.composables.recipe.RecipeList
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasePage(navController: NavController?) {

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
                        navController?.navigate("signup")
                    }
                ){
                    Icon(Icons.Default.AccountCircle, "sign up")
                }
            }
        )},

        content = {
                paddingValues ->
            PurchaseList(navController, paddingValues)
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
            NavigationBar (modifier = Modifier.height(80.dp), containerColor = Color.Transparent){
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("main")
                    },
                    icon = { Icon(Icons.Default.Book, "Recipe")},
                    selected = false,
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("purchase")
                    },
                    icon = { Icon(Icons.Default.Storefront, "Purchase")},
                    selected = true,
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("health")
                    },
                    icon = { Icon(Icons.Filled.RunningWithErrors, "Health")},
                    selected = false,
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("shoppingcart")
                    },
                    icon = { Icon(Icons.Filled.Fastfood, "Shopping Cart")},
                    selected = false,
                )
            }
        }
    )

}

@Preview(showBackground = true)
@Composable
fun PurchasePagePreview() {
    AppTheme {
        PurchasePage(navController = null)
    }
}

