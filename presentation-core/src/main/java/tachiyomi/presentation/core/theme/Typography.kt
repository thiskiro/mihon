package tachiyomi.presentation.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import tachiyomi.presentation.core.R

// M3 Expressive: Google Sans font family
val GoogleSansFontFamily = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_medium, FontWeight.Medium),
    Font(R.font.google_sans_medium, FontWeight.SemiBold),
    Font(R.font.google_sans_bold, FontWeight.Bold),
)

// M3 Expressive: Typography dengan Google Sans
val GoogleSansTypography = Typography(
    displayLarge = Typography().displayLarge.copy(fontFamily = GoogleSansFontFamily),
    displayMedium = Typography().displayMedium.copy(fontFamily = GoogleSansFontFamily),
    displaySmall = Typography().displaySmall.copy(fontFamily = GoogleSansFontFamily),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = GoogleSansFontFamily),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = GoogleSansFontFamily),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = GoogleSansFontFamily),
    titleLarge = Typography().titleLarge.copy(fontFamily = GoogleSansFontFamily),
    titleMedium = Typography().titleMedium.copy(fontFamily = GoogleSansFontFamily),
    titleSmall = Typography().titleSmall.copy(fontFamily = GoogleSansFontFamily),
    bodyLarge = Typography().bodyLarge.copy(fontFamily = GoogleSansFontFamily),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = GoogleSansFontFamily),
    bodySmall = Typography().bodySmall.copy(fontFamily = GoogleSansFontFamily),
    labelLarge = Typography().labelLarge.copy(fontFamily = GoogleSansFontFamily),
    labelMedium = Typography().labelMedium.copy(fontFamily = GoogleSansFontFamily),
    labelSmall = Typography().labelSmall.copy(fontFamily = GoogleSansFontFamily),
)

// M3 Expressive: header untuk section titles
val Typography.header: TextStyle
    @Composable
    get() = titleSmall.copy(
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
        fontFamily = GoogleSansFontFamily,
    )

// M3 Expressive: subtitle untuk deskripsi sekunder
val Typography.subtitle: TextStyle
    @Composable
    get() = bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontFamily = GoogleSansFontFamily,
    )

// M3 Expressive: label untuk badge/chip/pill
val Typography.expressiveLabel: TextStyle
    @Composable
    get() = labelMedium.copy(
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        fontWeight = FontWeight.Medium,
        fontFamily = GoogleSansFontFamily,
    )
