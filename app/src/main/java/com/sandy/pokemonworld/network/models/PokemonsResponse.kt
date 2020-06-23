package com.sandy.pokemonworld.network.models

data class PokemonsResponse(
    var count: Int?,
    var next: String?,
    var previous: String?,
    var name: String,
    var results: List<PokemonListItem>
    ) {
//
//    count:964
//    next:"https://pokeapi.co/api/v2/pokemon/?offset=20&limit=20"
//    previous:null
//    name:"bulbasaur"
//    url:"https://pokeapi.co/api/v2/pokemon/1/"

}