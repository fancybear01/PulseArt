package com.coding.pulseart.feature_main_screen.presentation.art_list

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.pulseart.core.presentation.util.ObserveAsEvents
import com.coding.pulseart.core.presentation.util.toString
import com.coding.pulseart.feature_main_screen.presentation.art_list.ArtworkListEvent.*
import com.coding.pulseart.feature_main_screen.presentation.art_list.components.ArtworkListItem
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.koin.androidx.compose.koinViewModel

@Composable
fun ArtworkListScreenCore(
    viewModel: ArtListViewModel = koinViewModel(),
    onArtworkClick: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEvents(events = viewModel.events) { event ->
        when (event) {
            is Error -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    ArtworkListScreen(
        state = state,
        onAction = viewModel::onAction,
        onArtworkClick = onArtworkClick
    )
}

@Composable
private fun ArtworkFilters(
    currentFilter: ArtworkFilterType,
    onFilterSelected: (ArtworkFilterType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FilterChip(
            selected = currentFilter == ArtworkFilterType.All,
            onClick = { onFilterSelected(ArtworkFilterType.All) },
            label = { Text("All") },
            modifier = Modifier
                .height(36.dp)
        )
        FilterChip(
            selected = currentFilter == ArtworkFilterType.Painting,
            onClick = { onFilterSelected(ArtworkFilterType.Painting) },
            label = { Text("Painting") },
            modifier = Modifier
                .height(36.dp)
        )
        FilterChip(
            selected = currentFilter == ArtworkFilterType.Sculpture,
            onClick = { onFilterSelected(ArtworkFilterType.Sculpture) },
            label = { Text("Sculpture") },
            modifier = Modifier
                .height(36.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtworkListScreen(
    state: ArtworkListState,
    onAction: (ArtworkListAction) -> Unit,
    onArtworkClick: (String) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            text = "PulseArt",
                            fontSize = 33.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    },
                    windowInsets = WindowInsets(
                        top = 0.dp, bottom = 8.dp
                    )
                )
                ArtworkFilters(
                    currentFilter = state.selectedFilter,
                    onFilterSelected = { filter ->
                        onAction(ArtworkListAction.FilterChanged(filter))
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading && state.artworks.isEmpty()) {
                    CircularProgressIndicator()
                }
            }

            val listState = rememberLazyListState()
            val shouldPaginate = remember {
                derivedStateOf {
                    val totalItems = listState.layoutInfo.totalItemsCount
                    val lastVisibleIndex = listState.layoutInfo
                        .visibleItemsInfo.lastOrNull()?.index ?: 0
                    ((lastVisibleIndex == totalItems - 1) || (totalItems == 0)) && !state.isLoading
                }
            }
            LaunchedEffect(key1 = listState) {
                snapshotFlow { shouldPaginate.value }
                    .distinctUntilChanged()
                    .filter { it }
                    .collect {onAction(ArtworkListAction.Paginate)}
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 8.dp),
                state = listState
            ) {
                items(state.artworks) { artwork ->
                    ArtworkListItem(
                        artwork = artwork,
                        onArtworkDetailClick = onArtworkClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}