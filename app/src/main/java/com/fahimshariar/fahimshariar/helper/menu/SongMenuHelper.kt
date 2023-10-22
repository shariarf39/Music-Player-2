package com.fahimshariar.fahimshariar.helper.menu

import androidx.annotation.StringRes
import com.ldt.fahimshariar.R
import androidx.appcompat.app.AppCompatActivity
import com.fahimshariar.fahimshariar.model.Song
import com.fahimshariar.fahimshariar.ui.AppActivity
import com.fahimshariar.fahimshariar.helper.songpreview.SongPreviewController
import com.fahimshariar.fahimshariar.loader.medialoader.SongLoader
import android.content.Intent
import com.fahimshariar.fahimshariar.util.MusicUtil
import com.fahimshariar.fahimshariar.ui.dialog.DeleteSongsDialog
import com.fahimshariar.fahimshariar.ui.dialog.AddToPlaylistDialog
import com.fahimshariar.fahimshariar.service.MusicPlayerRemote
import com.fahimshariar.fahimshariar.ui.floating.LyricFragment
import com.fahimshariar.fahimshariar.util.NavigationUtil
import com.fahimshariar.fahimshariar.util.RingtoneManager

object SongMenuHelper {
    @JvmField
    @StringRes
    val SONG_OPTION = intArrayOf( /*   R.string.play,*/
        R.string.play_next,
        R.string.play_preview,
        R.string.play_preview_all,
        R.string.add_to_queue,
        R.string.add_to_playlist,  /*    R.string.go_to_source_playlist,*/ /*        R.string.go_to_album,*/
        R.string.go_to_artist,
        R.string.show_lyric,
        R.string.edit_tag,
        R.string.detail,
        R.string.divider,
        R.string.share,
        R.string.set_as_ringtone,  /*  R.string.delete_from_playlist,*/
        R.string.delete_from_device
    )

    @StringRes
    val SONG_OPTION_STOP_PREVIEW = intArrayOf( /*   R.string.play,*/
        R.string.play_next,
        R.string.play_preview,
        R.string.str_stop_play_preview,
        R.string.add_to_queue,
        R.string.add_to_playlist,  /*    R.string.go_to_source_playlist,*/ /*        R.string.go_to_album,*/
        R.string.go_to_artist,
        R.string.show_lyric,
        R.string.edit_tag,
        R.string.detail,
        R.string.divider,
        R.string.share,
        R.string.set_as_ringtone,  /*  R.string.delete_from_playlist,*/
        R.string.delete_from_device
    )

    @JvmField
    @StringRes
    val SONG_QUEUE_OPTION = intArrayOf(
        R.string.play_next,
        R.string.play_preview,
        R.string.remove_from_queue,
        R.string.add_to_playlist,  /* R.string.go_to_source_playlist,*/ /*        R.string.go_to_album,*/
        R.string.go_to_artist,
        R.string.show_lyric,
        R.string.edit_tag,
        R.string.detail,
        R.string.divider,
        R.string.share,
        R.string.set_as_ringtone,  /*  R.string.delete_from_playlist,*/
        R.string.delete_from_device
    )

    @JvmField
    @StringRes
    val NOW_PLAYING_OPTION = intArrayOf(
        R.string.repeat_it_again,
        R.string.play_preview,  /* R.string.remove_from_queue,*/ /*  R.string.go_to_source_playlist,*/
        R.string.add_to_playlist,  /*        R.string.go_to_album,*/
        R.string.go_to_artist,
        R.string.show_lyric,
        R.string.edit_tag,
        R.string.detail,
        R.string.divider,
        R.string.share,
        R.string.set_as_ringtone,  /*  R.string.delete_from_playlist,*/
        R.string.delete_from_device
    )

    @JvmField
    @StringRes
    val SONG_ARTIST_OPTION = intArrayOf( /*   R.string.play,*/
        R.string.play_next,
        R.string.play_preview,
        R.string.add_to_queue,
        R.string.add_to_playlist,  /*    R.string.go_to_source_playlist,*/ /*        R.string.go_to_album,*/ /*R.string.go_to_artist,*/
        R.string.show_lyric,
        R.string.edit_tag,
        R.string.detail,
        R.string.divider,
        R.string.share,
        R.string.set_as_ringtone,  /*  R.string.delete_from_playlist,*/
        R.string.delete_from_device
    )

