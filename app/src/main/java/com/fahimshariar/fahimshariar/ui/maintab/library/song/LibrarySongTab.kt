package com.fahimshariar.fahimshariar.ui.maintab.library.song

import android.content.Context
import com.fahimshariar.fahimshariar.ui.bottomsheet.SortOrderBottomSheet.SortOrderChangedListener
import com.fahimshariar.fahimshariar.ui.maintab.library.song.PreviewRandomPlayAdapter.FirstItemCallBack
import com.ldt.fahimshariar.R
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.fahimshariar.fahimshariar.App
import com.fahimshariar.fahimshariar.util.InterpolatorUtil
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fahimshariar.fahimshariar.model.Song
import com.bumptech.glide.Glide
import com.ldt.fahimshariar.common.AppConfig
import com.ldt.fahimshariar.common.MediaManager
import com.fahimshariar.fahimshariar.helper.songpreview.SongPreviewController
import com.ldt.fahimshariar.interactors.AppExecutors
import com.ldt.fahimshariar.interactors.runOnUiThread
import com.ldt.fahimshariar.model.item.DataItem
import com.ldt.fahimshariar.notification.Action
import com.ldt.fahimshariar.notification.ActionResponder
import com.ldt.fahimshariar.notification.EventKey
import com.ldt.fahimshariar.notification.MediaKey
import com.ldt.fahimshariar.ui.maintab.library.adapter.MediaAdapter
import com.fahimshariar.fahimshariar.util.Tool
import com.fahimshariar.fahimshariar.util.Util
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import com.zalo.gitlabmobile.notification.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class LibrarySongTab : Fragment(R.layout.screen_songs_tab), SortOrderChangedListener, FirstItemCallBack, ActionResponder {
    private lateinit var recyclerView: RecyclerView

    private lateinit var refreshView: ImageView
    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var artistTextView: TextView
    private lateinit var shuffleTileGroup: Group
    private var currentSortOrder = 0

    private fun initSortOrder() {
        currentSortOrder = _root_ide_package_.com.fahimshariar.fahimshariar.App.getInstance().preferencesUtility.songChildSortOrder
    }

    fun shuffle() {
        //adapter.shuffle()
    }

    private val adapter: MediaAdapter = MediaAdapter()

    private fun refresh() {
        refreshView.animate().rotationBy(360f).setInterpolator(_root_ide_package_.com.fahimshariar.fahimshariar.util.InterpolatorUtil.getInterpolator(6)).duration = 650
        refreshView.postDelayed({ /*adapter.randomize()*/ }, 300)
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        refreshView = view.findViewById(R.id.refresh)
        imageView = view.findViewById(R.id.image)
        titleTextView = view.findViewById(R.id.title)
        artistTextView = view.findViewById(R.id.description)
        shuffleTileGroup = view.findViewById(R.id.random_group)

        view.findViewById<View>(R.id.preview_random_panel).setOnClickListener { shuffle() }
        view.findViewById<View>(R.id.refresh).setOnClickListener { refresh() }

        adapter.layoutInflater = layoutInflater

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        ViewCompat.setOnApplyWindowInsetsListener(recyclerView) { v, insets ->
            v.setPadding(
                insets.systemWindowInsetLeft,
                0,
                insets.systemWindowInsetRight,
                (insets.systemWindowInsetBottom + v.resources.getDimension(R.dimen.bottom_back_stack_spacing)).toInt()
            )
            ViewCompat.onApplyWindowInsets(v, insets)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.actionResponder = this
        _root_ide_package_.com.fahimshariar.fahimshariar.helper.songpreview.SongPreviewController.getInstance().addSongPreviewListener(adapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initSortOrder()
        refreshData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _root_ide_package_.com.fahimshariar.fahimshariar.helper.songpreview.SongPreviewController.getInstance().removeAudioPreviewerListener(adapter)
    }

    private fun submitList(data: List<DataItem>) {
        runOnUiThread {
            if(isAdded && !isRemoving) {
                val shouldShowPreview = AppConfig.isShowShuffleTileInLibrarySongTab && data.isNotEmpty() && data[0] is DataItem.SongItem
                showOrHideShuffleTile(shouldShowPreview)
                adapter.submitList(data)
            }
        }
    }

    private fun refreshData() {
        AppExecutors.io().execute {

            if(!MediaManager.isLoadedPlaylists) {
                // fetch later
                return@execute
            }

            val result = mutableListOf<DataItem>()
            val playlist = MediaManager.getPlaylist(MediaKey.PLAYLIST_ID_ALL_SONGS) ?: run {
                if(AppConfig.isShowEmptyViewInLibrarySongTab) {
                    result.add(DataItem.Empty.Error)
                }
                submitList(result)
                return@execute
            }

            val songs = mutableListOf<DataItem.SongItem>()

            playlist.songs.forEach { songId ->
                MediaManager.getSong(songId)?.also { song ->
                    songs.add(DataItem.SongItem(0, song, songs.size, playlist.id))
                }
            }

            result.addAll(songs)

            // Add Sorting Tile
            if(AppConfig.isShowSortingTileInLibrarySongTab && songs.isNotEmpty()) {
                result.add(0, DataItem.SortingTile)
            }

            // Submit result
            submitList(result)
        }
    }

    private fun showOrHideShuffleTile(show: Boolean) {
        val v = if (show) View.VISIBLE else View.GONE
        shuffleTileGroup.visibility = v
    }

    override fun onFirstItemCreated(song: _root_ide_package_.com.fahimshariar.fahimshariar.model.Song) {
        titleTextView.text = song.title
        artistTextView.text = song.artistName
        Glide.with(this)
            .load(_root_ide_package_.com.fahimshariar.fahimshariar.util.Util.getAlbumArtUri(song.albumId.toLong()))
            .placeholder(R.drawable.ic_music_style)
            .error(R.drawable.music_empty)
            .into(imageView)
    }

    override fun getSavedOrder(): Int {
        return currentSortOrder
    }

    override fun onOrderChanged(newType: Int, name: String) {
        if (currentSortOrder != newType) {
            currentSortOrder = newType
            _root_ide_package_.com.fahimshariar.fahimshariar.App.getInstance().preferencesUtility.songChildSortOrder = currentSortOrder
            refreshData()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onReceiveEvent(messageEvent: MessageEvent) {
        when(messageEvent.key) {
            EventKey.OnLoadedPlaylists -> {
                refreshData()
            }
            EventKey.OnMediaStoreChanged -> {
                refreshData()
            }
            EventKey.OnPaletteChanged -> {
                if (recyclerView is FastScrollRecyclerView) {
                    val recyclerView = recyclerView as FastScrollRecyclerView
                    recyclerView.setPopupBgColor(_root_ide_package_.com.fahimshariar.fahimshariar.util.Tool.getHeavyColor())
                    recyclerView.setThumbColor(_root_ide_package_.com.fahimshariar.fahimshariar.util.Tool.getHeavyColor())
                }
                adapter.onThemeChanged()
                //adapter.notifyOnMediaStateChanged(AbsMediaAdapter.PALETTE_CHANGED)
            }
            EventKey.OnPlayStateChanged -> {
                //adapter.notifyOnMediaStateChanged(AbsMediaAdapter.PLAY_STATE_CHANGED)
                adapter.onPlayingStateChanged()
            }
            EventKey.OnPlayingMetaChanged -> {
                //adapter.notifyOnMediaStateChanged(AbsMediaAdapter.PLAY_STATE_CHANGED)
            }
            EventKey.OnQueueChanged -> { }
            EventKey.OnRepeatModeChanged -> { }
            EventKey.OnShuffleModeChanged -> { }
            EventKey.Other -> { }
            else -> { }
        }
    }

    override fun invoke(action: Action<Any>): Action<Any>? {
        return null
    }

    companion object {
        const val TAG = "SongChildTab"
    }
}