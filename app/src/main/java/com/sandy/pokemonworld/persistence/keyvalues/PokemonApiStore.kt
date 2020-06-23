package com.sandy.pokemonworld.persistence.keyvalues

import android.content.Context

class PokemonApiStore constructor(context: Context) {
    private val sharedPref by lazy { context.getSharedPreferences("PokemonApiStore", Context.MODE_PRIVATE) }

    fun putTotalPokemonCount(count: Int) {
        sharedPref.edit().putInt(Key.TOTAL_POKEMON_COUNT.name, count).apply()
    }

    fun getTotalPokemonCount(): Int {
        return sharedPref.getInt(Key.TOTAL_POKEMON_COUNT.name, 0)
    }

    enum class Key {
        TOTAL_POKEMON_COUNT,
    }
}