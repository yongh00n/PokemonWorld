package com.sandy.pokemonworld.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.sandy.pokemonworld.R
import com.sandy.pokemonworld.persistence.entities.Pokemon
import com.sandy.pokemonworld.utils.Constants.BACK_DEFAULT_URL_FORMAT
import com.sandy.pokemonworld.utils.Constants.BACK_SHINY_URL_FORMAT
import com.sandy.pokemonworld.utils.Constants.FRONT_DEFAULT_URL_FORMAT
import com.sandy.pokemonworld.utils.Constants.FRONT_SHINY_URL_FORMAT
import com.sandy.pokemonworld.viewmodels.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_pokemon.*

@AndroidEntryPoint
class PokemonFragment : Fragment() {

    companion object {
        const val TAG = "PokemonFragment"
    }

    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pokemon, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.getInt("id")?.let { pokemonId ->
            viewModel.fetchPokemon(pokemonId)
            viewModel.getPokemon(pokemonId).observe(viewLifecycleOwner, Observer { pokemon ->
                pokemon?.let {
                    bindViewModel(it)
                }
            })
        }
    }

    private fun bindViewModel(pokemon: Pokemon) {
        Log.d(TAG, "bindViewModel ${pokemon}")
        val id = pokemon.id
        activity?.title = pokemon.name

        Glide.with(requireActivity()).apply {
            load(FRONT_DEFAULT_URL_FORMAT.format(id)).into(front_default)
            load(FRONT_SHINY_URL_FORMAT.format(id)).into(front_shiny)
            load(BACK_DEFAULT_URL_FORMAT.format(id)).into(back_default)
            load(BACK_SHINY_URL_FORMAT.format(id)).into(back_shiny)
        }

        description.text = "${pokemon.height * 10} cm / ${pokemon.weight / 10} kg"
    }
}