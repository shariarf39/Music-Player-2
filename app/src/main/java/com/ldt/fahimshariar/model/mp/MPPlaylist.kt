package com.ldt.fahimshariar.model.mp

import com.ldt.fahimshariar.model.Playlist

class MPPlaylist(id: Int, name: String): Playlist(id, name) {
    val songs = mutableListOf<Int>()
}