package com.pa1.logan.Healthcious.VM

import androidx.lifecycle.ViewModel

class ShoppingCartVM: ViewModel() {

    var shoppingCartList = mutableListOf(
        shoppingCartItem("purchases","CheeseBurger", 1200f, 12f, 20f),
        shoppingCartItem("recipes","AglioOlio", 800f, 15f, 24f)
    )

}

data class shoppingCartItem(
    val type: String = "",
    val name: String = "",
    val calories: Float = 0f,
    val sugar: Float = 0f,
    val salt: Float = 0f,
)