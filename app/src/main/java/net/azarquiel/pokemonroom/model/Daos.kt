package net.azarquiel.pokemonroom.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

@Dao
interface PokemonDao {
    @Query("SELECT * from pokemon ORDER BY pokemon_id ASC")
    fun getPokemons(): List<Pokemon>
}

@Dao
interface AbilityDao {
    @Query("select abilities.ability_id, name, description from abilities, pokemon_abilities where abilities.ability_id=pokemon_abilities.ability_id and pokemon_abilities.pokemon_id=:pokemon_id")
    fun getAbilities(pokemon_id: Int): List<Abilitie>
}

@Dao
interface TypeDao {
    @Query("select types.type_id, name from types, pokemon_types where types.type_id=pokemon_types.type_id and pokemon_types.pokemon_id=:pokemon_id")
    fun getTypes(pokemon_id: Int): List<Type>
}


