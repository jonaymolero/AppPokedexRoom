package net.azarquiel.pokemonroom.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Room
import android.content.Context

@Database(entities = [Pokemon::class, Abilitie::class, Type::class,PokemonAbilitie::class,PokemonType::class], version = 1)
abstract class PokemonDB: RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun abilityDao():AbilityDao
    abstract fun typeDao():TypeDao
    companion object {
        @Volatile
        private var INSTANCE: PokemonDB? = null

        fun getDatabase(context: Context): PokemonDB? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            PokemonDB::class.java, "pokemon.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}