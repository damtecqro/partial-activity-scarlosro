package com.test.pokedex.Activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.test.pokedex.R
import kotlinx.android.synthetic.main.activity_list.*
import com.test.pokedex.Adapters.AdapterList

class detailed : AppCompatActivity() {

    private var contexto: Context = this
    private lateinit var data: JsonObject
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var pokedex: String = "0"
    private var aux: String =""
    private var aux2: String = ""
    private var aux3: String = ""
    private lateinit var nombre: TextView
    private lateinit var imagen: ImageView
    private lateinit var pokemontipos : TextView
    private lateinit var pokemonbase : TextView
    private lateinit var pokemonmovimientos : TextView
    private lateinit var adapter: AdapterList



    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        manageIntent()
        initializeComponents()
        initializeData()
    }

    fun AdapterList(context:Context){
        this.contexto = context
    }

    private fun manageIntent(){
        if(intent != null){
            pokedex = intent.getStringExtra("Numero")

        }
    }

    private fun initializeComponents(){
        nombre = findViewById(R.id.pokemon_names)
        imagen = findViewById(R.id.pokemon_image)
        pokemontipos = findViewById(R.id.pokemon_types)
        pokemonmovimientos = findViewById(R.id.pokemon_movimientos)
        pokemonbase = findViewById(R.id.pokemon_base)
    }


    private fun initializeList(){
        linearLayoutManager = LinearLayoutManager(contexto)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.scrollToPosition(0)


        adapter = AdapterList()
       // adapter.AdapterList(contexto,data)

        recycler_view_list.layoutManager = linearLayoutManager
        recycler_view_list.adapter = adapter
        recycler_view_list.setHasFixedSize(true)
        recycler_view_list.itemAnimator = DefaultItemAnimator()
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
                    if(!data.get("types").isJsonNull){
                        aux = "Tipos: \n"
                        val tipos_aux = data.get("types").asJsonArray
                        for (n in 0.until(tipos_aux.size())){
                            val subtipo = tipos_aux.get(n).asJsonObject.get("type").asJsonObject
                            val nombreTipo = subtipo.get("name").toString()
                            aux += nombreTipo
                            aux += "\n"
                        }
                        pokemontipos.text = aux
                    }
                    else{
                        Log.e("Json Error", "No hay información")
                    }
                    if(!data.get("moves").isJsonNull){
                        aux2 = "Movimientos: \n"
                        val movimientos_aux = data.get("moves").asJsonArray
                        for(n in 0.until(movimientos_aux.size())){
                            val nombre_movimiento = movimientos_aux.get(n).asJsonObject.get("move").asJsonObject.get("name")
                            val nombre_string = nombre_movimiento.toString().replace("\"", "")
                            aux2+=nombre_string
                            aux2+="\n"
                        }
                        pokemonmovimientos.text = aux2
                    }
                    else{
                        pokemonmovimientos.text = "No Movimientos"
                        Log.e("JsoN eRROR","No hay informaciòn disponible")
                    }

                    if(!data.get("stats").isJsonNull){
                        aux3 = "Bases \n"
                        val stats = data.get("stats").asJsonArray
                        for(n in 0.until(stats.size())){
                            val basestat = stats.get(n).asJsonObject.get("base_stat")
                            val stat_name = stats.get(n).asJsonObject.get("stat").asJsonObject.get("name")
                            aux3 += stat_name.toString().replace("\"", "")
                            aux3+= "\n"
                        }
                        pokemonbase.text = aux3
                    }
                    else{
                        Log.e("JSON", "Stats bases nulo")
                    }

                }
            }

    }


}
