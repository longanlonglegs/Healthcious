package com.pa1.logan.Healthcious.VM

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ShoppingCartVM: ViewModel() {

    var shoppingCartList = mutableStateOf(listOf<shoppingCartItem>())

}

data class shoppingCartItem(
    val name: String = "",
    val calories: Float = 0f,
    val sugar: Float = 0f,
    val salt: Float = 0f,
    val quantity: Int = 0,
    val purchases: Purchases? = null,
    val recipes: Recipe? = null
)