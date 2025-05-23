package com.coding.pulseart.feature_main_screen.presentation.art_detail

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.coding.pulseart.core.presentation.util.ObserveAsEvents
import com.coding.pulseart.core.presentation.util.toString
import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkDetail
import com.coding.pulseart.ui.theme.PulseArtTheme
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
                            },
                        tint = Color.Red
                    )
                }
            }
        }
    ) { _ ->
        BoxWithConstraints(
            modifier = Modifier
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
                modifier = Modifier.fillMaxSize()
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

            Row {
                Text(
                    text = "Date: ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = artworkDetail.dateStart,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "-",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = artworkDetail.dateEnd,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = artworkDetail.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                overflow = TextOverflow.Ellipsis,
                //maxLines = 2,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun ArtworkDetailPreview() {
    PulseArtTheme {
        ArtworkDetailScreen(
            artworkDetail = previewArtwork
        )
    }
}

internal val previewArtwork = ArtworkDetail(
    id = 1,
    title = "A Sunday on La Grande Jatte — 1884",
    artistDisplay = "Georges Seurat (French, 1859–1891)",
    imageUrl = "https://www.artic.edu/iiif/2/2d484387-2509-5e8e-2c43-22f9981972eb/full/843,/0/default.jpg",
    dateStart = "1920",
    dateEnd = "1922",
    dateDisplay = "1922",
    description = "<p>The practice of decorating tiles with bright colors outlined in black was known as cuerda seca, which translates as “dry cord” from Spanish. Cuerda seca developed in the Islamic lands of Spain, Iran, and Central Asia at the end of the 14th century and remained popular in these regions for several centuries. The black line between colors allowed for carefully distinguished forms that otherwise might have been muddles during the firing process.These tiles are a section of a larger scene depicting the life of the epic Persian hero Bahram Gur. Here, gazelles festively play along the bank of a river as young man plays the flute and another presumably bridles a horse, of which only the nose is visible. Similar figures appear in different sets of tiles, evidence of the use of pattern books or stock images for similar scenes. The tales of Bahram Gur were popular subjects for both smaller decorative objects and long epic books, such as the lavishly decorated <em>Khamsa of Nizami</em>, a 16th-century manuscript created for the Mughal Emperor Akbar.</p>\\n",
    shortDescription = "Alma Thomas was enthralled by astronauts and outer space. This painting, made when she was 81, showcases that fascination through her signature style of short, rhythmic strokes of paint. “Color is life, and light is the mother of color,” she once proclaimed. In 1972, she became the first African American woman to have a solo exhibition at the Whitney Museum of American Art in New York."
)