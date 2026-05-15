package tachiyomi.presentation.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

// M3 Expressive: header untuk section titles
val Typography.header: TextStyle
    @Composable
    get() = titleSmall.copy(
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
    )

// M3 Expressive: subtitle untuk deskripsi sekunder
val Typography.subtitle: TextStyle
    @Composable
    get() = bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

// M3 Expressive: label untuk badge/chip/pill
val Typography.expressiveLabel: TextStyle
    @Composable
    get() = labelMedium.copy(
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        fontWeight = FontWeight.Medium,
    )
