package com.pa1.logan.Healthcious.VM

class HealthVM {

    var totalCalories = 0f
    var totalSugar = 0f
    var totalSalt = 0f

}

data class Goals(
    val targetCalories: Float = 2000f,
    val targetSugar: Float = 30f,
    val targetSalt: Float = 2000f,
)