package com.sandy.pokemonworld.utils

object ApiUtils {
    private const val API_FETCH_BASE_URL: String = "https://pokeapi.co/api/v2/pokemon/"

    fun getPokemonIdFromFetchUrl(url: String): Int {
        return url.substringAfter(API_FETCH_BASE_URL).substringBefore("/").toInt()
    }

}
