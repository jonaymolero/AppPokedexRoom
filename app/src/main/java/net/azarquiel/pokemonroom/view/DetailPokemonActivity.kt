package net.azarquiel.pokemonroom.view

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_detail_pokemon.*
import kotlinx.android.synthetic.main.content_detail_pokemon.*
import net.azarquiel.pokemonroom.R
import net.azarquiel.pokemonroom.model.Abilitie
import net.azarquiel.pokemonroom.model.Pokemon
import net.azarquiel.pokemonroom.model.Type
import net.azarquiel.pokemonroom.viewmodel.AbilityViewModel
import net.azarquiel.pokemonroom.viewmodel.TypeViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

@Suppress("DEPRECATION")
class DetailPokemonActivity : AppCompatActivity() {

    private lateinit var pokemon:Pokemon
    private lateinit var abilitiesViewModel:AbilityViewModel
    private lateinit var typeViewModel:TypeViewModel
    private lateinit var listaTipos:List<Type>
    private lateinit var listaHabilidades:List<Abilitie>
    private lateinit var favoritos: SharedPreferences
    private var isFavorito:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pokemon)
        setSupportActionBar(toolbar)

        pokemon=intent.getSerializableExtra("pokemonpulsado") as Pokemon
        favoritos = getSharedPreferences("favoritos", Context.MODE_PRIVATE)

        title="${pokemon.pokemon_id}. ${pokemon.name}"

        abilitiesViewModel = ViewModelProviders.of(this).get(AbilityViewModel::class.java)
        typeViewModel = ViewModelProviders.of(this).get(TypeViewModel::class.java)

        doAsync {
            listaTipos=typeViewModel.getTypes(pokemon.pokemon_id)
            listaHabilidades=abilitiesViewModel.getAbilities(pokemon.pokemon_id)
            uiThread {
                pintarPokemon()
            }
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun comprobarFavoritos(menu: Menu) {
        val jsonPokemon = favoritos.getString(pokemon.pokemon_id.toString(),"nosta")
        val item=menu.findItem(R.id.favoritos)
        if(!jsonPokemon.equals("nosta")){
            item.setIcon(android.R.drawable.btn_star_big_on)
            isFavorito=true
        }
    }

    private fun pintarPokemon() {
        var lp= LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(0,0,20,0)

        Picasso.with(this).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.pokemon_id}.png").into(ivPokemonDetail)
        tvNombrePokemonDetail.text=pokemon.name

        for (tipos in listaTipos){
            var texto=TextView(this)
            var id=resources.getIdentifier(tipos.name.toLowerCase(), "color", packageName)
            texto.text=tipos.name
            texto.layoutParams=lp
            texto.setBackgroundColor(resources.getColor(id))
            texto.setTextColor(Color.WHITE)
            lyTiposDetail.addView(texto)
        }

        for(habilidad in listaHabilidades){
            var texto=TextView(this)
            texto.text=habilidad.name
            texto.layoutParams=lp
            lyAbilitiesDetail.addView(texto)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        comprobarFavoritos(menu)
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

    private fun sacarFavoritos(item: MenuItem): Boolean {
        isFavorito=!isFavorito
        if(isFavorito){
            item.setIcon(android.R.drawable.btn_star_big_on)
        }else{
            item.setIcon(android.R.drawable.btn_star_big_off)
        }
        return true
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("pokemonDevuelto", pokemon)
        intent.putExtra("isFavorito", isFavorito)
        setResult(Activity.RESULT_OK,intent)
        super.onBackPressed()
    }
}
