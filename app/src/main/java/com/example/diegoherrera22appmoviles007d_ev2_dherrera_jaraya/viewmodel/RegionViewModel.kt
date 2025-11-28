package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.RegionComunaRepository



//este de aqqui no solo sirve para las regiones, si no tambien para las comunas
class RegionViewModel (application: Application) : AndroidViewModel(application) {

    val regionesComunas: Map<String, List<String>> by lazy {
        RegionComunaRepository.cargarDesdeAssets(application)
    }

    val regiones: List<String>
        get() = regionesComunas.keys.toList()

    fun comunasDe(region: String): List<String> {
        return regionesComunas[region].orEmpty()
    }
}
