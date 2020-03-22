package com.test.pokedex.Activities;

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.test.pokedex.R
import java.util.*

 class ActivityItem : AppCompatActivity() {
    private var contexto: Context = this
    private lateinit var data: JsonObject
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var pokedex: String = "0"
    private lateinit var nombre: TextView
    private lateinit var imagen: ImageView
    private lateinit var pokemontipos : String
    private lateinit var pokemonbase : String
    private lateinit var pokemonmovimientos : String

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_list)
        manageIntent()
        initializeComponents()
        initializeData()
    }

    private fun manageIntent(){
        if(! intent == null){
            pokedex = intent.getStringExtra("pokedex")
        }
    }

    private fun initializeComponents(){
        nombre = findViewById(R.id.pokemon_name)
        imagen = findViewById(R.id.pokemon_image)
    }

    private fun initializeData(){

        Ion.with(contexto)
            .load("https://pokeapi.co/api/v2/pokemon/" + pokedex + "/")
            .asJsonObject()
            .done{
                e,result ->
                if(e == null){
                    data = result
                    if(!data.get("sprites").isJsonNull){
                        if(data.get("sprites").asJsonObject.get("front_default") != null){
                            Glide
                                .with(contexto)
                                .load(data.get("sprites").asJsonObject.get("front_default").asString)
                                .placeholder(R.drawable.pokemon_logo_min)
                                .into(imagen)
                        }
                        else{
                            imagen.setImageDrawable(
                                ContextCompat.getDrawable( contexto,
                                    R.drawable.pokemon_logo_min)
                            )
                        }
                    }
                    else{
                        imagen.setImageDrawable(ContextCompat.getDrawable(contexto,
                            R.drawable.pokemon_logo_min))
                        Log.e("JSON","No hay Sprite")
                    }

                    if(data.get("name") != null){
                        val completoNombre: String =
                            "#$pokedex: " + (this.data.get("name").toString().replace(
                                "\"",
                                ""
                            ))
                        nombre.text = completoNombre
                    }else{
                        nombre.text="No disponible por ahora"
                    }
                    if(! data.get("types").isJsonNull){
                        val tipos_aux = data.get("types").asJsonArray
                        for (n in 0.until(tipos_aux.size())){
                            val subtipo = tipos_aux.get(n).asJsonObject.get("type").asJsonObject
                            val nombreTipo = subtipo.get("name").toString()
                            pokemontipos+=nombreTipo
                        }
                        var id_tipos : TextView = findViewById(R.id.pokemon_types)
                        id_tipos.text = pokemontipos
                    }
                    else{
                        Log.e("Json Error", "No hay información")
                    }
                    if(!data.get("moves").isJsonNull){
                        val movimientos_aux = data.get("moves").asJsonArray
                        for(n in 0.until(movimientos_aux.size())){
                            val nombre_movimiento = movimientos_aux.get(n).asJsonObject.get("move")
                            val nombre_string = nombre_movimiento.toString()
                            pokemonmovimientos+=nombre_string
                        }
                    }
                    else{
                        Log.e("JsoN eRROR","No hay informaciòn disponible")
                    }

                }
            }

    }






}
