package com.sandy.pokemonworld.viewmodels

import androidx.recyclerview.widget.DiffUtil

data class PokemonItemModel(val id: Int, val name: String) {

    val photoUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${id}.png"
    val isLoadingItem = id == Int.MIN_VALUE

    companion object {
        val LOADING_ITEM_MODEL = PokemonItemModel(Int.MIN_VALUE, "")

        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<PokemonItemModel>() {
            override fun areItemsTheSame(oldItem: PokemonItemModel, newItem: PokemonItemModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PokemonItemModel, newItem: PokemonItemModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
