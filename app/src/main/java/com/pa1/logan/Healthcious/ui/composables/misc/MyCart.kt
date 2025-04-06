package com.pa1.logan.Healthcious.ui.composables.misc

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.pa1.logan.Healthcious.VM.ShoppingCartVM
import com.pa1.logan.Healthcious.database.showImg
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.pa1.logan.Healthcious.database.fetchUserEatenFood
import com.pa1.logan.Healthcious.database.getCurrentUser
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MyCart(navController: NavController?) {

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
        , horizontalAlignment = Alignment.CenterHorizontally){
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
