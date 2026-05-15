package tachiyomi.presentation.core.components.material

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import tachiyomi.presentation.core.components.Pill

@Composable
fun TabText(text: String, badgeCount: Int? = null) {
    // M3 Expressive: warna pill lebih kontras dan expressive
    val pillAlpha = if (isSystemInDarkTheme()) 0.16f else 0.12f

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (badgeCount != null) {
            // M3 Expressive: pakai secondaryContainer untuk badge tab
            Pill(
                text = "$badgeCount",
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = pillAlpha),
                fontSize = 10.sp,
            )
        }
    }
}
