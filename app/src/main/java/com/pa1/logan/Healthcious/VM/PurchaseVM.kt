package com.pa1.logan.Healthcious.VM

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PurchaseVM {

    var purchaseList = mutableStateOf(listOf<Purchases>())

}

data class Purchases(
    val name: String = "",
    val calories: Float = 0f,
    val sugar: Float = 0f,
    val salt: Float = 0f,
    val store: String = "",
)