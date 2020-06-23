package com.sandy.pokemonworld.modules

import android.content.Context
import androidx.room.Room
import com.sandy.pokemonworld.network.PokemonApi
import com.sandy.pokemonworld.persistence.databases.PokemonDatabase
import com.sandy.pokemonworld.persistence.keyvalues.PokemonApiStore
import com.sandy.pokemonworld.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun providePokemonApiStore(@ApplicationContext context: Context): PokemonApiStore {
        return PokemonApiStore(context)
    }
}