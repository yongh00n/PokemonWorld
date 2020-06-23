package com.sandy.pokemonworld.viewmodels

import android.util.Log
import androidx.arch.core.util.Function
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.sandy.pokemonworld.network.PokemonApi
import com.sandy.pokemonworld.persistence.daos.PokemonItemDao
import com.sandy.pokemonworld.repositories.PokemonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonListViewModel @ViewModelInject constructor(
    private val pokemonRepository: PokemonRepository
)
    : ViewModel() {

    companion object {
        const val TAG = "PokemonListModel"
    }

    val isLoading = MutableLiveData<Boolean>(false)

    val pokemonItems: LiveData<List<PokemonItemModel>> = Transformations.map(pokemonRepository.pokemonItems
    ) { items -> items.map { PokemonItemModel(it.id, it.name)} }

    fun fetchPokemonItems(fetchMore: Boolean = false) {
        Log.d(TAG, "fetchPokemonItems fetchMore=$fetchMore isLoading=${isLoading.value}")
        if (isLoading.value == true) {
            Log.d(TAG, "fetchPokemonItems - A previous fetch is still on the way.")
            return
        }

        val loadedItemCounts = pokemonRepository.pokemonItems.value?.size ?: 0
        Log.d(TAG, "totalCount=${pokemonRepository.totalCount}")
        if (pokemonRepository.totalCount <= loadedItemCounts) {
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
}
