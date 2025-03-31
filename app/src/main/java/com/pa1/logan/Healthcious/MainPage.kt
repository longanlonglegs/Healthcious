package com.pa1.logan.Healthcious

import android.graphics.ColorFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.compose.AppTheme
import kotlinx.coroutines.launch

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

            navigationIcon = { IconButton(
                onClick = {}
            ){
                Icon(Icons.Default.Menu, "menu")
            }},

            actions = {
                IconButton(
                    onClick = {}
                ){
                    Icon(Icons.Default.AccountCircle, "menu")
                }
            }
        )},

        content = {
            paddingValues ->
            TabRow(paddingValues, navController)
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                }
            ) {
                Icon(Icons.Default.ShoppingCart, "food eaten")
            }
        }
    )

}

@Composable
fun TabRow(paddingValues: PaddingValues, navController: NavController?) {

    val pages = 3
    val pagerState = rememberPagerState(pageCount = {pages})
    val scope = rememberCoroutineScope()
    val tabList = listOf(
        "Recipe",
        "Purchase",
        "Health"
    )

    Column (modifier = Modifier.padding(paddingValues)){
        androidx.compose.material3.TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabList.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(title) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) {page ->
            Column (horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier
                .fillMaxSize()){
                when (page) {
                    0 -> {
                        RecipeList(navController)
                    }
                    1 -> {
                        PurchaseList()
                    }
                    2 -> {
                        Health(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun PurchaseList() {
    LazyColumn(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){

        items(20){
            Card (modifier = Modifier.fillMaxSize()){  }
        }

    }
}

@Composable
fun Searchbar(search: String) {
    var search by remember { mutableStateOf("") }
    TextField(value = search, onValueChange = {search = it},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search by keyword...")},
        leadingIcon = {Icon(Icons.Default.Search, contentDescription = "Search")},
    )
}

@Composable
fun RecipeList(navController: NavController?) {

    var search by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    )
    {
        Searchbar(search)

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {

            items(20) {
                Recipe(navController)
            }

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

