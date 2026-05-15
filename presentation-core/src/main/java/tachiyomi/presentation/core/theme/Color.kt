package tachiyomi.presentation.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// M3 Expressive: warna "active" untuk highlight chapter terbaru, badge, dll
val ColorScheme.active: Color
    @Composable
    get() {
        return if (isSystemInDarkTheme()) Color(0xFFFFEB3B) else Color(0xFFFFC107)
    }

// M3 Expressive: warna warning yang lebih expressive
val ColorScheme.warning: Color
    @Composable
    get() {
        return if (isSystemInDarkTheme()) Color(0xFFFFB74D) else Color(0xFFF57C00)
    }

// M3 Expressive: warna success untuk downloaded/completed status
val ColorScheme.success: Color
    @Composable
    get() {
        return if (isSystemInDarkTheme()) tertiary else tertiary
    }

// M3 Expressive: overlay color untuk modal/sheet backgrounds
val ColorScheme.harmonizedSurface: Color
    @Composable
    get() {
        return if (isSystemInDarkTheme()) {
            surfaceContainerHigh
        } else {
            surfaceContainerLow
        }
    }
