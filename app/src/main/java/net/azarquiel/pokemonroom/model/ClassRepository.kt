package net.azarquiel.pokemonroom.model

import android.app.Application

class PokemonRepository(application: Application) {

    val pokemonDao = PokemonDB.getDatabase(application)!!.pokemonDao()
    // select
    fun getPokemons():List<Pokemon>{
        return pokemonDao.getPokemons()
    }
}

class AbilityRepository(application: Application){
    val abilityDao=PokemonDB.getDatabase(application)!!.abilityDao()

    fun getAbility(pokemon_Id:Int):List<Abilitie>{
        return abilityDao.getAbilities(pokemon_Id)
    }
}

class TypeRepository(application: Application){
    val typeDao=PokemonDB.getDatabase(application)!!.typeDao()

    fun getType(pokemon_Id:Int):List<Type>{
        return typeDao.getTypes(pokemon_Id)
    }
}