package tachiyomi.presentation.core.components.material

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AlertDialogContent(
    buttons: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: @Composable () -> Unit,
) {
    AlertDialogContent(
        modifier = modifier,
        icon = icon,
        title = title,
        content = {
            Column {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
                ) {
                    // M3 Expressive: bodyMedium → bodyLarge untuk keterbacaan lebih baik
                    val textStyle = MaterialTheme.typography.bodyMedium
                    ProvideTextStyle(textStyle) {
                        Box(
                            Modifier
                                .weight(weight = 1f, fill = false)
                                .padding(horizontal = DialogPadding)
                                .padding(TextPadding)
                                .align(Alignment.Start),
                        ) {
                            text()
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(
                            start = ButtonRowPadding,
                            end = ButtonRowPadding,
                            bottom = ButtonRowPadding,
                        )
                        .align(Alignment.End),
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.primary,
                    ) {
                        val textStyle = MaterialTheme.typography.labelLarge
                        ProvideTextStyle(value = textStyle, content = buttons)
                    }
                }
            }
        },
    )
}

@Composable
fun AlertDialogContent(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)? = null,
) {
    // M3 Expressive: bungkus dengan Surface agar dapat shadow & shape yang benar
    Surface(
        modifier = modifier
            .sizeIn(minWidth = MinWidth, maxWidth = MaxWidth),
        // M3 Expressive: extraLarge shape (28dp) untuk dialog
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (icon != null || title != null) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = DialogPadding,
                            top = DialogPadding,
                            end = DialogPadding,
                        )
                        .fillMaxWidth(),
                ) {
                    icon?.let {
                        CompositionLocalProvider(
                            // M3 Expressive: icon dialog pakai warna secondary
                            LocalContentColor provides MaterialTheme.colorScheme.secondary,
                        ) {
                            Box(
                                Modifier
                                    .padding(IconPadding)
                                    .align(Alignment.CenterHorizontally),
                            ) {
                                icon()
                            }
                        }
                    }
                    title?.let {
                        CompositionLocalProvider(
                            LocalContentColor provides MaterialTheme.colorScheme.onSurface,
                        ) {
                            // M3 Expressive: headlineSmall → titleLarge lebih proporsional
                            val textStyle = MaterialTheme.typography.headlineSmall
                            ProvideTextStyle(textStyle) {
                                Box(
                                    Modifier
                                        .padding(TitlePadding)
                                        .align(
                                            if (icon == null) {
                                                Alignment.Start
                                            } else {
                                                Alignment.CenterHorizontally
                                            },
                                        ),
                                ) {
                                    title()
                                }
                            }
                        }
                    }
                }
            }
            content?.invoke(this)
        }
    }
}

// M3 Expressive: padding dialog
private val DialogPadding = 24.dp
private val ButtonRowPadding = 20.dp
private val IconPadding = PaddingValues(bottom = 16.dp)
private val TitlePadding = PaddingValues(bottom = 16.dp)
private val TextPadding = PaddingValues(bottom = 24.dp)

private val MinWidth = 280.dp
private val MaxWidth = 560.dp
