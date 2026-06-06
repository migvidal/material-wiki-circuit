package com.migvidal.wikicircuit.core.di

import com.migvidal.wikicircuit.detail.Detail
import com.migvidal.wikicircuit.detail.DetailPresenterFactory
import com.migvidal.wikicircuit.detail.DetailScreen
import com.migvidal.wikicircuit.search.Search
import com.migvidal.wikicircuit.search.SearchPresenterFactory
import com.migvidal.wikicircuit.search.SearchScreen
import com.slack.circuit.foundation.Circuit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object CircuitModule {

    @Provides
    fun provideCircuit(
        feedPresenterFactory: SearchPresenterFactory,
        detailPresenterFactory: DetailPresenterFactory,
    ) =
        Circuit.Builder()
            .addPresenterFactory(feedPresenterFactory)
            .addUi<SearchScreen, SearchScreen.State> { state, modifier -> Search(state, modifier) }
            .addPresenterFactory(detailPresenterFactory)
            .addUi<DetailScreen, DetailScreen.State> { state, modifier -> Detail(state, modifier) }
            .build()
}