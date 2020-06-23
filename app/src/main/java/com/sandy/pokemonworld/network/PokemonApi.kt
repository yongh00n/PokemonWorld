package com.sandy.pokemonworld.network

import com.sandy.pokemonworld.network.models.PokemonsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * https://pokeapi.co/docs/v2
 */
interface PokemonApi {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    @GET("pokemon")
    suspend fun listPokemons(@Query("offset") offset: Int = 0, @Query("limit") limit: Int = DEFAULT_PAGE_SIZE): PokemonsResponse
}