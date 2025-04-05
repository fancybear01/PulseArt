package com.coding.pulseart.feature_main_screen.presentation.art_search


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.pulseart.core.presentation.util.toString
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import com.coding.pulseart.feature_main_screen.domain.SearchItem

@Composable
fun SearchScreenCore(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    onArtworkSelected: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    ObserveSearchEvents(viewModel.events, context)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SearchTopBar(
                query = state.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                onSearch = { viewModel.searchArtworks() }
            )
        },
        floatingActionButton = {
            if (state.searchQuery.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { viewModel.clearSearch() },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Очистить поиск")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> {

                }

                state.results.isEmpty() -> EmptySearchState()
                else -> SearchResultsList(
                    artworkSearchItems = state.results,
                    onArtworkSelected = onArtworkSelected,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    var isSearchActive by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text("Поиск произведений") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = onQueryChange,
                        onSearch = {
                            onSearch()
                            expanded = false
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text("Search") }
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {

            }
        }
    )
}

@Composable
private fun SearchResultsList(
    artworkSearchItems: List<SearchItem>,
    onArtworkSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        items(artworkSearchItems) { artwork ->
            ArtworkSearchItem(
                artworkSearchItem = artwork,
                onClick = { onArtworkSelected(artwork.id) }
            )
        }
    }
}

@Composable
private fun ArtworkSearchItem(
    artworkSearchItem: SearchItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            AsyncImage(
//                model = artwork.imageUrl,
//                contentDescription = artwork.title,
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(MaterialTheme.shapes.medium),
//                contentScale = ContentScale.Crop
//            )
//
//            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = artworkSearchItem.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
//                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
//                    Text(
//                        text = artworkSearchItem.artistDisplay,
//                        style = MaterialTheme.typography.bodyMedium,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                }
            }
        }
    }
}

@Composable
private fun ObserveSearchEvents(events: Flow<SearchEvent>, context: Context) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is SearchEvent.Error -> {
                    Toast.makeText(
                        context,
                        event.error.toString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
//                SearchEvent.ClearSearch -> {
//                    // Обработка очистки поиска
//                }
            }
        }
    }
}


@Composable
fun EmptySearchState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = "Пустой поиск",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Введите запрос для поиска произведений",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}