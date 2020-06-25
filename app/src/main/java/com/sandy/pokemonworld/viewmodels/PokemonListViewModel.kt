package com.sandy.pokemonworld.viewmodels

import android.util.Log
import androidx.arch.core.util.Function
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import com.sandy.pokemonworld.network.PokemonApi
import com.sandy.pokemonworld.persistence.daos.PokemonItemDao
import com.sandy.pokemonworld.repositories.PokemonRepository
import kotlinx.coroutines.*
import javax.inject.Inject

class PokemonListViewModel @ViewModelInject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    companion object {
        const val TAG = "PokemonListModel"
        const val DEBOUNCE_LIMIT_IN_MILLIS = 300L
    }

    val isLoading = MutableLiveData<Boolean>(false)
    val query = MutableLiveData<String>("")
    private var queryTextChangeJob: Job? = null

    val pokemonItems: LiveData<List<PokemonItemModel>> =
        Transformations.switchMap(query) { name -> getPokemonItemsByName(name) }

    private fun getPokemonItemsByName(name: String): LiveData<List<PokemonItemModel>> {
        return Transformations.map(pokemonRepository.getPokemonItems(name)) { items ->
            items.map { PokemonItemModel(it.id, it.name) }
        }
    }

    fun fetchPokemonItems(fetchMore: Boolean = false) {
        Log.d(TAG, "fetchPokemonItems fetchMore=$fetchMore isLoading=${isLoading.value}")
        if (isLoading.value == true) {
            Log.d(TAG, "fetchPokemonItems - A previous fetch is still on the way.")
            return
        }

        val loadedItemCounts = pokemonItems.value?.size ?: 0
        Log.d(TAG, "totalCount=${pokemonRepository.totalCount}")
        if (loadedItemCounts > 0 && pokemonRepository.totalCount <= loadedItemCounts) {
            Log.d(TAG, "fetchPokemonItems - already fetched all pokemons! :-)")
            return
        }

        isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val offset = if (fetchMore) loadedItemCounts else 0
            pokemonRepository.fetchPokemons(offset)
            withContext(Dispatchers.Main) {
                isLoading.value = false
            }
        }
    }

    fun onQueryTextChange(text: String) {
        queryTextChangeJob?.cancel()
        queryTextChangeJob = CoroutineScope(Dispatchers.Main).launch {
            delay(DEBOUNCE_LIMIT_IN_MILLIS)
            query.value = text
        }
    }
}
