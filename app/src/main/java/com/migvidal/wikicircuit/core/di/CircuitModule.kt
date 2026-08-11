package com.migvidal.wikicircuit.core.di

import com.migvidal.wikicircuit.feature.page.article.ArticleUi
import com.migvidal.wikicircuit.feature.page.article.ArticleScreen
import com.migvidal.wikicircuit.feature.feed.FeedPresenterFactory
import com.migvidal.wikicircuit.feature.feed.FeedScreen
import com.migvidal.wikicircuit.feature.feed.FeedUi
import com.migvidal.wikicircuit.feature.page.article.ArticlePresenterFactory
import com.migvidal.wikicircuit.feature.page.image.ImagePresenterFactory
import com.migvidal.wikicircuit.feature.page.image.ImageScreen
import com.migvidal.wikicircuit.feature.page.image.ImageUi
import com.migvidal.wikicircuit.feature.search.SearchUi
import com.migvidal.wikicircuit.feature.search.SearchPresenterFactory
import com.migvidal.wikicircuit.feature.search.SearchScreen
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
        articlePresenterFactory: ArticlePresenterFactory,
        imagePresenterFactory: ImagePresenterFactory,
    ) =
        Circuit.Builder()
            .addPresenterFactory(feedPresenterFactory)
            .addUi<FeedScreen, FeedScreen.State> { state, modifier -> FeedUi(state, modifier) }
            .addPresenterFactory(searchPresenterFactory)
            .addUi<SearchScreen, SearchScreen.State> { state, modifier ->
                SearchUi(state = state, modifier = modifier)
            }
            .addPresenterFactory(articlePresenterFactory)
            .addUi<ArticleScreen, ArticleScreen.State> { state, modifier ->
                ArticleUi(state = state, modifier = modifier)
            }
            .addPresenterFactory(imagePresenterFactory)
            .addUi<ImageScreen, ImageScreen.State> { state, modifier ->
                ImageUi(state = state, modifier = modifier)
            }
            .build()
}