package com.pa1.logan.Healthcious.ui.composables

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
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
fun MainPage(navController: NavController?) {

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
            RecipeList(navController, paddingValues)
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
                    icon = { Icon(Icons.Default.Lock, "Recipe")},
                    selected = true,
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("purchase")
                    },
                    icon = { Icon(Icons.Default.AccountBox, "Purchase")},
                    selected = false,
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("health")
                    },
                    icon = { Icon(Icons.Filled.Warning, "Health")},
                    selected = false,
                )
                NavigationBarItem(
                    onClick = {
                        navController?.navigate("shoppingcart")
                    },
                    icon = { Icon(Icons.Filled.ShoppingCart, "Shopping Cart")},
                    selected = false,
                )
            }
        }
    )

}

@Composable
fun MinimalDropdownMenu(navController: NavController?) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.Menu, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Custom Recipe") },
                onClick = {

                }
            )
            DropdownMenuItem(
                text = { Text("Option 2") },
                onClick = { /* Do something... */ }
            )
        }
    }
}

//@Composable
//fun TabRow(paddingValues: PaddingValues, navController: NavController?) {
//
//    val pages = 3
//    val pagerState = rememberPagerState(pageCount = {pages})
//    val scope = rememberCoroutineScope()
//    val tabList = listOf(
//        "Recipe",
//        "Purchase",
//        "Health"
//    )
//
//    Column (modifier = Modifier.padding(paddingValues)){
//        androidx.compose.material3.TabRow(
//            selectedTabIndex = pagerState.currentPage,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            tabList.forEachIndexed { index, title ->
//                Tab(
//                    selected = pagerState.currentPage == index,
//                    onClick = {
//                        scope.launch {
//                            pagerState.animateScrollToPage(index)
//                        }
//                    },
//                    text = { Text(title) }
//                )
//            }
//        }
//
//        HorizontalPager(
//            state = pagerState,
//            modifier = Modifier.fillMaxSize()
//        ) {page ->
//            Column (horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier
//                .fillMaxSize()){
//                when (page) {
//                    0 -> {
//                        RecipeList(navController)
//                    }
//                    1 -> {
//                        PurchaseList()
//                    }
//                    2 -> {
//                        Health(navController)
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun PurchaseList() {
    LazyColumn(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){

        items(20){
            Card (modifier = Modifier.fillMaxSize()){  }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    AppTheme {
        MainPage(navController = null)
    }
}

