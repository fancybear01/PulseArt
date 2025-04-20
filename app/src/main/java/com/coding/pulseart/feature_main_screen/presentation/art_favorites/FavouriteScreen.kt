package com.coding.pulseart.feature_main_screen.presentation.art_favorites

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.pulseart.R
import com.coding.pulseart.core.presentation.util.ObserveAsEvents
import com.coding.pulseart.core.presentation.util.toString
import com.coding.pulseart.feature_main_screen.presentation.art_list.ArtworkListEvent.Error
import com.coding.pulseart.feature_main_screen.presentation.art_list.components.ArtworkListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouriteScreenCore(
    viewModel: FavouriteViewModel = koinViewModel(),
    onArtworkClick: (String) -> Unit
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
    FavouriteScreen(
        state = state,
        onAction = viewModel::onAction,
        onArtworkClick = onArtworkClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(
    state: FavouriteState,
    onAction: (FavouriteAction) -> Unit,
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
                        text = stringResource(R.string.favourite),
                        fontSize = 33.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                windowInsets = WindowInsets(
                    top = 0.dp, bottom = 8.dp
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
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading && state.artworks.isEmpty()) {
                    CircularProgressIndicator()
                }
                else if (state.artworks.isEmpty()) {
                    Text(
                        text = stringResource(R.string.add_your_favorites_from_the_home_screen),
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            val listState = rememberLazyListState()

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