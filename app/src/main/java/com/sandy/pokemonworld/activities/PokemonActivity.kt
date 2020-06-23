package com.sandy.pokemonworld.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
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
class PokemonActivity : AppCompatActivity() {

    companion object {
        const val TAG = "PokemonActivity"
    }

    @Inject
    lateinit var repository: PokemonRepository
    lateinit var pokemonAdapter: PokemonListAdapter
    lateinit var gridLayoutManager: GridLayoutManager

    private val viewModel: PokemonListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu!!.findItem(R.id.search)
        val searchView = searchMenuItem.actionView as SearchView
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isIconified = false
            isIconifiedByDefault = false

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    setQuery(query, false)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { viewModel.onQueryTextChange(newText) }
                    return false
                }
            })
        }
        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                searchView.setQuery("", false)
                return true
            }
        })
        return true
    }

    private fun initViews() {
        pokemonAdapter = PokemonListAdapter(this)
        gridLayoutManager = GridLayoutManager(
            this@PokemonActivity,
            SPAN_COUNT,
            GridLayoutManager.VERTICAL,
            false
        )
        rv_pokemons.apply {
            adapter = pokemonAdapter
            layoutManager = gridLayoutManager
        }
    }

    private fun initViewModel() {
        viewModel.pokemonItems.observe(this, Observer {
            pokemonAdapter.submitList(it)
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            loading_indicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.fetchPokemonItems()

        rv_pokemons.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisiblePosition = gridLayoutManager.findLastVisibleItemPosition()
                if (lastVisiblePosition > 0 && lastVisiblePosition == pokemonAdapter.itemCount - 1) {
                    viewModel.fetchPokemonItems(fetchMore = true)
                }
            }
        })
    }
}