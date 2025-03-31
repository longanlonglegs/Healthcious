package com.pa1.logan.Healthcious.VM

import androidx.lifecycle.ViewModel

class PurchaseVM {

    var purchaseList = mutableListOf<Purchases>()

}

data class Purchases(
    val name: String,
    val calories: Float,
    val sugar: Float,
    val salt: Float,
    val store: String,
)