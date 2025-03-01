package com.coding.pulseart.feature_main_screen.presentation.art_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.coding.pulseart.feature_main_screen.data.mappers.toArtworkUi
import com.coding.pulseart.feature_main_screen.domain.Artwork
import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkUi
import com.coding.pulseart.ui.theme.AppTypography
import com.coding.pulseart.ui.theme.PulseArtTheme

@Composable
fun ArtworkListItem(
    artworkUi: ArtworkUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
    Card(
        onClick = onClick,
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = modifier
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            AsyncImage(
                model = artworkUi.imageUrl,
                contentDescription = artworkUi.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(0.3f))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = artworkUi.title,
                fontSize = 20.sp,
                style = MaterialTheme.typography.displayLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = contentColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = artworkUi.artistDisplay,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 17.sp,
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                ),
                color = contentColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(shape = RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.5f))
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun ArtworkListItemPreview() {
    PulseArtTheme {
        ArtworkListItem(
            artworkUi = previewArtwork,
            onClick = {},
            modifier = Modifier.background(
                MaterialTheme.colorScheme.background
            )
        )
    }
}

internal val previewArtwork = Artwork(
    id = "1",
    title = "A Sunday on La Grande Jatte — 1884",
    artistDisplay = "Georges Seurat (French, 1859–1891)",
    imageUrl = "https://www.artic.edu/iiif/2/2d484387-2509-5e8e-2c43-22f9981972eb/full/843,/0/default.jpg"
).toArtworkUi()