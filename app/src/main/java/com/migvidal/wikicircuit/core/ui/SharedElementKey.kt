package com.migvidal.wikicircuit.core.ui

data class SharedElementKey(val type: Type, val id: String? = null) {
    enum class Type {
        Title, Description, Card, Image
    }
}