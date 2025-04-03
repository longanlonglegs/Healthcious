package com.pa1.logan.Healthcious.ui.composables.misc

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.Key.Companion.F
import androidx.compose.ui.unit.dp

@Composable
fun Searchbar(search: String) {
    var search by remember { mutableStateOf(search) }
    OutlinedTextField(value = search, onValueChange = {search = it},
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100.dp))
            .padding(10.dp),
        placeholder = { Text("Search by keyword...") },
        trailingIcon     = { Icon(Icons.Default.Search, contentDescription = "Search") },
    )
}