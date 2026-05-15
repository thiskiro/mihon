package eu.kanade.presentation.more.settings.screen.browse.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.util.system.copyToClipboard
import kotlinx.collections.immutable.ImmutableSet
import mihon.domain.extensionrepo.model.ExtensionRepo
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.i18n.stringResource

@Composable
fun ExtensionReposContent(
    repos: ImmutableSet<ExtensionRepo>,
    lazyListState: LazyListState,
    paddingValues: PaddingValues,
    onOpenWebsite: (ExtensionRepo) -> Unit,
    onClickDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = lazyListState,
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
        modifier = modifier,
    ) {
        repos.forEach {
            item {
                ExtensionRepoListItem(
                    modifier = Modifier.animateItem(),
                    repo = it,
                    onOpenWebsite = { onOpenWebsite(it) },
                    onDelete = { onClickDelete(it.baseUrl) },
                )
            }
        }
    }
}

@Composable
private fun ExtensionRepoListItem(
    repo: ExtensionRepo,
    onOpenWebsite: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    // M3 Expressive: Surface dengan extraLarge shape
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 1.dp,
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.padding.medium,
                    top = MaterialTheme.padding.medium,
                    end = MaterialTheme.padding.medium,
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Label,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = MaterialTheme.padding.small),
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = repo.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = repo.baseUrl,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                // M3 Expressive: open website button
                IconButton(
                    onClick = onOpenWebsite,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                        contentDescription = stringResource(MR.strings.action_open_in_browser),
                    )
                }
                // M3 Expressive: copy button
                IconButton(
                    onClick = { context.copyToClipboard(repo.baseUrl, repo.baseUrl) },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = stringResource(MR.strings.action_copy_to_clipboard),
                    )
                }
                // M3 Expressive: delete button dengan error color
                IconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(MR.strings.action_delete),
                    )
                }
            }
        }
    }
}
