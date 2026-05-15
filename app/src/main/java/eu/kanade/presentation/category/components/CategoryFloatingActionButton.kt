package eu.kanade.presentation.category.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.i18n.stringResource
import tachiyomi.presentation.core.util.shouldExpandFAB

@Composable
fun CategoryFloatingActionButton(
    lazyListState: LazyListState,
    onCreate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val expanded = lazyListState.shouldExpandFAB()

    // M3 Expressive: spring animation pada elevation saat expand/collapse
    val elevation by animateDpAsState(
        targetValue = if (expanded) 6.dp else 3.dp,
        animationSpec = spring(),
        label = "fabElevation",
    )

    ExtendedFloatingActionButton(
        text = { Text(text = stringResource(MR.strings.action_add)) },
        icon = { Icon(imageVector = Icons.Outlined.Add, contentDescription = null) },
        onClick = onCreate,
        expanded = expanded,
        modifier = modifier,
        // M3 Expressive: gunakan large shape (full rounded) dan warna primary
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = elevation,
            pressedElevation = 2.dp,
            focusedElevation = elevation,
            hoveredElevation = (elevation.value + 2).dp,
        ),
    )
}
