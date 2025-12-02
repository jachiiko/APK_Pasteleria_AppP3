package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Region
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegionViewModel : ViewModel() {

    private val _regiones = MutableStateFlow<List<Region>>(emptyList())
    val regiones: StateFlow<List<Region>> = _regiones

    // Datos locales para cuando la API no responda
    private val fallbackRegiones = listOf(
        Region(
            id = "13",
            nombre = "Región Metropolitana",
            comunas = listOf("Santiago", "Ñuñoa", "La Florida", "Puente Alto")
        ),
        Region(
            id = "05",
            nombre = "Valparaíso",
            comunas = listOf("Valparaíso", "Viña del Mar", "Quilpué")
        ),
        Region(
            id = "08",
            nombre = "Biobío",
            comunas = listOf("Concepción", "Talcahuano", "Chiguayante")
        )
    )

    init {
        cargarRegiones()
    }

    private fun cargarRegiones() {
        viewModelScope.launch {
            try {
                val data = ApiClient.regionApi.getRegions()
                android.util.Log.d("API_REGIONES", "Regiones recibidas: $data")
                _regiones.value = if (data.isNotEmpty()) data else fallbackRegiones
            } catch (e: Exception) {
                android.util.Log.e("API_REGIONES", "Error cargando regiones", e)
                _regiones.value = fallbackRegiones
            }
        }
    }


    fun nombresRegiones(): List<String> {
        return _regiones.value.map { it.nombre }
    }

    fun comunasDe(regionNombre: String): List<String> {
        return _regiones.value
            .firstOrNull { it.nombre == regionNombre }
            ?.comunas
            ?: emptyList()
    }
}