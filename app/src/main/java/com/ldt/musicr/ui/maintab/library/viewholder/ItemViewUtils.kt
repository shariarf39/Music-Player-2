package com.ldt.musicr.ui.maintab.library.viewholder

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ldt.musicr.contract.AbsMediaAdapter
import com.ldt.musicr.contract.AbsSongAdapter
import com.ldt.musicr.helper.menu.SongMenuHelper
import com.ldt.musicr.helper.songpreview.SongPreviewController
import com.ldt.musicr.interactors.postDelayedOnUiThread
import com.ldt.musicr.model.Song
import com.ldt.musicr.service.MusicPlayerRemote
import com.ldt.musicr.ui.bottomsheet.OptionBottomSheet

object ItemViewUtils {
    private val mOptionRes = SongMenuHelper.SONG_OPTION
    fun handleMenuClickOnNormalSongItem(viewHolder: RecyclerView.ViewHolder, song: Song?, position: Int) {
        song ?: return
        OptionBottomSheet
            .newInstance(mOptionRes, song)
            .show((viewHolder.itemView.context as AppCompatActivity).supportFragmentManager, "song_popup_menu")
    }

    /**
     * Params: Playlist (object or id)
     * Expect: Process play all a playlist
     */
    fun processPlayAll(playlistId: String) {

    }

    fun processPlayAll(songs: List<Song>, startPosition: Int, playNow: Boolean, viewHolder: RecyclerView.ViewHolder) {
        postDelayedOnUiThread(100) {
            MusicPlayerRemote.openQueue(songs, startPosition, playNow)
            notifyPlayingStateForLegacyAdapter(startPosition, viewHolder)
        }

    }

    private fun notifyPlayingStateForLegacyAdapter(startPosition: Int, viewHolder: RecyclerView.ViewHolder) {
        postDelayedOnUiThread(50) {
            // Behavior của Adapter cũ: cập nhật lại playing state cho item cũ và mới
            (viewHolder.bindingAdapter as? AbsSongAdapter)?.apply {
                notifyItemChanged(getMediaHolderPosition(mMediaPlayDataItem), AbsMediaAdapter.PLAY_STATE_CHANGED)
                notifyItemChanged(getMediaHolderPosition(startPosition), AbsMediaAdapter.PLAY_STATE_CHANGED)
                mMediaPlayDataItem = startPosition
            }
        }
    }

    /**
     * Stop current and preview this song
     */
    fun previewSongsAndStopCurrent(song: Song) {
        SongPreviewController.getInstance().previewSongsAndStopCurrent(song)
    }

    /**
     * Preview this song or stop it if this one is the current previewing
     */
    fun previewSongsOrStopCurrent(song: Song) {
        SongPreviewController.getInstance().apply {
            if(isPlayingPreview && isPreviewingSong(song)) {
                cancelPreview()
            } else {
                previewSongs(song)
            }
        }
    }
}