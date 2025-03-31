package com.pa1.logan.Healthcious

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Onboarding(navController: NavController?) {
    val pages = 3
    val pagerState = rememberPagerState(pageCount = { pages })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        content = { paddingValues ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) { page ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                ) {
                    when (page) {
                        0 -> {
                            Page1()
                        }
                        1 -> {
                            Page2()
                        }
                        2 -> {
                            Page3(navController)
                        }
                    }
                }
            }
        },
        bottomBar = {
            LinearProgressIndicator(
                progress = {
                    (pagerState.currentPage.toFloat() + 1) / pagerState.pageCount  // Using currentProgress directly
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp),
            )
        }
    )
}


@Composable
fun Page1() {

    Column(Modifier
        .fillMaxSize()
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        content = {

            Image(painter = painterResource(R.drawable.logo), "Healthcious")

            Text("Welcome to Healthcious!", fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, lineHeight = 40.sp, textAlign = TextAlign.Center)

            Text("Your one stop paradise to a nutritious life :)", fontSize = 15.sp, textAlign = TextAlign.Center)

        }
    )

}

@Composable
fun Page2() {

    Column(Modifier
        .fillMaxSize()
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        content = {

            Image(painter = painterResource(R.drawable.logo), "Healthcious")

            Text("Surpass your weight loss goals & dreams", fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, lineHeight = 40.sp, textAlign = TextAlign.Center)

            Text("Using our calorie tracker and visualization tools!", fontSize = 15.sp, textAlign = TextAlign.Center)

        }
    )
}

@Composable
fun Page3(navController: NavController?) {

    Column(Modifier
        .fillMaxSize()
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        content = {

            Image(painter = painterResource(R.drawable.logo), "Healthcious")

            Text("Get tons of recipes and insights into food", fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, lineHeight = 40.sp, textAlign = TextAlign.Center)

            Text("through the wonderful catalogue of items available!", fontSize = 15.sp, textAlign = TextAlign.Center)

            Button(onClick = {
                navController?.navigate("main")
            }) {
                Text("Start Journey!")
            }

        }
    )
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    AppTheme {
        Onboarding(navController = null)
    }
}