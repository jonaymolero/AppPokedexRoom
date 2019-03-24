package net.azarquiel.pokemonroom.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "pokemon")
data class Pokemon(@PrimaryKey
                   var pokemon_id: Int,
                   var name:String):Serializable
@Entity(tableName = "abilities")
data class Abilitie(@PrimaryKey
                    var ability_id: Int,
                    var name:String,
                    var description:String):Serializable
@Entity(tableName = "types")
data class Type(@PrimaryKey
                var type_id: Int,
                var name:String):Serializable

@Entity(tableName = "pokemon_abilities",
        foreignKeys = [ForeignKey(entity = Pokemon::class,
                parentColumns = arrayOf("pokemon_id"),
                childColumns = arrayOf("pokemon_id")),
            ForeignKey(entity = Abilitie::class,
                    parentColumns = arrayOf("ability_id"),
                    childColumns = arrayOf("ability_id"))])
data class PokemonAbilitie(@PrimaryKey
                           var pokemon_ability_id: Int,
                           var pokemon_id:Int,
                           var ability_id:Int):Serializable
@Entity(tableName = "pokemon_types",
        foreignKeys = [
            ForeignKey(entity = Pokemon::class,
                    parentColumns = arrayOf("pokemon_id"),
                    childColumns = arrayOf("pokemon_id")),
            ForeignKey(entity = Type::class,
                    parentColumns = arrayOf("type_id"),
                    childColumns = arrayOf("type_id"))])
data class PokemonType(@PrimaryKey
                       var pokemon_type_id: Int,
                       var pokemon_id:Int,
                       var type_id:Int):Serializable
