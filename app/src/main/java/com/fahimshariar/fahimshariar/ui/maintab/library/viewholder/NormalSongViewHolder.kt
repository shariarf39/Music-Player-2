package com.fahimshariar.fahimshariar.ui.maintab.library.viewholder

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.ldt.fahimshariar.R
import android.widget.TextView
import com.fahimshariar.fahimshariar.ui.widget.CircularPlayPauseProgressBar
import com.fahimshariar.fahimshariar.model.Song
import android.os.Build
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.fahimshariar.fahimshariar.helper.songpreview.PreviewSong
import com.fahimshariar.fahimshariar.service.MusicPlayerRemote
import com.ldt.fahimshariar.model.item.DataItem
import com.ldt.fahimshariar.notification.ActionResponder
import com.ldt.fahimshariar.provider.ColorProvider
import com.ldt.fahimshariar.utils.ArtworkUtils

open class NormalSongViewHolder(parent: ViewGroup, private val actionResponder: ActionResponder?, layoutInflater: LayoutInflater? = null) : RecyclerView.ViewHolder((layoutInflater ?: LayoutInflater.from(parent.context)).inflate(R.layout.item_song_normal, parent, false)), BindPlayingState, BindPreviewState, BindTheme {
    private val numberView: TextView = itemView.findViewById(R.id.number)
    private val titleTextView: TextView = itemView.findViewById(R.id.title)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.description)
    private val imageView: ImageView = itemView.findViewById(R.id.image)
    private val quickPlayPauseView: ImageView = itemView.findViewById(R.id.quick_play_pause)
    private val menuView: View = itemView.findViewById(R.id.menu_button)
    private val panelView: View = itemView.findViewById(R.id.panel)
    private val previewView: _root_ide_package_.com.fahimshariar.fahimshariar.ui.widget.CircularPlayPauseProgressBar = itemView.findViewById(R.id.preview_button)
    private var data: DataItem.SongItem? = null

    private val playState = booleanArrayOf(false, false)

    init {
        itemView.setOnClickListener {
            data?.also { ItemViewUtils.playPlaylist(it.playlistId, it.song.id, true) }
        }

        itemView.findViewById<View>(R.id.menu_button).setOnClickListener {
            data?.also { ItemViewUtils.handleMenuClickOnNormalSongItem(this, it.song, bindingAdapterPosition) }
        }

        previewView.setOnClickListener {
            data?.also { ItemViewUtils.previewSongsOrStopCurrent(it.song) }
        }
    }

    fun bind(item: DataItem.SongItem, previewingSong: _root_ide_package_.com.fahimshariar.fahimshariar.helper.songpreview.PreviewSong? = null) {
        val isTheSame = this.data?.song?.id == item.song.id
        this.data = item

        numberView.text = numberView.resources.getString(R.string.str_number_placeholder, item.positionInData + 1)
        titleTextView.text = item.name
        descriptionTextView.text = item.subName
        bindPlayingState()
        bindPreviewButton(item.song, previewingSong)
        bindTheme()
        if(!isTheSame) {
            ArtworkUtils.loadAlbumArtworkBySong(imageView, item.song)
        }
    }

    override fun bindTheme() {
        bindPlayingStateColor()

        // Touch effect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val baseColorStateList = ColorProvider.baseColorStateList
            (panelView.background as RippleDrawable).setColor(baseColorStateList)
            (menuView.background as RippleDrawable).setColor(baseColorStateList)
            (previewView.background as RippleDrawable).setColor(baseColorStateList)
        }

        data?.also {
            if(DataItem.hasFlag(it.flags, DataItem.FLAG_DIM)) {
                //itemView.setBackgroundColor((0xFF212121).toInt())
                itemView.setBackgroundResource(R.drawable.search_song_normal_background)
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    private fun bindPreviewButton(song: _root_ide_package_.com.fahimshariar.fahimshariar.model.Song, previewingSong: _root_ide_package_.com.fahimshariar.fahimshariar.helper.songpreview.PreviewSong?) {
        if ((previewingSong == null || previewingSong.song.id != song.id) && previewView.mode == _root_ide_package_.com.fahimshariar.fahimshariar.ui.widget.CircularPlayPauseProgressBar.PLAYING) {
            previewView.resetProgress()
        } else if (previewingSong != null && previewingSong.song == song) {
            var timePlayed = previewingSong.timePlayed
            if (timePlayed == -1L) previewView.resetProgress() else {
                if (timePlayed < 0) timePlayed = 0
                if (timePlayed <= previewingSong.totalPreviewDuration) previewView.syncProgress(previewingSong.totalPreviewDuration, timePlayed.toInt())
            }
        }
    }

    override fun bindPreviewState(previewSong: _root_ide_package_.com.fahimshariar.fahimshariar.helper.songpreview.PreviewSong?) {
        data?.also {
            bindPreviewButton(it.song, previewSong)
        }
    }

    override fun bindPlayingState() {
        val isCurrentSongNew = data?.song?.id == _root_ide_package_.com.fahimshariar.fahimshariar.service.MusicPlayerRemote.getCurrentSong().id
        val isPlayingNew = _root_ide_package_.com.fahimshariar.fahimshariar.service.MusicPlayerRemote.isPlaying()

        // no changes, so return
        if(playState[0] == isCurrentSongNew && playState[1] == isPlayingNew) return
        playState[0] = isCurrentSongNew
        playState[1] = isPlayingNew
        bindPlayingStateNonColor()
        bindPlayingStateColor()

        if(playState[0]) {
            imageView.setOnClickListener { _root_ide_package_.com.fahimshariar.fahimshariar.service.MusicPlayerRemote.playOrPause() }
        } else {
            imageView.setOnClickListener(null)
            imageView.isClickable = false
            imageView.isFocusable = false
        }
    }

    private fun bindPlayingStateColor() {
        // current song
        if(playState[0]) {
            titleTextView.setTextColor(ColorProvider.baseColorL60)
            descriptionTextView.setTextColor(ColorProvider.baseColorAaa)
            quickPlayPauseView.setColorFilter(ColorProvider.baseColorL60)
        } else {
            // not current song
            titleTextView.setTextColor(ColorProvider.flatWhite)
            descriptionTextView.setTextColor(ColorProvider.flatWhiteAaa)
        }
    }

    private fun bindPlayingStateNonColor() {
        when {
            playState[0] && playState[1] -> quickPlayPauseView.setImageResource(R.drawable.ic_volume_up_black_24dp)
            playState[0] -> quickPlayPauseView.setImageResource(R.drawable.ic_volume_mute_black_24dp)
            else -> quickPlayPauseView.setImageDrawable(null)
        }
    }
}