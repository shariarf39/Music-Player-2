package com.fahimshariar.fahimshariar.ui.maintab.library.viewholder

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ldt.fahimshariar.common.MediaManager
import com.fahimshariar.fahimshariar.contract.AbsMediaAdapter
import com.fahimshariar.fahimshariar.contract.AbsSongAdapter
import com.ldt.fahimshariar.helper.menu.SongMenuHelper
import com.fahimshariar.fahimshariar.helper.songpreview.SongPreviewController
import com.ldt.fahimshariar.interactors.postDelayedOnUiThread
import com.fahimshariar.fahimshariar.model.Media
import com.fahimshariar.fahimshariar.model.Song
import com.fahimshariar.fahimshariar.service.MusicPlayerRemote
import com.fahimshariar.fahimshariar.ui.bottomsheet.OptionBottomSheet
import com.fahimshariar.fahimshariar.utils.KeyboardUtils
import com.ldt.fahimshariar.utils.Utils

object ItemViewUtils {
    private val mOptionRes = SongMenuHelper.SONG_OPTION

    fun selectOptionByMediaType(media: _root_ide_package_.com.fahimshariar.fahimshariar.model.Media?): IntArray? {
        return when(media) {
            is _root_ide_package_.com.fahimshariar.fahimshariar.model.Song -> {
                if(_root_ide_package_.com.fahimshariar.fahimshariar.helper.songpreview.SongPreviewController.getInstance().isPlayingPreview) SongMenuHelper.SONG_OPTION_STOP_PREVIEW else SongMenuHelper.SONG_OPTION
            }
            else -> null
        }
    }
    fun handleMenuClickOnNormalSongItem(viewHolder: RecyclerView.ViewHolder, song: _root_ide_package_.com.fahimshariar.fahimshariar.model.Song?, position: Int) {
        song ?: return
        val options = selectOptionByMediaType(song) ?: run {
            Utils.showGeneralErrorToast()
            return
        }
        _root_ide_package_.com.fahimshariar.fahimshariar.utils.KeyboardUtils.hideSoftInput(viewHolder.itemView)
        _root_ide_package_.com.fahimshariar.fahimshariar.ui.bottomsheet.OptionBottomSheet
            .newInstance(options, song)
            .show((viewHolder.itemView.context as AppCompatActivity).supportFragmentManager, "song_popup_menu")
    }

    fun processPlayAll(songs: List<_root_ide_package_.com.fahimshariar.fahimshariar.model.Song>, startPosition: Int, playNow: Boolean, viewHolder: RecyclerView.ViewHolder) {
        postDelayedOnUiThread(100) {
            _root_ide_package_.com.fahimshariar.fahimshariar.service.MusicPlayerRemote.openQueue(songs, startPosition, playNow)
            notifyPlayingStateForLegacyAdapter(startPosition, viewHolder)
        }

    }

    private fun notifyPlayingStateForLegacyAdapter(startPosition: Int, viewHolder: RecyclerView.ViewHolder) {
        postDelayedOnUiThread(50) {
            // Behavior của Adapter cũ: cập nhật lại playing state cho item cũ và mới
            (viewHolder.bindingAdapter as? _root_ide_package_.com.fahimshariar.fahimshariar.contract.AbsSongAdapter)?.apply {
                notifyItemChanged(getMediaHolderPosition(mMediaPlayDataItem), _root_ide_package_.com.fahimshariar.fahimshariar.contract.AbsMediaAdapter.PLAY_STATE_CHANGED)
                notifyItemChanged(getMediaHolderPosition(startPosition), _root_ide_package_.com.fahimshariar.fahimshariar.contract.AbsMediaAdapter.PLAY_STATE_CHANGED)
                mMediaPlayDataItem = startPosition
            }
        }
    }

    /**
     * Stop current and preview this song
     */
    fun previewSongsAndStopCurrent(song: _root_ide_package_.com.fahimshariar.fahimshariar.model.Song) {
        _root_ide_package_.com.fahimshariar.fahimshariar.helper.songpreview.SongPreviewController.getInstance().previewSongsAndStopCurrent(song)
    }

    /**
     * Preview this song or stop it if this one is the current previewing
     */
    fun previewSongsOrStopCurrent(song: _root_ide_package_.com.fahimshariar.fahimshariar.model.Song) {
        _root_ide_package_.com.fahimshariar.fahimshariar.helper.songpreview.SongPreviewController.getInstance().apply {
            if(isPlayingPreview && isPreviewingSong(song)) {
                cancelPreview()
            } else {
                previewSongs(song)
            }
        }
    }

    /**
     * Play all songs in given playlist
     */
    fun playPlaylist(playlistId: Int, firstSongId: Int? = null, shuffle: Boolean = true): Boolean {
        val playlist = MediaManager.getPlaylist(playlistId) ?: return false
        val songs = mutableListOf<_root_ide_package_.com.fahimshariar.fahimshariar.model.Song>()
        var firstSongIndex = -1

        playlist.songs.forEachIndexed { index, id ->
            MediaManager.getSong(id)?.apply {
                if(id == firstSongId) {
                    firstSongIndex = index
                }
                songs.add(this)
            }
        }

        // The playlist doesn't contain song
        if(firstSongId != null && firstSongIndex == -1) return false
        _root_ide_package_.com.fahimshariar.fahimshariar.service.MusicPlayerRemote.openQueue(songs, firstSongIndex, true)
        return false
    }
}