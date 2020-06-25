package com.sandy.pokemonworld.network.models

data class PokemonsResponse(
    var count: Int?,
    var next: String?,
    var previous: String?,
    var name: String,
    var results: List<PokemonListItem>
    )