package com.migvidal.wikicircuit.search

import android.os.Parcel
import android.os.Parcelable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data object SearchScreen : Screen {
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    @JvmField
    val CREATOR = object : Parcelable.Creator<SearchScreen> {
        override fun createFromParcel(parcel: Parcel): SearchScreen = SearchScreen
        override fun newArray(size: Int): Array<SearchScreen?> = arrayOfNulls(size)
    }

    data class State(
        val response: CachedSearchResponse,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {
        sealed interface Event {
            data class ArticleClicked(val title: String) : Event
            data class Search(val term: String) : Event
        }
    }
}