package mihon.feature.migration.config

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Deselect
import androidx.compose.material.icons.outlined.DragHandle
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.domain.source.service.SourcePreferences
import eu.kanade.presentation.browse.components.SourceIcon
import eu.kanade.presentation.components.AppBar
import eu.kanade.presentation.components.AppBarActions
import eu.kanade.presentation.util.Screen
import eu.kanade.tachiyomi.source.online.HttpSource
import eu.kanade.tachiyomi.ui.browse.migration.search.MigrateSearchScreen
import eu.kanade.tachiyomi.util.system.LocaleHelper
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.update
import mihon.feature.migration.list.MigrationListScreen
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState
import sh.calvin.reorderable.rememberReorderableLazyListState
import tachiyomi.core.common.util.lang.launchIO
import tachiyomi.domain.source.model.Source
import tachiyomi.domain.source.service.SourceManager
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.FastScrollLazyColumn
import tachiyomi.presentation.core.components.Pill
import tachiyomi.presentation.core.components.material.Scaffold
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.i18n.stringResource
import tachiyomi.presentation.core.screens.LoadingScreen
import tachiyomi.presentation.core.util.shouldExpandFAB
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class MigrationConfigScreen(private val mangaIds: Collection<Long>) : Screen() {

    constructor(mangaId: Long) : this(listOf(mangaId))

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = rememberScreenModel { MigrationConfigScreenModel() }
        val state by model.state.collectAsState()
        val lazyListState = rememberLazyListState()

        when {
            state.isLoading -> LoadingScreen()
            else -> {
                val expandedFab by remember {
                    derivedStateOf { lazyListState.shouldExpandFAB() }
                }

                Scaffold(
                    topBar = {
                        AppBar(
                            title = stringResource(MR.strings.migration_source_selection),
                            navigateUp = navigator::pop,
                            actions = {
                                AppBarActions(
                                    persistentListOf(
                                        AppBar.Action(
                                            title = stringResource(MR.strings.action_select_all),
                                            icon = Icons.Outlined.SelectAll,
                                            onClick = model::toggleAllSources,
                                        ),
                                        AppBar.Action(
                                            title = stringResource(MR.strings.action_deselect_all),
                                            icon = Icons.Outlined.Deselect,
                                            onClick = model::toggleAllSources,
                                        ),
                                    ),
                                )
                            },
                        )
                    },
                    floatingActionButton = {
                        // M3 Expressive: ExtendedFloatingActionButton dengan extraLarge shape
                        ExtendedFloatingActionButton(
                            text = { Text(stringResource(MR.strings.action_migrate)) },
                            icon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                                    contentDescription = null,
                                )
                            },
                            onClick = {
                                navigator.push(
                                    MigrationListScreen(
                                        mangaIds = mangaIds,
                                        targetSourceIds = state.sources
                                            .filter { it.second }
                                            .map { it.first.id },
                                    ),
                                )
                            },
                            expanded = expandedFab,
                            shape = MaterialTheme.shapes.extraLarge,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 4.dp,
                            ),
                        )
                    },
                ) { paddingValues ->
                    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
                        model.reorderSource(from.index, to.index)
                    }

                    FastScrollLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                        contentPadding = paddingValues,
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                    ) {
                        itemsIndexed(
                            items = state.sources,
                            key = { _, (source, _) -> source.id },
                        ) { index, (source, enabled) ->
                            ReorderableItem(reorderableState, key = source.id) {
                                MigrationSourceItem(
                                    source = source,
                                    enabled = enabled,
                                    reorderableState = reorderableState,
                                    onToggle = { model.toggleSource(source) },
                                )
                            }

                            if (index < state.sources.lastIndex) {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium))
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ReorderableCollectionItemScope.MigrationSourceItem(
        source: Source,
        enabled: Boolean,
        reorderableState: ReorderableLazyListState,
        onToggle: () -> Unit,
    ) {
        // M3 Expressive: Surface dengan extraLarge shape
        Surface(
            shape = MaterialTheme.shapes.large,
            color = if (enabled) {
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.surfaceContainerLow
            },
            modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium),
        ) {
            ListItem(
                modifier = Modifier.clickable(onClick = onToggle),
                headlineContent = {
                    Text(
                        text = source.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                supportingContent = source.lang.takeIf { it.isNotEmpty() }?.let {
                    {
                        Text(
                            text = LocaleHelper.getSourceDisplayName(it, null),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                leadingContent = {
                    SourceIcon(source = source)
                },
                trailingContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                    ) {
                        if (enabled) {
                            Pill(
                                text = stringResource(MR.strings.action_selected),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                        Icon(
                            imageVector = Icons.Outlined.DragHandle,
                            contentDescription = null,
                            modifier = Modifier.draggableHandle(),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                ),
            )
        }
    }
}
