package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository

import android.content.Context
import org.json.JSONObject


//este de aqui se encarga de obtener y procesar la informacion del regiones y comunas que estan en un json
object RegionComunaRepository {

    fun cargarDesdeAssets(context: Context): Map<String, List<String>> {
        // Abre el archivo desde assets
        val inputStream = context.assets.open("regiones_comunas.json")
        val json = inputStream.bufferedReader().use { it.readText() }

        // Convierte el JSON en un mapa usable
        val jsonObject = JSONObject(json)
        val map = mutableMapOf<String, List<String>>()

        jsonObject.keys().forEach { region ->
            val comunasArray = jsonObject.getJSONArray(region)
            val comunas = List(comunasArray.length()) { i ->
                comunasArray.getString(i)
            }
            map[region] = comunas
        }

        return map
    }
}