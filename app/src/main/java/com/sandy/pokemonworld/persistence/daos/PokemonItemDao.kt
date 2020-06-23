package com.sandy.pokemonworld.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sandy.pokemonworld.persistence.entities.PokemonItem

@Dao
interface PokemonItemDao {

    @Query("SELECT * from pokemon_item_table ORDER BY id ASC")
    fun getPokemonItems(): LiveData<List<PokemonItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg pokemonItem: PokemonItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemonItems: List<PokemonItem>)

    @Query("DELETE FROM pokemon_item_table")
    suspend fun deleteAll()
}