package com.sandy.pokemonworld.persistence.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sandy.pokemonworld.persistence.daos.PokemonDao
import com.sandy.pokemonworld.persistence.daos.PokemonItemDao
import com.sandy.pokemonworld.persistence.entities.Pokemon
import com.sandy.pokemonworld.persistence.entities.PokemonItem

@Database(entities = [PokemonItem::class, Pokemon::class], version = 1, exportSchema = false)
abstract class PokemonDatabase: RoomDatabase() {

    abstract fun pokemonItemDao(): PokemonItemDao

    abstract fun pokemonDao(): PokemonDao
}
