package com.fahimshariar.fahimshariar.notification

import com.ldt.fahimshariar.helper.extension.post
import com.fahimshariar.fahimshariar.service.MusicServiceEventListener

object GlobalEventBusMusicEventListener:
    _root_ide_package_.com.fahimshariar.fahimshariar.service.MusicServiceEventListener {
    override fun onMediaStoreChanged() = EventKey.OnMediaStoreChanged.post()

    override fun onPaletteChanged() = EventKey.OnPaletteChanged.post()

    override fun onPlayStateChanged() = EventKey.OnPlayStateChanged.post()

    override fun onPlayingMetaChanged() = EventKey.OnPlayingMetaChanged.post()

    override fun onQueueChanged() = EventKey.OnQueueChanged.post()

    override fun onRepeatModeChanged() = EventKey.OnRepeatModeChanged.post()

    override fun onShuffleModeChanged() = EventKey.OnShuffleModeChanged.post()

    override fun onServiceConnected() = EventKey.OnServiceConnected.post()

    override fun onServiceDisconnected() = EventKey.OnServiceDisconnected.post()
}