package com.fahimshariar.fahimshariar.common

import com.fahimshariar.fahimshariar.App
import com.ldt.fahimshariar.helper.extension.post
import com.fahimshariar.fahimshariar.loader.medialoader.ArtistLoader
import com.fahimshariar.fahimshariar.loader.medialoader.PlaylistLoader
import com.fahimshariar.fahimshariar.loader.medialoader.PlaylistSongLoader
import com.fahimshariar.fahimshariar.loader.medialoader.SongLoader
import com.fahimshariar.fahimshariar.model.Artist
import com.fahimshariar.fahimshariar.model.Song
import com.ldt.fahimshariar.model.mp.MPPlaylist
import com.ldt.fahimshariar.notification.EventKey
import com.ldt.fahimshariar.notification.MediaKey
import com.ldt.fahimshariar.utils.Utils
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

object MediaManager {
    private val allSongs = Collections.synchronizedList(mutableListOf<_root_ide_package_.com.fahimshariar.fahimshariar.model.Song>())

    /**
     * Song Id to Song
     */
    private val mapIdToSong = Collections.synchronizedMap(hashMapOf<Int, _root_ide_package_.com.fahimshariar.fahimshariar.model.Song>())

    /**
     * Playlist Id to Playlist
     */
    private val mapIdToPlaylist = Collections.synchronizedMap(hashMapOf<Int, MPPlaylist>())

    /**
     * Song Id to Top Hit Score
     */
    private val mapSongIdToTopHit = Collections.synchronizedMap(hashMapOf<Int, Float>())

    /**
     * Artist Id to Artist
     */
    private val mapArtistIdToArtist = Collections.synchronizedMap(hashMapOf<Int, _root_ide_package_.com.fahimshariar.fahimshariar.model.Artist>())
    private val allArtists = Collections.synchronizedList(mutableListOf<_root_ide_package_.com.fahimshariar.fahimshariar.model.Artist>())

    private val isLoadedMediaInternal = AtomicBoolean(false)
    private val isLoadingMediaInternal = AtomicBoolean(false)
    private val isLoadedSongsInternal = AtomicBoolean(false)
    private val isLoadedPlaylistsInternal = AtomicBoolean(false)
    private val isLoadedArtistsInternal = AtomicBoolean(false)

    fun getSong(id: Int): _root_ide_package_.com.fahimshariar.fahimshariar.model.Song? {
        return mapIdToSong[id]
    }

    fun getPlaylist(id: Int): MPPlaylist? {
        return mapIdToPlaylist[id]
    }

    fun getArtist(id: Int): _root_ide_package_.com.fahimshariar.fahimshariar.model.Artist? {
        return mapArtistIdToArtist[id]
    }

    val isLoadedMedia: Boolean get() = isLoadedMediaInternal.get()
    val isLoadingMedia: Boolean get() = isLoadingMediaInternal.get()
    val isLoadedSongs: Boolean get() = isLoadedSongsInternal.get()
    val isLoadedPlaylists: Boolean get() = isLoadedMediaInternal.get()
    val isLoadedArtists: Boolean get() = isLoadedMediaInternal.get()

    @JvmStatic
    fun loadMediaIfNeeded() {
        if(isLoadedMediaInternal.get() || isLoadingMediaInternal.get()) return
        isLoadingMediaInternal.set(true)

        // Load Media
        loadAllSongs()
        loadAllPlaylists()
        loadAllArtists()

        isLoadingMediaInternal.set(false)
        isLoadedMediaInternal.set(true)
        EventKey.OnLoadedMedia.post()
    }

    private fun loadAllSongs() {
        mapIdToSong.clear()
        allSongs.clear()
        allSongs.addAll(
            _root_ide_package_.com.fahimshariar.fahimshariar.loader.medialoader.SongLoader.getAllSongs(
                _root_ide_package_.com.fahimshariar.fahimshariar.App.getInstance()))
        allSongs.forEach {
            mapIdToSong[it.id] = it
        }

        isLoadedSongsInternal.set(true)
        EventKey.OnLoadedSongs.post()
    }

    private fun loadAllPlaylists() {
        mapIdToPlaylist.clear()

        // Add All Songs Playlist
        val allSongPlaylist = MPPlaylist(MediaKey.PLAYLIST_ID_ALL_SONGS, "All Songs")
        allSongs.forEach {
            allSongPlaylist.songs.add(it.id)
        }
        mapIdToPlaylist[allSongPlaylist.id] = allSongPlaylist

        // Add Queue Playlist

        // Add All System Playlists
        val systemPlaylists = _root_ide_package_.com.fahimshariar.fahimshariar.loader.medialoader.PlaylistLoader.getAllPlaylists(
            _root_ide_package_.com.fahimshariar.fahimshariar.App.getInstance())
        systemPlaylists.forEach {
            val songIds = _root_ide_package_.com.fahimshariar.fahimshariar.loader.medialoader.PlaylistSongLoader.getPlaylistSongIds(
                _root_ide_package_.com.fahimshariar.fahimshariar.App.getInstance(), it.id)
            val mpPlaylist = MPPlaylist(it.id, it.name)
            mpPlaylist.songs.addAll(songIds)
            mapIdToPlaylist[mpPlaylist.id] = mpPlaylist
        }

        // Add Auto-generated Playlist

        // Add User Playlist

        isLoadedPlaylistsInternal.set(true)
        EventKey.OnLoadedPlaylists.post()
    }

    private fun loadAllArtists() {
        mapArtistIdToArtist.clear()
        allArtists.clear()
        val systemArtists = _root_ide_package_.com.fahimshariar.fahimshariar.loader.medialoader.ArtistLoader.getAllArtists(Utils.getApp())
        allArtists.addAll(systemArtists)
        systemArtists.forEach {
            mapArtistIdToArtist[it.id] = it
        }

        isLoadedArtistsInternal.set(true)
        EventKey.OnLoadedArtists.post()
    }

    @JvmStatic
    fun clearMedia() {
        mapIdToSong.clear()
        mapIdToPlaylist.clear()

        isLoadingMediaInternal.set(false)
        isLoadedMediaInternal.set(false)

        isLoadedSongsInternal.set(false)
        isLoadedPlaylistsInternal.set(false)
        isLoadedArtistsInternal.set(false)
    }
}