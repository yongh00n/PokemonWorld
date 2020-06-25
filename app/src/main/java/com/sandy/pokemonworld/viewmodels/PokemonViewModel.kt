package com.sandy.pokemonworld.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sandy.pokemonworld.persistence.entities.Pokemon
import com.sandy.pokemonworld.repositories.PokemonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonViewModel @ViewModelInject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    companion object {
        const val TAG = "PokemonViewModel"
    }

    fun fetchPokemon(id: Int) {
        Log.d(TAG, "fetchPokemon id=$id")

        CoroutineScope(Dispatchers.IO).launch {
            pokemonRepository.fetchPokemon(id)
        }
    }

    fun getPokemon(id: Int): LiveData<Pokemon> {
        return pokemonRepository.getPokemon(id)
    }
}