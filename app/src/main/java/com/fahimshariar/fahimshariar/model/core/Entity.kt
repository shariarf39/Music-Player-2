package com.fahimshariar.fahimshariar.model.core

interface Entity: Searchable {
    /**
     * Unique identifier
     */
    val uid: String

    /**
     * Name that displays on user interface
     */
    val displayName: String
}