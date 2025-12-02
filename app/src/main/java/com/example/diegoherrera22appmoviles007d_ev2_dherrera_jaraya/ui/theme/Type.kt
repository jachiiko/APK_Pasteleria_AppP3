package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.unit.sp
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.R

// PROVEEDOR (API correcta para tu versión)
private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// FUENTES
private val lato = GoogleFont("Lato")
private val pacifico = GoogleFont("Pacifico")

// FAMILIAS
private val latoFamily = FontFamily(
    Font(googleFont = lato, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = lato, fontProvider = provider, weight = FontWeight.Bold)
)

private val pacificoFamily = FontFamily(
    Font(googleFont = pacifico, fontProvider = provider, weight = FontWeight.Normal)
)

// TYPOGRAPHY
val Typography = Typography(
    displaySmall = TextStyle(
        fontFamily = pacificoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = latoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)
