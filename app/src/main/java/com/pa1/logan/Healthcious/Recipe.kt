package com.pa1.logan.Healthcious

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme

@Composable
fun Recipe(navController: NavController?) {

    Card (
        modifier = Modifier
            .fillMaxSize(),

        onClick = {

        }
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(painter = painterResource(R.drawable.logo), "")

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 5.dp, bottom = 5.dp)
            ) {
                Text(
                    "name",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp
                )

                Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(
                        "description",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 20.sp
                    )

                    Card (){
                        Text("prep time")
                    }

                    Card(){
                        Text("cuisine")
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun RecipePreview() {
    AppTheme {
        Recipe(navController = null)
    }
}

