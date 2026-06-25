package com.migvidal.wikicircuit.core.di

import com.migvidal.wikicircuit.detail.DetailUi
import com.migvidal.wikicircuit.detail.DetailPresenterFactory
import com.migvidal.wikicircuit.detail.DetailScreen
import com.migvidal.wikicircuit.feed.FeedPresenterFactory
import com.migvidal.wikicircuit.feed.FeedScreen
import com.migvidal.wikicircuit.feed.FeedUi
import com.migvidal.wikicircuit.search.SearchUi
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
        feedPresenterFactory: FeedPresenterFactory,
        searchPresenterFactory: SearchPresenterFactory,
        detailPresenterFactory: DetailPresenterFactory,
    ) =
        Circuit.Builder()
            .addPresenterFactory(feedPresenterFactory)
            .addUi<FeedScreen, FeedScreen.State> { state, modifier -> FeedUi(state, modifier) }
            .addPresenterFactory(searchPresenterFactory)
            .addUi<SearchScreen, SearchScreen.State> { state, modifier -> SearchUi(state, modifier) }
            .addPresenterFactory(detailPresenterFactory)
            .addUi<DetailScreen, DetailScreen.State> { state, modifier -> DetailUi(state, modifier) }
            .build()
}