    @JvmStatic
    fun handleMenuClick(activity: AppCompatActivity, song: _root_ide_package_.com.fahimshariar.fahimshariar.model.Song, string_res_option: Int): Boolean {
        when (string_res_option) {
            R.string.play_preview -> if (activity is _root_ide_package_.com.fahimshariar.fahimshariar.ui.AppActivity) {
                activity.songPreviewController.previewSongs(song)
            }
            R.string.play_preview_all -> {
                val preview = _root_ide_package_.com.fahimshariar.fahimshariar.helper.songpreview.SongPreviewController.getInstance()
                if (preview != null) {
                    if (preview.isPlayingPreview) preview.cancelPreview()
                    val list = _root_ide_package_.com.fahimshariar.fahimshariar.loader.medialoader.SongLoader.getAllSongs(activity)
                    list.shuffle()
                    var index = 0
                    var i = 0
                    while (i < list.size) {
                        if (song.id == list[i]?.id) index = i
                        i++
                    }
                    if (index != 0) list.add(0, list.removeAt(index))
                    preview.previewSongsAndStopCurrent(list)
                }
            }
            R.string.str_stop_play_preview -> {
                val controller = _root_ide_package_.com.fahimshariar.fahimshariar.helper.songpreview.SongPreviewController.getInstance()
                if (controller != null && controller.isPlayingPreview) {
                    _root_ide_package_.com.fahimshariar.fahimshariar.helper.songpreview.SongPreviewController.getInstance().cancelPreview()
                }
            }
            R.string.set_as_ringtone -> {
                if (_root_ide_package_.com.fahimshariar.fahimshariar.util.RingtoneManager.requiresDialog(activity)) {
                    _root_ide_package_.com.fahimshariar.fahimshariar.util.RingtoneManager.showDialog(activity)
                } else {
                    val ringtoneManager =
                        _root_ide_package_.com.fahimshariar.fahimshariar.util.RingtoneManager()
                    ringtoneManager.setRingtone(activity, song.id)
                }
                return true
            }
            R.string.share -> {
                activity.startActivity(Intent.createChooser(_root_ide_package_.com.fahimshariar.fahimshariar.util.MusicUtil.createShareSongFileIntent(song, activity), null))
                return true
            }
            R.string.delete_from_device -> {
                _root_ide_package_.com.fahimshariar.fahimshariar.ui.dialog.DeleteSongsDialog.create(song).show(activity.supportFragmentManager, "DELETE_SONGS")
                return true
            }
            R.string.add_to_playlist -> {
                _root_ide_package_.com.fahimshariar.fahimshariar.ui.dialog.AddToPlaylistDialog.create(song).show(activity.supportFragmentManager, "ADD_PLAYLIST")
                return true
            }
            R.string.repeat_it_again, R.string.play_next -> {
                _root_ide_package_.com.fahimshariar.fahimshariar.service.MusicPlayerRemote.playNext(song)
                return true
            }
            R.string.add_to_queue -> {
                _root_ide_package_.com.fahimshariar.fahimshariar.service.MusicPlayerRemote.enqueue(song)
                return true
            }
            R.string.show_lyric -> _root_ide_package_.com.fahimshariar.fahimshariar.ui.floating.LyricFragment.newInstance(song).show(activity.supportFragmentManager, _root_ide_package_.com.fahimshariar.fahimshariar.ui.floating.LyricFragment.TAG)
            R.string.edit_tag -> return true
            R.string.detail ->                 //  SongDetailDialog.create(song).show(activity.getSupportFragmentManager(), "SONG_DETAILS");
                return true
            R.string.go_to_album ->                 // NavigationUtil.goToAlbum(activity, song.albumId);
                return true
            R.string.go_to_artist -> {
                _root_ide_package_.com.fahimshariar.fahimshariar.util.NavigationUtil.navigateToArtist(activity, song.artistId)
                return true
            }
        }
        return false
    }
}