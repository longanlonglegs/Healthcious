package com.pa1.logan.Healthcious.ui.composables.misc

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun Searchbar(search: String) {
    var search by remember { mutableStateOf(search) }
    TextField(value = search, onValueChange = {search = it},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search by keyword...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
    )
}