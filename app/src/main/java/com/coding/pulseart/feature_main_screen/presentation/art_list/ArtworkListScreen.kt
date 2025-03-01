package com.coding.pulseart.feature_main_screen.presentation.art_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.coding.pulseart.feature_main_screen.presentation.art_list.components.ArtworkListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun ArtworkListScreenCore(
    viewModel: ArtListViewModel = koinViewModel(),
    onArtworkClick: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ArtworkListScreen(
        state = state,
        onAction = viewModel::onAction,
        onArtworkClick = onArtworkClick
    )
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
                    top = 50.dp, bottom = 8.dp
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                val listState = rememberLazyListState()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    state = listState
                ) {
                    items(state.artworks) { artworkUi ->
                        ArtworkListItem(
                            artworkUi = artworkUi,
                            onClick = {
                                onAction(ArtworkListAction.OnArtworkClick(artworkUi))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}