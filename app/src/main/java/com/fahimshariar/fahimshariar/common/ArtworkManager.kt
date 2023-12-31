package com.fahimshariar.fahimshariar.common

import com.fahimshariar.fahimshariar.model.Media
import com.fahimshariar.fahimshariar.model.Playlist
import com.fahimshariar.fahimshariar.model.Song
import com.ldt.fahimshariar.model.mp.ArtworkInfo
import java.util.*

/**
 * Manage every image cover showing on screen, such as playlist cover, song cover etc
 */
object ArtworkManager {
    val mapMediaIdToArtworkInfo = Collections.synchronizedMap(hashMapOf<String, ArtworkInfo>())
    @JvmStatic
    fun getMediaId(media: _root_ide_package_.com.fahimshariar.fahimshariar.model.Media): String {
        return when(media) {
            is _root_ide_package_.com.fahimshariar.fahimshariar.model.Song -> "song_${media.id}"
            is _root_ide_package_.com.fahimshariar.fahimshariar.model.Playlist -> "playlist_${media.id}"
            else -> ""
        }
    }

    @JvmStatic
    fun getArtworkInfo(media: _root_ide_package_.com.fahimshariar.fahimshariar.model.Media): ArtworkInfo? {
        val id = getMediaId(media)
        return mapMediaIdToArtworkInfo[id] ?: createArtworkInfo(media, id)?.also { mapMediaIdToArtworkInfo[id] = it}
    }

    private fun createArtworkInfo(media: _root_ide_package_.com.fahimshariar.fahimshariar.model.Media, mediaId: String = getMediaId(media)): ArtworkInfo? {
        // TODO:
        // 1. Generate the artwork if needed
        // 2. Save into disk and get path
        // 3. Save artwork info to database
        // 4. return the Artwork info
        return null
    }
}