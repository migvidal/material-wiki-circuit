package com.migvidal.wikicircuit.feed

import android.os.Parcel
import android.os.Parcelable
import com.migvidal.wikicircuit.search.SearchScreen
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

object FeedScreen: Screen {
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    @JvmField
    val CREATOR = object : Parcelable.Creator<SearchScreen> {
        override fun createFromParcel(parcel: Parcel): SearchScreen = SearchScreen
        override fun newArray(size: Int): Array<SearchScreen?> = arrayOfNulls(size)
    }

    data class State(
        val response: CachedFeedResponse,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {
        sealed interface Event {
            data class ItemClicked(val title: String) : Event
        }
    }
}