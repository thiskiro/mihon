package tachiyomi.presentation.core.components.material

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * M3 Expressive Center-aligned Navigation Rail
 * - Rounded corners pada sisi kanan
 * - Shadow elevation untuk efek floating
 *
 * @see [androidx.compose.material3.NavigationRail]
 */
@Composable
fun NavigationRail(
    modifier: Modifier = Modifier,
    // M3 Expressive: surfaceContainer konsisten dengan NavigationBar
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = contentColorFor(containerColor),
    header: @Composable (ColumnScope.() -> Unit)? = null,
    windowInsets: WindowInsets = NavigationRailDefaults.windowInsets,
    content: @Composable ColumnScope.() -> Unit,
) {
    androidx.compose.material3.Surface(
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier,
        // M3 Expressive: rounded corners di sisi kanan
        shape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
        tonalElevation = 3.dp,
        shadowElevation = 4.dp,
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .windowInsetsPadding(windowInsets)
                .widthIn(min = 80.dp)
                .padding(vertical = MaterialTheme.padding.small)
                .selectableGroup(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.padding.extraSmall,
                alignment = Alignment.CenterVertically,
            ),
        ) {
            if (header != null) {
                header()
                Spacer(Modifier.height(MaterialTheme.padding.small))
            }
            content()
        }
    }
}
