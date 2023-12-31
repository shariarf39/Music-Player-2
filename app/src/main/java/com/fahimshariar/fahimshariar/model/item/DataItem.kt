package com.fahimshariar.fahimshariar.model.item

import com.fahimshariar.fahimshariar.model.Song
import com.ldt.fahimshariar.model.mp.MPPlaylist

sealed class DataItem: AdapterDataItem {
    class SongItem(val flags: Long, val song: _root_ide_package_.com.fahimshariar.fahimshariar.model.Song, val positionInData: Int, val playlistId: Int, val name: CharSequence = song.title, val subName: CharSequence = song.artistName): DataItem()
    class PlaylistItem(val flags: Long, val playlist: MPPlaylist, val name: CharSequence = playlist.name): DataItem()

    sealed class Control: DataItem()
    object SortingTile: Control()
    sealed class FeatureSectionTile(): Control()
    sealed class Empty: Control() {
        object Error: Empty()
        object NoResult: Empty()
    }

    sealed class Dim: Control() {
        object TopGradientDim: Dim()
        object BottomGradientDim: Dim()
    }

    companion object {
        const val FLAG_DIM = (0x00000001).toLong()

        fun hasFlag(flagsHolder: Long, flagsNeedToCheck: Long): Boolean {
            return flagsHolder and flagsNeedToCheck != 0L
        }
    }
}