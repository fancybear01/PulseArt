package com.coding.pulseart.di

import com.coding.pulseart.core.data.networking.HttpClientFactory
import com.coding.pulseart.feature_main_screen.data.networking.RemoteArtworkDataSource
import com.coding.pulseart.feature_main_screen.domain.ArtworkDataSource
import com.coding.pulseart.feature_main_screen.presentation.art_detail.ArtworkDetailViewModel
import com.coding.pulseart.feature_main_screen.presentation.art_list.ArtListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::RemoteArtworkDataSource).bind<ArtworkDataSource>()

    viewModel { ArtListViewModel(get()) }
    viewModel { ArtworkDetailViewModel(get()) }
}