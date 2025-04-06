package com.pa1.logan.Healthcious.ui.composables.purchase

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pa1.logan.Healthcious.VM.Purchases
import com.pa1.logan.Healthcious.VM.shoppingCartItem
import com.pa1.logan.Healthcious.database.fetchUserEatenFood
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.database.showImg
import com.pa1.logan.Healthcious.database.writeUserEatenFood
import com.pa1.logan.Healthcious.database.writeUserPurchase
import com.pa1.logan.Healthcious.database.writeUserRecipe
import com.pa1.logan.Healthcious.ui.composables.recipe.ItemScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dish(navController: NavController?, purchases: Purchases) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Purchase") },
                navigationIcon = { IconButton(onClick = {
                    navController?.navigate("main")
                }) { Icon(Icons.AutoMirrored.Default.ArrowBack, "go back") } },
            )
        },
        content = {
                paddingValues ->
            DishScreen(paddingValues, purchases, navController)
        },
    )
}


@Composable
fun DishScreen(paddingValues: PaddingValues, purchases: Purchases, navController: NavController?) {

    val name = purchases.name.replace("+", " ")
    var eatenList by remember { mutableStateOf(listOf<shoppingCartItem>()) }
    val context = LocalContext.current
    var quantity by remember { mutableIntStateOf(1) }

    LaunchedEffect (Unit){
        fetchUserEatenFood(
            onDataReceived = {
                eatenList = it
                Log.d("Eaten Food list", "Fetched ${it.size} Eaten Food")
            },
            onFailure = { }
        )
    }

    Column (
        Modifier
            .padding(paddingValues)
            .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Image(
            painter = showImg("images/purchases/${name}.png"),
            "recipe image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
        )

        Column {
            Text(
                if (purchases.store != "") purchases.store else "unknown store",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            Text(
                name,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 25.sp,
                lineHeight = 25.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card{
                Text(
                    "${purchases.calories.toInt()}kcal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    modifier = (Modifier.padding(5.dp))
                )
            }

            Card {
                Text(
                    "${purchases.salt.toInt()}g salt", fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = (Modifier.padding(5.dp))
                )
            }

            Card {
                Text(
                    "${purchases.sugar.toInt()}g sugar", fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = (Modifier.padding(5.dp))
                )
            }

        }

        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center)
        {

            Text("Quantity: ")

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(16.dp).clip(RoundedCornerShape(64.dp)).background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                CounterCircleButton("-", onClick = {if (quantity > 1) quantity-- })
                Text(
                    text = quantity.toString(),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .animateContentSize()
                        .padding(horizontal = 12.dp)
                )
                CounterCircleButton("+", onClick = { quantity++ })
            }
        }

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom){
            FilledTonalButton(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    for (food in eatenList) {
                        if (food.name == purchases.name && food.recipes == null) {
                            quantity += food.quantity
                        }
                    }

                    writeUserEatenFood(
                        shoppingCartItem(
                            purchases.name,
                            purchases.calories,
                            purchases.salt,
                            purchases.sugar,
                            quantity = quantity,
                            purchases = purchases,
                        ),
                        onResult = { success, message ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Om Nom! Yummy in my tummy!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController?.navigate("main")
                            } else {
                                Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                Log.d("Dish eating button err", message.toString())
                            }
                        }
                    )
                },
            )
            {
                Text("Eat Now!", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun CounterCircleButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = Modifier
            .size(56.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Text(text, fontSize = 22.sp, color = Color.White, textAlign = TextAlign.Center)
    }
}
