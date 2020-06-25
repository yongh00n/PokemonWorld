package com.sandy.pokemonworld.network.models

data class PokemonResponse(
    var id: Int,
    var name: String,
    var height: Int,
    var weight: Int
)