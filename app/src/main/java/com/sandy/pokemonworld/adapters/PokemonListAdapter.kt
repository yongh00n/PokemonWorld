package com.sandy.pokemonworld.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandy.pokemonworld.R
import com.sandy.pokemonworld.viewmodels.PokemonItemModel

class PokemonListAdapter(context: Context)
    : ListAdapter<PokemonItemModel, RecyclerView.ViewHolder>(PokemonItemModel.DIFF_CALLBACK) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(inflater.inflate(R.layout.view_item_loading, parent, false))
            else -> ItemViewHolder(inflater.inflate(R.layout.view_item_pokemon, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindTo(getItem(position))
        } else if (holder is LoadingViewHolder) {

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isLoadingItem) VIEW_TYPE_LOADING else VIEW_TYPE_DATA
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imageView: ImageView = itemView.findViewById(R.id.pokemon_image)
        private var nameView: TextView = itemView.findViewById(R.id.pokemon_name)

        fun bindTo(model: PokemonItemModel) {
            Glide.with(imageView.context)
                .load(model.photoUrl)
                .into(imageView)
            nameView.text = model.name
            itemView.setOnClickListener {
//                Log.d("XXXX", "clicked on ${model.name}")
            }
        }
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var progresBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
    }

    companion object {
        const val VIEW_TYPE_DATA = 0
        const val VIEW_TYPE_LOADING = 1
    }
}