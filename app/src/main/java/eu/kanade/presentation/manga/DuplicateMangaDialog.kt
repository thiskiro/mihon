package eu.kanade.presentation.manga

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMaxOfOrNull
import coil3.request.ImageRequest
import coil3.request.crossfade
import eu.kanade.presentation.components.AdaptiveSheet
import eu.kanade.presentation.components.TabbedDialogPaddings
import eu.kanade.presentation.manga.components.MangaCover
import eu.kanade.presentation.more.settings.LocalPreferenceMinHeight
import eu.kanade.presentation.more.settings.widget.TextPreferenceWidget
import eu.kanade.tachiyomi.source.Source
import eu.kanade.tachiyomi.source.model.SManga
import tachiyomi.domain.manga.model.Manga
import tachiyomi.domain.manga.model.MangaWithChapterCount
import tachiyomi.domain.source.model.StubSource
import tachiyomi.domain.source.service.SourceManager
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.Badge
import tachiyomi.presentation.core.components.BadgeGroup
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.i18n.pluralStringResource
import tachiyomi.presentation.core.i18n.stringResource
import tachiyomi.presentation.core.util.secondaryItemAlpha
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

@Composable
fun DuplicateMangaDialog(
    duplicates: List<MangaWithChapterCount>,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onOpenManga: (manga: Manga) -> Unit,
    onMigrate: (manga: Manga) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sourceManager = remember { Injekt.get<SourceManager>() }
    val minHeight = LocalPreferenceMinHeight.current
    val horizontalPadding = PaddingValues(horizontal = TabbedDialogPaddings.Horizontal)
    val horizontalPaddingModifier = Modifier.padding(horizontalPadding)

    AdaptiveSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = TabbedDialogPaddings.Vertical)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.medium),
        ) {
            Text(
                text = stringResource(MR.strings.possible_duplicates_title),
                // M3 Expressive: headlineMedium untuk dialog title
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .then(horizontalPaddingModifier)
                    .padding(top = MaterialTheme.padding.small),
            )

            Text(
                text = stringResource(MR.strings.possible_duplicates_summary),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.then(horizontalPaddingModifier),
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                modifier = Modifier.height(getMaximumMangaCardHeight(duplicates)),
                contentPadding = horizontalPadding,
            ) {
                items(
                    items = duplicates,
                    key = { it.manga.id },
                ) {
                    DuplicateMangaListItem(
                        duplicate = it,
                        getSource = { sourceManager.getOrStub(it.manga.source) },
                        onMigrate = { onMigrate(it.manga) },
                        onDismissRequest = onDismissRequest,
                        onOpenManga = { onOpenManga(it.manga) },
                    )
                }
            }

            Column(modifier = horizontalPaddingModifier) {
                HorizontalDivider()

                TextPreferenceWidget(
                    title = stringResource(MR.strings.action_add_anyway),
                    icon = Icons.Outlined.Add,
                    onPreferenceClick = {
                        onDismissRequest()
                        onConfirm()
                    },
                    modifier = Modifier.clip(CircleShape),
                )
            }

            // M3 Expressive: OutlinedButton dengan extraLarge shape
            OutlinedButton(
                onClick = onDismissRequest,
                modifier = Modifier
                    .then(horizontalPaddingModifier)
                    .padding(bottom = MaterialTheme.padding.medium)
                    .heightIn(min = minHeight)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Text(
                    modifier = Modifier.padding(vertical = MaterialTheme.padding.extraSmall),
                    text = stringResource(MR.strings.action_cancel),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
private fun DuplicateMangaListItem(
    duplicate: MangaWithChapterCount,
    getSource: () -> Source,
    onDismissRequest: () -> Unit,
    onOpenManga: () -> Unit,
    onMigrate: () -> Unit,
) {
    val source = getSource()
    val manga = duplicate.manga
    Column(
        modifier = Modifier
            .width(MangaCardWidth)
            // M3 Expressive: extraLarge shape untuk card manga
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .combinedClickable(
                onLongClick = { onOpenManga() },
                onClick = {
                    onDismissRequest()
                    onMigrate()
                },
            ),
    ) {
        MangaCover.Book(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge),
            data = ImageRequest.Builder(LocalContext.current)
                .data(manga)
                .crossfade(true)
                .build(),
        )

        Column(
            modifier = Modifier.padding(MaterialTheme.padding.small),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
        ) {
            Text(
                text = manga.title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            val badgeInfo = getMangaBadges(duplicate, source)
            badgeInfo.forEach { (icon, text) ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            BadgeGroup {
                duplicate.chapterCount?.let {
                    Badge(
                        text = pluralStringResource(MR.plurals.num_chapters, count = it.toInt(), it.toInt()),
                    )
                }
            }
        }
    }
}

private data class BadgeInfo(val icon: ImageVector, val text: String)

@Composable
private fun getMangaBadges(duplicate: MangaWithChapterCount, source: Source): List<BadgeInfo> {
    val manga = duplicate.manga
    val badges = mutableListOf<BadgeInfo>()

    val statusIcon = when (manga.status.toInt()) {
        SManga.ONGOING -> Icons.Outlined.Schedule
        SManga.COMPLETED -> Icons.Outlined.Done
        SManga.LICENSED -> Icons.Outlined.AttachMoney
        SManga.PUBLISHING_FINISHED -> Icons.Outlined.DoneAll
        SManga.CANCELLED -> Icons.Outlined.Close
        SManga.ON_HIATUS -> Icons.Outlined.Pause
        else -> Icons.Filled.Warning
    }

    val statusText = when (manga.status.toInt()) {
        SManga.ONGOING -> stringResource(MR.strings.ongoing)
        SManga.COMPLETED -> stringResource(MR.strings.completed)
        SManga.LICENSED -> stringResource(MR.strings.licensed)
        SManga.PUBLISHING_FINISHED -> stringResource(MR.strings.publishing_finished)
        SManga.CANCELLED -> stringResource(MR.strings.cancelled)
        SManga.ON_HIATUS -> stringResource(MR.strings.on_hiatus)
        else -> stringResource(MR.strings.unknown)
    }
    badges.add(BadgeInfo(statusIcon, statusText))

    manga.author?.let {
        badges.add(BadgeInfo(Icons.Filled.PersonOutline, it))
    }
    manga.artist?.takeIf { it != manga.author }?.let {
        badges.add(BadgeInfo(Icons.Filled.Brush, it))
    }

    if (source !is StubSource) {
        badges.add(BadgeInfo(Icons.Outlined.Block, source.name))
    }

    return badges
}

private val MangaCardWidth = 150.dp

@Composable
private fun getMaximumMangaCardHeight(
    duplicates: List<MangaWithChapterCount>,
): Dp {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val typography = MaterialTheme.typography

    return with(density) {
        val coverHeight = (MangaCardWidth * MangaCover.Book.ratio).toPx()
        val badgeHeight = with(textMeasurer) {
            measure(text = " ", style = typography.labelSmall).size.height.toFloat()
        }
        val titleHeight = duplicates.fastMaxOfOrNull {
            measureMangaTitle(it.manga.title, textMeasurer, typography, density)
        } ?: 0f

        (coverHeight + titleHeight + badgeHeight * 4 + 48).toDp()
    }
}

private fun measureMangaTitle(
    title: String,
    textMeasurer: TextMeasurer,
    typography: Typography,
    density: Density,
): Float {
    val constraints = with(density) {
        Constraints(maxWidth = MangaCardWidth.toPx().toInt())
    }
    return textMeasurer.measure(
        text = title,
        style = typography.labelMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        constraints = constraints,
    ).size.height.toFloat()
}
