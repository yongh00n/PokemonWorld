package com.sandy.pokemonworld.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sandy.pokemonworld.R
import com.sandy.pokemonworld.adapters.PokemonListAdapter
import com.sandy.pokemonworld.repositories.PokemonRepository
import com.sandy.pokemonworld.viewmodels.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val SPAN_COUNT = 3
    }

    @Inject
    lateinit var repository: PokemonRepository
    lateinit var pokemonAdapter: PokemonListAdapter

    //    private val viewModel by viewModels<PokemonListModel>()
    private val viewModel: PokemonListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pokemonAdapter = PokemonListAdapter(this)
        val gridLayoutManager = GridLayoutManager(
            this@MainActivity,
            SPAN_COUNT,
            GridLayoutManager.VERTICAL,
            false
        )
        rv_pokemons.apply {
            adapter = pokemonAdapter
            layoutManager = gridLayoutManager
        }

        viewModel.pokemonItems.observe(this, Observer {
            pokemonAdapter.submitList(it)
        })

        viewModel.isLoading.observe(this, Observer {
            Log.d(TAG, "###### isLoading=${it}")
        })

        viewModel.fetchPokemonItems()

        rv_pokemons.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisiblePosition = gridLayoutManager.findLastVisibleItemPosition()
                if (lastVisiblePosition > 0 && lastVisiblePosition == pokemonAdapter.itemCount - 1) {
                    Log.d(TAG, "###### fetch more")
                    viewModel.fetchPokemonItems(fetchMore = true)
                }
            }
        })
    }
}