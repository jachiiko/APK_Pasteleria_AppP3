package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SoftPink,
    secondary = Chocolate,
    tertiary = Chocolate,
    background = Color(0xFF2B1D15),
    surface = Color(0xFF35251C),
    onPrimary = Color(0xFF2B1D15),
    onSecondary = PastelCream,
    onTertiary = PastelCream,
    onBackground = PastelCream,
    onSurface = PastelCream,
    onSurfaceVariant = LightGrayText
)

private val LightColorScheme = lightColorScheme(
    primary = SoftPink,
    secondary = Chocolate,
    tertiary = Chocolate,
    background = PastelCream,
    surface = PastelCream,
    onPrimary = Color.White,
    onSecondary = PastelCream,
    onTertiary = PastelCream,
    onBackground = DarkBrownText,
    onSurface = DarkBrownText,
    onSurfaceVariant = LightGrayText
)

@Composable
fun pastelButtonColors() = ButtonDefaults.buttonColors(
    containerColor = SoftPink,
    contentColor = Color.White
)

@Composable
fun pastelTextButtonColors() = ButtonDefaults.textButtonColors(
    contentColor = Chocolate
)

@Composable
fun pastelOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = SoftPink,
    unfocusedBorderColor = SoftPink.copy(alpha = 0.6f),
    cursorColor = Chocolate,
    focusedLabelColor = Chocolate,
    unfocusedLabelColor = Chocolate.copy(alpha = 0.9f),
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedTextColor = DarkBrownText,
    unfocusedTextColor = DarkBrownText
)

@Composable
fun DiegoHerrera22AppMoviles007D_EV2_DHerrera_JArayaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}