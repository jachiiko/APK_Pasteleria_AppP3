package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiTopBar(title: String) {
    TopAppBar(
        title = { Text(title) }
    )
}