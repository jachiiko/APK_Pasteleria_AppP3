package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Region
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.User
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun loadMe() {
        viewModelScope.launch {
            _isLoading.value = true
            val me = repository.getMe()
            _user.value = me
            _message.value = if (me == null) "No se pudo cargar el usuario" else null
            _isLoading.value = false
        }
    }

    fun updateProfile(
        nombre: String,
        apellido: String,
        rut: String,
        direccion: String,
        comuna: String,
        region: Region
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val updated = repository.updateMyProfile(
                nombre = nombre,
                apellido = apellido,
                rut = rut,
                direccion = direccion,
                comuna = comuna,
                region = region
            )
            updated.onSuccess {
                _user.value = it
                _message.value = "Datos guardados"
            }.onFailure { error ->
                val errorMessage = when {
                    error is HttpException && (error.code() == 401 || error.code() == 403) ->
                        "Sesión expirada"
                    else -> "No se pudo actualizar el usuario"
                }
                _message.value = errorMessage
            }
            _isLoading.value = false
        }
    }

    fun consumeMessage() {
        _message.value = null
    }
}