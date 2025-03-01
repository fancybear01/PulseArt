package com.coding.pulseart.feature_main_screen.data.networking

import com.coding.pulseart.core.data.networking.constructUrl
import com.coding.pulseart.core.data.networking.safeCall
import com.coding.pulseart.core.domain.util.Error
import com.coding.pulseart.core.domain.util.NetworkError
import com.coding.pulseart.core.domain.util.Result
import com.coding.pulseart.core.domain.util.map
import com.coding.pulseart.feature_main_screen.data.mappers.toArtwork
import com.coding.pulseart.feature_main_screen.data.mappers.toArtworkDetail
import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkDetailDto
import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkDetailResponseDto
import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkResponseDto
import com.coding.pulseart.feature_main_screen.domain.Artwork
import com.coding.pulseart.feature_main_screen.domain.ArtworkDataSource
import com.coding.pulseart.feature_main_screen.domain.ArtworkDetail
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemoteArtworkDataSource(
    private val httpClient: HttpClient
) : ArtworkDataSource {
    override suspend fun getArtworks(): Result<List<Artwork>, NetworkError> {
        return safeCall<ArtworkResponseDto> {
            httpClient.get(
                urlString = constructUrl("/artworks")
            ) {
                url {
                    parameters.append("page", "1")
                    parameters.append("limit", "10")
                }
            }
        }.map { response ->
            response.data.map { it.toArtwork() }
        }
    }
    override suspend fun getArtwork(artworkId: String): Result<ArtworkDetail, NetworkError> {
        return safeCall<ArtworkDetailResponseDto> {
            httpClient.get(
                urlString = constructUrl("/artworks/${artworkId}")
            ) {

            }
        }.map { response ->
            response.data.toArtworkDetail()
        }
    }
}
