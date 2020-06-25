package com.sandy.pokemonworld.utils

import androidx.room.Ignore

object Constants {
    const val API_BASE_URL: String = "https://pokeapi.co/api/v2/"

    const val FRONT_DEFAULT_URL_FORMAT = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/%d.png"
    const val FRONT_SHINY_URL_FORMAT = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/%d.png"
    const val BACK_DEFAULT_URL_FORMAT = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/%d.png"
    const val BACK_SHINY_URL_FORMAT = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/%d.png"
}
