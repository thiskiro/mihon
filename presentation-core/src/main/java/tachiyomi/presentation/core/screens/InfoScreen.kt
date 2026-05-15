package tachiyomi.presentation.core.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import tachiyomi.presentation.core.components.material.Button
import tachiyomi.presentation.core.components.material.ButtonDefaults
import tachiyomi.presentation.core.components.material.Scaffold
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.util.secondaryItemAlpha

@Composable
fun InfoScreen(
    icon: ImageVector,
    headingText: String,
    subtitleText: String,
    acceptText: String,
    onAcceptClick: () -> Unit,
    canAccept: Boolean = true,
    rejectText: String? = null,
    onRejectClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        bottomBar = {
            val borderColor = MaterialTheme.colorScheme.outlineVariant
            // M3 Expressive: Surface dengan elevation untuk bottom bar
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 2.dp,
                shadowElevation = 8.dp,
            ) {
                Column(
                    modifier = Modifier
                        .drawBehind {
                            drawLine(
                                borderColor,
                                Offset(0f, 0f),
                                Offset(size.width, 0f),
                                Dp.Hairline.value,
                            )
                        }
                        .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                        .padding(
                            horizontal = MaterialTheme.padding.medium,
                            vertical = MaterialTheme.padding.small,
                        ),
                ) {
                    // M3 Expressive: pakai custom Button dengan extraLarge shape
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = canAccept,
                        onClick = onAcceptClick,
                        colors = ButtonDefaults.buttonColors(),
                    ) {
                        Text(
                            text = acceptText,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                    if (rejectText != null && onRejectClick != null) {
                        Spacer(modifier = Modifier.height(MaterialTheme.padding.extraSmall))
                        // M3 Expressive: OutlinedButton dengan shape konsisten
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onRejectClick,
                            shape = MaterialTheme.shapes.extraLarge,
                        ) {
                            Text(text = rejectText)
                        }
                    }
                }
            }
        },
    ) { paddingValues ->
        // Status bar scrim
        Box(
            modifier = Modifier
                .zIndex(2f)
                .secondaryItemAlpha()
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .height(paddingValues.calculateTopPadding()),
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(top = 48.dp)
                .padding(horizontal = MaterialTheme.padding.medium),
        ) {
            // M3 Expressive: icon container dengan background primaryContainer
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(bottom = MaterialTheme.padding.medium),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(MaterialTheme.padding.small)
                        .size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }

            Text(
                text = headingText,
                // M3 Expressive: headlineLarge tetap untuk impact
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = subtitleText,
                modifier = Modifier
                    .secondaryItemAlpha()
                    .padding(vertical = MaterialTheme.padding.small),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            content()
        }
    }
}

@PreviewLightDark
@Composable
private fun InfoScaffoldPreview() {
    InfoScreen(
        icon = Icons.Outlined.Newspaper,
        headingText = "Heading",
        subtitleText = "Subtitle",
        acceptText = "Accept",
        onAcceptClick = {},
        rejectText = "Reject",
        onRejectClick = {},
    ) {
        Text("Hello world")
    }
}
