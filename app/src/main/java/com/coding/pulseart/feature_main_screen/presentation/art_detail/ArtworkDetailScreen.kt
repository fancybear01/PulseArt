package com.coding.pulseart.feature_main_screen.presentation.art_detail

import android.annotation.SuppressLint
import android.widget.Button
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ContentAlpha
import coil.compose.AsyncImage
import com.coding.pulseart.R
import com.coding.pulseart.core.presentation.util.ObserveAsEvents
import com.coding.pulseart.core.presentation.util.toString
import com.coding.pulseart.feature_main_screen.domain.ArtworkDetail
import com.coding.pulseart.feature_main_screen.presentation.art_list.ArtworkListEvent.Error
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ArtworkDetailScreenCore(
    artworkId: String,
    viewModel: ArtworkDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(artworkId) {
        viewModel.onAction(ArtworkDetailAction.LoadArtworkDetail(artworkId))
        viewModel.checkFavoriteStatus(artworkId)
    }

    Scaffold(
        floatingActionButton = {
            val animatedProgress by animateFloatAsState(
                targetValue = if (state.isFavorite) 1f else 0f,
                animationSpec = tween(durationMillis = 500)
            )

            FloatingActionButton(
                onClick = {
                    scope.launch {
                        viewModel.toggleFavorite(state.artworkDetail)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Crossfade(
                    targetState = state.isFavorite,
                    label = "FavoriteIconAnimation"
                ) { isFavorite ->
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite
                        else Icons.Outlined.FavoriteBorder,
                        contentDescription = "FavoriteIconAnimation",
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = 1f + 0.2f * animatedProgress
                                scaleY = 1f + 0.2f * animatedProgress
                            }
                    )
                }
            }
        }
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isError -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }

                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                state.artworkDetail != null -> {
                    state.artworkDetail?.let { artworkDetail ->
                        ArtworkDetailScreen(
                            modifier = Modifier.padding(paddingValues),
                            artworkDetail = artworkDetail
                        )
                    }
                }
            }

            ObserveAsEvents(events = viewModel.events) { event ->
                when (event) {
                    is ArtworkDetailEvent.Error -> {
                        Toast.makeText(
                            context,
                            event.error.toString(context),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is ArtworkDetailEvent.FavouriteStatusChanged -> {
                        scope.launch {
                            val snackbarMessage = if (state.isFavorite) {
                                // context.getString(R.string.added_to_favorites)
                            } else {
                                //context.getString(R.string.removed_from_favorites)
                            }

                            //SnackbarHostState().showSnackbar(snackbarMessage)
                        }
                    }

                    is ArtworkDetailEvent.UnknownError -> {
                        Toast.makeText(
                            context,
                            event.error.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}

@Composable
fun ArtworkDetailScreen(
    artworkDetail: ArtworkDetail,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    val imageHeight = remember(configuration) {
        (configuration.screenHeightDp * 0.4).dp
    }
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = artworkDetail.imageUrl,
                contentDescription = artworkDetail.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
//                placeholder = painterResource(R.drawable.placeholder_image),
//                error = painterResource(R.drawable.error_image),
//                loading = {
//                    CircularProgressIndicator(
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = artworkDetail.title,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 2,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = artworkDetail.artistDisplay,
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onBackground
            )


            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
