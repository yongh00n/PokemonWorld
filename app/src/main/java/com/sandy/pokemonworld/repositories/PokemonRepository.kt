package com.sandy.pokemonworld.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.sandy.pokemonworld.network.PokemonApi
import com.sandy.pokemonworld.persistence.daos.PokemonDao
import com.sandy.pokemonworld.persistence.daos.PokemonItemDao
import com.sandy.pokemonworld.persistence.entities.Pokemon
import com.sandy.pokemonworld.persistence.entities.PokemonItem
import com.sandy.pokemonworld.persistence.keyvalues.PokemonApiStore
import com.sandy.pokemonworld.utils.ApiUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(
    private val pokemonApi: PokemonApi,
    private val pokemonDao: PokemonDao,
    private val pokemonItemDao: PokemonItemDao,
    private val pokemonApiStore: PokemonApiStore
) {

    companion object {
        const val TAG = "PokemonRepository"
    }

    fun getPokemonItems(name: String = ""): LiveData<List<PokemonItem>> {
        Log.d(TAG, "getPokemonItems by name=${name}")
        if (name.isEmpty()) {
            return pokemonItemDao.getPokemonItems()
        } else {
            return pokemonItemDao.getPokemonItemsByName(name)
        }
    }

    val totalCount: Int
        get() { return pokemonApiStore.getTotalPokemonCount() }

    suspend fun fetchPokemons(offset: Int = 0) {
        Log.d(TAG, "fetchPokemons offset=$offset totalCount=$totalCount")
        val pokemonsResponse = pokemonApi.listPokemons(offset)
        Log.d(TAG, "pokemonsResponse=${pokemonsResponse}")
        val items = pokemonsResponse.results.map {
            PokemonItem(ApiUtils.getPokemonIdFromFetchUrl(it.url!!), it.name!!)
        }
        pokemonsResponse.count?.let {
            pokemonApiStore.putTotalPokemonCount(it)
        }
        pokemonItemDao.insertAll(items)
    }

    fun getPokemon(id: Int): LiveData<Pokemon> {
        return Transformations.map(pokemonDao.getPokemon(id)) { pokemons ->
            if (pokemons.isEmpty()) null else pokemons.first()
        }
    }

    suspend fun fetchPokemon(id: Int = 0) {
        Log.d(TAG, "fetchPokemon id=$id")
        val pokemonResponse = pokemonApi.getPokemon(id)
        pokemonResponse.apply {
            val item = Pokemon(id, name, height, weight)
            Log.d(TAG, "insert ${item}")
            pokemonDao.insert(item)
        }
    }
}