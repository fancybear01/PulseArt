package com.coding.pulseart.feature_main_screen.presentation.art_detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.coding.pulseart.core.presentation.util.ObserveAsEvents
import com.coding.pulseart.core.presentation.util.toString
import com.coding.pulseart.feature_main_screen.domain.ArtworkDetail
import com.coding.pulseart.feature_main_screen.presentation.art_list.ArtworkListEvent.Error
import org.koin.androidx.compose.koinViewModel

@Composable
fun ArtworkDetailScreenCore(
    artworkId: String,
    viewModel: ArtworkDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.onAction(ArtworkDetailAction.LoadArtworkDetail(artworkId))
    }
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading && state.artworkDetail == null) {
                CircularProgressIndicator()
            }
            ObserveAsEvents(events = viewModel.events) { event ->
                when(event) {
                    is Error -> {
                        Toast.makeText(
                            context,
                            event.error.toString(context),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            state.artworkDetail?.let { artworkDetail ->
                ArtworkDetailScreen(
                    modifier = Modifier.padding(paddingValues),
                    artworkDetail = artworkDetail
                )
            }
        }
    }
}

@Composable
fun ArtworkDetailScreen(
    artworkDetail: ArtworkDetail,
    modifier: Modifier = Modifier,
) {
    val contentColor = if(isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp)
            .padding(top = 8.dp)
    ) {
        AsyncImage(
            model = artworkDetail.imageUrl,
            contentDescription = artworkDetail.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary.copy(0.5f))
                .height(230.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = artworkDetail.title,
            style = MaterialTheme.typography.displayLarge,
            fontSize = 28.sp,
            maxLines = 2,
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = artworkDetail.artistDisplay,
            overflow = TextOverflow.Ellipsis,
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(
                horizontal = 16.dp
            ),
            color = contentColor
        )
    }
}