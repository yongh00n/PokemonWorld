package com.sandy.pokemonworld.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sandy.pokemonworld.R
import com.sandy.pokemonworld.adapters.PokemonListAdapter
import com.sandy.pokemonworld.repositories.PokemonRepository
import com.sandy.pokemonworld.viewmodels.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import javax.inject.Inject

@AndroidEntryPoint
class PokemonListFragment: Fragment() {

    companion object {
        const val TAG = "PokemonListFragment"
        const val SPAN_COUNT = 3
    }

    @Inject
    lateinit var repository: PokemonRepository
    lateinit var pokemonAdapter: PokemonListAdapter
    lateinit var gridLayoutManager: GridLayoutManager

    private val viewModel: PokemonListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pokemon_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "###### findNavController=${findNavController()}")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        initSearchView(menu)
    }

    private fun initViews() {
        pokemonAdapter = PokemonListAdapter(requireContext())
        gridLayoutManager = GridLayoutManager(
            context,
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
        viewModel.pokemonItems.observe(viewLifecycleOwner, Observer {
            pokemonAdapter.submitList(it)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
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

    private fun initSearchView(menu: Menu) {
        activity?.let { activity ->
            val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val searchMenuItem = menu!!.findItem(R.id.search)
            val searchView = searchMenuItem.actionView as SearchView
            searchView.apply {
                setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
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
        }
    }
}