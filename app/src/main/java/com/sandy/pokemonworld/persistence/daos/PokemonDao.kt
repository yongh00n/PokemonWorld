package com.sandy.pokemonworld.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sandy.pokemonworld.persistence.entities.Pokemon

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon_table")
    fun getAllPokemons(): LiveData<List<Pokemon>>

    @Query("SELECT * FROM pokemon_table WHERE id = :id")
    fun getPokemon(id: Int): LiveData<List<Pokemon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg pokemon: Pokemon)

    @Query("DELETE FROM pokemon_table")
    suspend fun deleteAll()
}