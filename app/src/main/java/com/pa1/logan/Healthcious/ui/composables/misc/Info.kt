package com.pa1.logan.Healthcious.ui.composables.misc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.pa1.logan.Healthcious.ui.composables.recipe.ItemScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Info(navController: NavController?) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Info") },
                navigationIcon = { IconButton(onClick = {
                    navController?.navigate("main")
                }) { Icon(Icons.AutoMirrored.Default.ArrowBack, "go back") } },
            )
        },
        content = {
                paddingValues ->
            Instructions(paddingValues)
        },
    )

}

@Composable
fun Instructions(paddingValues: PaddingValues) {

    Column(Modifier.fillMaxSize().padding(paddingValues), horizontalAlignment = Alignment.CenterHorizontally) {

        Text("Instructions")

    }

}