package net.azarquiel.pokemonroom.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import net.azarquiel.pokemonroom.model.*

class PokemonViewModel (application: Application) : AndroidViewModel(application) {

    private var repository: PokemonRepository =
            PokemonRepository(application)

    fun getPokemons():List<Pokemon>{
        return repository.getPokemons()
    }
}

class AbilityViewModel (application: Application) : AndroidViewModel(application) {

    private var repository: AbilityRepository =
            AbilityRepository(application)

    fun getAbilities(pokemon_id:Int):List<Abilitie>{
        return repository.getAbility(pokemon_id)
    }
}
class TypeViewModel (application: Application) : AndroidViewModel(application) {

    private var repository: TypeRepository =
            TypeRepository(application)

    fun getTypes(pokemon_id:Int):List<Type>{
        return repository.getType(pokemon_id)
    }
}
