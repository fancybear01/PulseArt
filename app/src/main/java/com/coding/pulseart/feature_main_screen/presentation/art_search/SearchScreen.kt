package com.coding.pulseart.feature_main_screen.presentation.art_search


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
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
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coding.pulseart.R
import com.coding.pulseart.feature_main_screen.domain.SearchItem
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenCore(
    onArtworkSelected: (String) -> Unit
) {
    val viewModel: SearchViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        snapshotFlow { state.searchQuery }
            .distinctUntilChanged()
            .debounce(100) // Дополнительный debounce в UI
            .collect { query ->
                if (query.length >= 3) {
                    viewModel.searchArtworks()
                }
            }
    }

    Scaffold(
        topBar = {
            SearchTopBar(
                query = state.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                onSearch = { viewModel.searchArtworks() },
                state = state,
                onClear = {
                    viewModel.onSearchQueryChange("")
                    focusManager.clearFocus()
                }
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
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> LoadingState()
                state.error != null -> ErrorState(state.error!!)
                state.results.isEmpty() -> EmptySearchState()
                else -> SearchResultsList(
                    artworkSearchItems = state.results,
                    onArtworkSelected = onArtworkSelected
                )
            }
        }
    }
}

@Composable
private fun SearchResultsList(
    artworkSearchItems: List<SearchItem>,
    onArtworkSelected: (String) -> Unit,
) {

//    Column(Modifier.verticalScroll(rememberScrollState())) {
//        artworkSearchItems.forEach { artwork ->
//            ListItem(
//                headlineContent = {
//                    ArtworkSearchItem(
//                        artworkSearchItem = artwork,
//                        onClick = { onArtworkSelected(artwork.id) }
//                    )
//                },
//                modifier = Modifier
//                    .clickable {
//                        //textFieldState.edit { replace(0, length, result) }
//                        //expanded = false
//                    }
//                    .fillMaxWidth()
//            )
//        }
//    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        items(
            artworkSearchItems,
            key = { it.id }) { artwork ->
            ArtworkSearchItem(
                artworkSearchItem = artwork,
                onClick = { onArtworkSelected(artwork.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    state: SearchState,
    onClear: () -> Unit
) {
    var active by remember { mutableStateOf(false) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { isTraversalGroup = true }
    ) {

        TopAppBar(
            title = { Text(stringResource(R.string.search_artworks)) },
            colors = TopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                scrolledContainerColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.primary,
                actionIconContentColor = MaterialTheme.colorScheme.primary
            ),
            actions = {
                SearchBar(
                    colors = SearchBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.onBackground
                    ),
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
                    when (query.length) {
                        in 0..2 -> state.results = emptyList() //onSearch()
                        else -> onSearch()
                    }
                }
            }
        )
    }
}

@Composable
private fun ArtworkSearchItem(
    artworkSearchItem: SearchItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Add AsyncImage here when ready
            Spacer(modifier = Modifier.size(48.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = artworkSearchItem.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

//                Text(
//                    text = artworkSearchItem.artistDisplay,
//                    style = MaterialTheme.typography.bodyMedium.copy(
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    ),
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun ErrorState(error: Throwable) {
    val context = LocalContext.current
    LaunchedEffect(error) {
        Toast.makeText(
            context,
            error.toString(),
            Toast.LENGTH_LONG
        ).show()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "error",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Error occurred",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
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