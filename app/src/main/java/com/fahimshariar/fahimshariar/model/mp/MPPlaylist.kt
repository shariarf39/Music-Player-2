package com.fahimshariar.fahimshariar.model.mp

import com.fahimshariar.fahimshariar.model.Playlist

class MPPlaylist(id: Int, name: String): _root_ide_package_.com.fahimshariar.fahimshariar.model.Playlist(id, name) {
    val songs = mutableListOf<Int>()
}