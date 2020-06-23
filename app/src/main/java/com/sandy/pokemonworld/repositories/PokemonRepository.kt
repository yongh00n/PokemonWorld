package com.sandy.pokemonworld.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sandy.pokemonworld.activities.MainActivity
import com.sandy.pokemonworld.network.PokemonApi
import com.sandy.pokemonworld.network.models.PokemonsResponse
import com.sandy.pokemonworld.persistence.daos.PokemonItemDao
import com.sandy.pokemonworld.persistence.entities.PokemonItem
import com.sandy.pokemonworld.persistence.keyvalues.PokemonApiStore
import com.sandy.pokemonworld.utils.ApiUtils
import com.sandy.pokemonworld.viewmodels.PokemonItemModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(
    private val pokemonApi: PokemonApi,
    private val pokemonItemDao: PokemonItemDao,
    private val pokemonApiStore: PokemonApiStore
) {

    companion object {
        const val TAG = "PokemonRepository"
    }

//    val pokemonItems: LiveData<List<PokemonItem>> = pokemonItemDao.getPokemonItems()

    fun getPokemonItems(name: String = ""): LiveData<List<PokemonItem>> {
        Log.d(TAG, "##### getPokemonItems name=${name}")
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
}