package net.azarquiel.pokemonroom.view

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import net.azarquiel.pokemonroom.R
import net.azarquiel.pokemonroom.adapter.CustomAdapterPokemon
import net.azarquiel.pokemonroom.model.Pokemon
import net.azarquiel.pokemonroom.util.Util
import net.azarquiel.pokemonroom.viewmodel.PokemonViewModel
import net.azarquiel.pokemonroom.viewmodel.TypeViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_ADD=0
    }

    private lateinit var pokemonViewModel: PokemonViewModel
    private lateinit var tipoViewModel: TypeViewModel
    private lateinit var pokemons: List<Pokemon>
    private lateinit var adapter:CustomAdapterPokemon
    private lateinit var favoritos: SharedPreferences
    private lateinit var listaPokemonFavortios:ArrayList<Pokemon>
    private var isFavorito:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Util.inyecta(this)

        title="Todos los pokemon"

        favoritos = getSharedPreferences("favoritos", Context.MODE_PRIVATE)
        listaPokemonFavortios= ArrayList()
        pokemonViewModel = ViewModelProviders.of(this).get(PokemonViewModel::class.java)
        tipoViewModel = ViewModelProviders.of(this).get(TypeViewModel::class.java)

        createAdapter()
        datosSharePreferences()

        doAsync {
            pokemons=pokemonViewModel.getPokemons()
            uiThread {
                adapter.setPokemon(pokemons)
            }
        }
        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/
    }

    private fun datosSharePreferences() {
        val pokemonShare = favoritos.all
        for (entry in pokemonShare.entries) {
            val jsonPokemon=entry.value.toString()
            val pokemonLista: Pokemon= Gson().fromJson(jsonPokemon, Pokemon::class.java)
            listaPokemonFavortios.add(pokemonLista)
        }
    }

    private fun createAdapter() {
        adapter= CustomAdapterPokemon(this,R.layout.rowpokemon,tipoViewModel)
        rvPokemon.layoutManager=LinearLayoutManager(this)
        rvPokemon.adapter=adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.favoritos -> sacarFavoritos(item)
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val pokemonDetail = data!!.getSerializableExtra("pokemonDevuelto") as Pokemon
            val isAdd=data!!.getBooleanExtra("isFavorito",false)
            val editor = favoritos.edit()
            if(!favoritos.contains(pokemonDetail.pokemon_id.toString())){
                if(isAdd){
                    val jsonPokemon: String = Gson().toJson(pokemonDetail)
                    editor.putString(pokemonDetail.pokemon_id.toString(), jsonPokemon)
                    listaPokemonFavortios.add(pokemonDetail)
                }
            }
            if(!isAdd){
                 editor.remove(pokemonDetail.pokemon_id.toString())
                 listaPokemonFavortios.remove(pokemonDetail)
            }
            editor.commit()
            if(isFavorito){
                adapter.setPokemon(listaPokemonFavortios)
            }
        }
    }

    private fun sacarFavoritos(item: MenuItem): Boolean {
        isFavorito=!isFavorito
        if(isFavorito){
            title="Pokemon favoritos"
            item.setIcon(android.R.drawable.btn_star_big_on)
            adapter.setPokemon(listaPokemonFavortios)
        }else{
            title="Todos los pokemon"
            item.setIcon(android.R.drawable.btn_star_big_off)
            adapter.setPokemon(pokemons)
        }
        return true
    }

    fun pulsarPokemon(v:View){
        var pokemon=v.tag as Pokemon
        var intent= Intent(this, DetailPokemonActivity::class.java)
        intent.putExtra("pokemonpulsado", pokemon)
        startActivityForResult(intent, REQUEST_ADD)
    }
}
