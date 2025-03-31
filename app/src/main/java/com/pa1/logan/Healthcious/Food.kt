package com.pa1.logan.Healthcious

import android.content.ClipData.Item
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Food(navController: NavController?) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())},
                navigationIcon = { IconButton(onClick = {}) {Icon(Icons.AutoMirrored.Default.ArrowBack, "go back")} },
                actions = {
                    IconButton(onClick = {
                        TODO("star a recipe")
                    }) {

                        Icon(Icons.Default.Favorite, "save this recipe")

                    }
                }
            )
        },
        content = {
            paddingValues ->
            ItemScreen(paddingValues)
        }
    )
}

@Composable
fun ItemScreen(paddingValues: PaddingValues) {
    Column(
        Modifier
            .padding(paddingValues)
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){

        Text(
            "dish name",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 50.sp,
            lineHeight = 40.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
            )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                "1000",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 40.sp,
                textAlign = TextAlign.Start,
            )
            Text(
                "cal",
                //fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
            )
        }

        Image(painter = painterResource(R.drawable.logo),
            "dish image"
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
        ) {
            Card (){
                Text("keyword",
                    fontSize = 25.sp)
            }

            Card(){
                Text("keyword",
                    fontSize = 25.sp)
            }
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
        ) {
            Card (){
                Text("salt", fontSize = 25.sp)
            }

            Card(){
                Text("sugar", fontSize = 25.sp)
            }

            Card(){
                Text("allergens", fontSize = 25.sp)
            }
        }


        Card(modifier = Modifier.fillMaxWidth()) {
            Text("description", fontSize = 25.sp)
        }

        Spacer(modifier = Modifier.padding(30.dp))

        Button(onClick = {

        }) {
            Text("Eat!", fontSize = 25.sp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FoodPreview() {
    AppTheme {
        Food(navController = null)
    }
}