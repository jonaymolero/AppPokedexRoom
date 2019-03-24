package net.azarquiel.pokemonroom.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rowpokemon.view.*
import net.azarquiel.pokemonroom.model.Pokemon
import net.azarquiel.pokemonroom.model.Type
import net.azarquiel.pokemonroom.viewmodel.TypeViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

@Suppress("DEPRECATION")
/**
 * Created by pacopulido on 9/10/18.
 */
class CustomAdapterPokemon(val context: Context,
                    val layout: Int,
                    val typeViewModel:TypeViewModel
                    ) : RecyclerView.Adapter<CustomAdapterPokemon.ViewHolder>() {

    private var dataList: List<Pokemon> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context, typeViewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal fun setPokemon(pokemon: List<Pokemon>) {
        this.dataList = pokemon
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context, val typeViewModel: TypeViewModel) : RecyclerView.ViewHolder(viewlayout) {
        private lateinit var tipos: List<Type>

        fun bind(dataItem: Pokemon){
            itemView.tag=dataItem
            itemView.tvNombrePokemonRow.text=dataItem.name
            Picasso.with(context).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${dataItem.pokemon_id}.png").into(itemView.ivFotoPokemonRow)
            tipos= emptyList()
            doAsync {
                tipos=typeViewModel.getTypes(dataItem.pokemon_id)
                uiThread {
                    pintarTipos()
                }
            }
        }

        private fun pintarTipos() {
            var lp= LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.setMargins(0,0,20,0)
            itemView.lyTiposRow.removeAllViews()
            for(tipo in tipos){
                var texto= TextView(context)
                var id=context.resources.getIdentifier(tipo.name.toLowerCase(), "color", context.packageName)
                texto.text=tipo.name
                texto.layoutParams=lp
                texto.setBackgroundColor(context.resources.getColor(id))
                texto.setTextColor(Color.WHITE)
                itemView.lyTiposRow.addView(texto)
            }
        }


    }
}