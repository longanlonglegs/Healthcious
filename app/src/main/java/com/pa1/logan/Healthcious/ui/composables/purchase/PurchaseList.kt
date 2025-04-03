package com.pa1.logan.Healthcious.ui.composables.purchase

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.pa1.logan.Healthcious.VM.PurchaseVM
import com.pa1.logan.Healthcious.VM.Purchases
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.VM.RecipeVM
import com.pa1.logan.Healthcious.database.fetchPurchases
import com.pa1.logan.Healthcious.database.fetchRecipes
import com.pa1.logan.Healthcious.database.fetchUserPurchases
import com.pa1.logan.Healthcious.database.fetchUserRecipes
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.ui.composables.MainPage
import com.pa1.logan.Healthcious.ui.composables.misc.Searchbar
import com.pa1.logan.Healthcious.ui.composables.recipe.RecipeList

@Composable
fun PurchaseList(navController: NavController?, paddingValues: PaddingValues) {

    var search by remember { mutableStateOf("") }
    val purchaseVM = remember { PurchaseVM() }


    LaunchedEffect(Unit) {
        fetchPurchases(
            onDataReceived = {
                purchaseVM.purchaseList.value = it
                Log.d("Purchase list", "Fetched ${it.size} purchase")
            },
            onFailure = { }
        )
        if (getCurrentUser() != null) fetchUserPurchases(
            onDataReceived = {
                purchaseVM.purchaseList.value = it + purchaseVM.purchaseList.value
                Log.d("Purchase list", "Fetched ${it.size} purchases")
            },
            onFailure = { }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    )
    {

        Text(
            "Stay healthy with these purchases",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).padding(bottom = 5.dp),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp,
            lineHeight = 30.sp
        )

        Searchbar(search)

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {

            for (purchase in purchaseVM.purchaseList.value) {
                item {
                    Purchase(navController, purchase)
                }
            }

        }
    }
}