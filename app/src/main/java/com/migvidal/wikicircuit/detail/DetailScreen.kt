package com.migvidal.wikicircuit.detail

import android.os.Parcel
import android.os.Parcelable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data class DetailScreen(val title: String) : Screen {
    override fun describeContents() = 0

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(title)
    }

    companion object CREATOR : Parcelable.Creator<DetailScreen> {
        override fun createFromParcel(p0: Parcel?) = DetailScreen(p0?.readString() ?: "")
        override fun newArray(p0: Int): Array<out DetailScreen?> = arrayOfNulls(p0)
    }

    data class State(
        val response: CachedArticleResponse,
        val isFavorite: Boolean,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {
        sealed interface Event {
            data object BackClicked : Event
        }
    }
}

