package com.coding.pulseart.feature_main_screen.presentation.art_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.coding.pulseart.feature_main_screen.domain.Artwork
import com.coding.pulseart.feature_main_screen.presentation.art_list.ArtworkListState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ArtworkDetailScreenCore(
    artworkId: String,
    viewModel: ArtworkDetailViewModel = koinViewModel()
) {

}

@Composable
fun ArtworkDetailScreen(
    artwork: Artwork,
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
            model = artwork.imageUrl,
            contentDescription = artwork.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary.copy(0.5f))
                .height(230.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = artwork.title,
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
            text = artwork.artistDisplay,
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