package com.coding.pulseart.feature_main_screen.data.networking

import android.content.ContentValues.TAG
import android.util.Log
import com.coding.pulseart.core.data.networking.constructUrl
import com.coding.pulseart.core.data.networking.safeCall
import com.coding.pulseart.core.domain.util.Error
import com.coding.pulseart.core.domain.util.NetworkError
import com.coding.pulseart.core.domain.util.Result
import com.coding.pulseart.core.domain.util.map
import com.coding.pulseart.feature_main_screen.data.mappers.toArtwork
import com.coding.pulseart.feature_main_screen.data.mappers.toArtworkDetail
import com.coding.pulseart.feature_main_screen.data.mappers.toPagination
import com.coding.pulseart.feature_main_screen.data.mappers.toSearchItem
import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkDetailDto
import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkDetailResponseDto
import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkResponseDto
import com.coding.pulseart.feature_main_screen.data.networking.dto.SearchResponseDto
import com.coding.pulseart.feature_main_screen.domain.Artwork
import com.coding.pulseart.feature_main_screen.domain.ArtworkDataSource
import com.coding.pulseart.feature_main_screen.domain.ArtworkDetail
import com.coding.pulseart.feature_main_screen.domain.Pagination
import com.coding.pulseart.feature_main_screen.domain.SearchItem
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemoteArtworkDataSource(
    private val httpClient: HttpClient
) : ArtworkDataSource {
    override suspend fun getArtworks(nextPage: String): Result<Pair<List<Artwork>, Pagination>, NetworkError> {
        return safeCall<ArtworkResponseDto> {
            httpClient.get(
                urlString = constructUrl("/artworks")
            ) {
                url {
                    parameters.append("page", nextPage)
                    parameters.append("limit", "10")
//                    if (nextPage != null) parameters.append("page", nextPage)
//                    else parameters.append("page", "1")
                }
            }
        }.map { response ->
            Pair(
                response.data.map { it.toArtwork() },
                response.pagination.toPagination()
            )
        }
    }
    override suspend fun getArtwork(artworkId: String): Result<ArtworkDetail, NetworkError> {
        Log.d(TAG, "getArtwork artworkId is $artworkId")
        return safeCall<ArtworkDetailResponseDto> {
            httpClient.get(
                urlString = constructUrl("/artworks/${artworkId}")
            )
        }.map { response ->
            response.data.toArtworkDetail()
        }
    }

    override suspend fun searchArtworks(query: String): Result<List<SearchItem>, NetworkError> {
        return safeCall<SearchResponseDto> {
            httpClient.get(
                urlString = constructUrl("/artworks/search?q=$query")
            ) {
                url {
                    parameters.append("q", query)
                }
            }
        }.map { response ->
            response.data.map { it.toSearchItem() }
        }
    }
}
