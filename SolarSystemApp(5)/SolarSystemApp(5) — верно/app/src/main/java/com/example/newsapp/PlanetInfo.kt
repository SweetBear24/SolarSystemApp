// PlanetInfo.kt
package com.example.newsapp

data class PlanetInfo(val name: String, val description: String, val imageResId: Int)

val planetsInfo = listOf(
    PlanetInfo("Sun", "Звезда в центре нашей солнечной системы.", R.drawable.sun),
    PlanetInfo("Mercury", "Ближайшая планета к Солнцу.", R.drawable.mercury),
    PlanetInfo("Moon", "Естественный спутник Земли.", R.drawable.moon)
    // Добавьте информацию для каждой планеты
)
