package eu.kanade.presentation.theme.colorscheme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

internal abstract class BaseColorScheme {

    abstract val darkScheme: ColorScheme
    abstract val lightScheme: ColorScheme

    // M3 Expressive: AMOLED surface containers
    // Cannot be pure black as there's content scrolling behind it
    // https://m3.material.io/components/navigation-bar/guidelines#90615a71-607e-485e-9e09-778bfc080563
    private val surfaceContainer = Color(0xFF0C0C0C)
    private val surfaceContainerHigh = Color(0xFF131313)
    private val surfaceContainerHighest = Color(0xFF1B1B1B)

    fun getColorScheme(
        isDark: Boolean,
        isAmoled: Boolean,
        overrideDarkSurfaceContainers: Boolean,
    ): ColorScheme {
        if (!isDark) return lightScheme

        if (!isAmoled) return darkScheme

        // M3 Expressive: AMOLED dengan surface containers yang lebih kaya
        val amoledScheme = darkScheme.copy(
            background = Color.Black,
            onBackground = Color.White,
            surface = Color.Black,
            onSurface = Color.White,
            // M3 Expressive: pertahankan scrim dan overlay yang lebih baik
            scrim = Color(0xFF000000),
        )

        if (!overrideDarkSurfaceContainers) return amoledScheme

        return amoledScheme.copy(
            surfaceVariant = surfaceContainer, // Navigation bar background (ThemePrefWidget)
            surfaceContainerLowest = Color(0xFF000000),
            surfaceContainerLow = surfaceContainer,
            surfaceContainer = surfaceContainer, // Navigation bar background
            surfaceContainerHigh = surfaceContainerHigh,
            surfaceContainerHighest = surfaceContainerHighest,
        )
    }
}
